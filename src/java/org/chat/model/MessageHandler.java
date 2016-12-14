/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.chat.model.db.Message;
import org.chat.model.db.User;
import org.chat.utils.JSON;
import org.chat.utils.ThreadManager;

/**
 *
 * @author Roberta
 */
public class MessageHandler {

    public MessageHandler() {

    }

    public static void sendMessage(HttpServletRequest request) {
        String sender = request.getParameter("sender");
        String receiver = request.getParameter("receiver");
        String text = request.getParameter("text");

        Map<String, User> users = LocalDatabase.getUsers();
        User us = users.get(sender.toUpperCase());
        User ur = users.get(receiver.toUpperCase());

        Message m = new Message(sender, receiver, text);

        if (receiver.equalsIgnoreCase("GROUP")) {
            if (LocalDatabase.getUsers().containsKey(sender.toUpperCase())) {
                for (User u : LocalDatabase.getUsers().values()) {
                    u.getChatRoombufferMessages().add(m);
                    u.getChatRoomMessages().add(m);
                }
            }
        } else {
            
            us.getUserMessages().get(ur).add(m);
            us.getBufferUserMessages().get(ur).add(m);
            ur.getUserMessages().get(us).add(m);
            ur.getBufferUserMessages().get(us).add(m);
        }

        LocalDatabase.getMessages().add(m);

        (new Thread(new ThreadManager(m, 2))).start();
    }

    private static String createMessageJSON(List<Message> messages) {
        List<Object> key = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        List<Object> array = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            array.add("messages");
            key.add("id");
            value.add(m.getId());
            key.add("sender");
            value.add(m.getSender());
            key.add("receiver");
            value.add(m.getReceiver());
            key.add("text");
            value.add(m.getText());
            key.add("datetime");
            value.add(m.getFormattedDate());
        }
        return JSON.create(key, value, array);
    }

    public static String getMessages(HttpServletRequest request) {

        String me = request.getParameter("me");
        String other = request.getParameter("other");

        String json = "";

        User um = LocalDatabase.getUsers().get(me.toUpperCase());
        User uo = LocalDatabase.getUsers().get(other.toUpperCase());
        List<Message> messages = LocalDatabase.getChatMessages(me, other);

        if (messages != null && !messages.isEmpty()) {
            json = createMessageJSON(messages);
        }

        if (other.equalsIgnoreCase("GROUP")) {
            um.getChatRoombufferMessages().clear();
        } else {
            um.getBufferUserMessages().get(uo).clear();
        }

        if (json.equals("")) {
            return JSON.create("messages", "empty");
        }

        return json;
    }

    public static String getNewMessages(HttpServletRequest request) {

        String me = request.getParameter("me");
        String other = request.getParameter("other");

        User um = LocalDatabase.getUsers().get(me.toUpperCase());
        User uo = LocalDatabase.getUsers().get(other.toUpperCase());

        List<Message> buffer = null;

        String json = "";
        if (other.equalsIgnoreCase("GROUP")) {
            buffer = um.getChatRoombufferMessages();
        } else {
            buffer = um.getBufferUserMessages().get(uo);
        }
        if (buffer != null && !buffer.isEmpty()) {
            json = createMessageJSON(buffer);
            buffer.clear();
        }

        if (json.equals("")) {
            return JSON.create("messages", "empty");
        }

        return json;
    }

    public static void deleteMessage(HttpServletRequest request) {

        String me = request.getParameter("me");
        String idMessage = request.getParameter("message");
        
        LocalDatabase.getMessage(Long.parseLong(idMessage)).setUserVisibility(me, Boolean.FALSE);

        (new Thread(new ThreadManager(3))).start();
    }

}
