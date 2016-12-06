/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.chat.model.db.Message;
import org.chat.model.db.MessageFacade;
import org.chat.model.db.User;
import org.chat.model.db.UserFacade;

/**
 *
 * @author Roberta
 */
public class LocalDatabase {

    private static Map<String, User> users = null;
    private static List<Message> messages = null;

    private LocalDatabase() {

    }

    public static Map<String, User> getUsers() {
        if (users == null) {
            users = new HashMap<>();
        }
        return users;
    }

    public static List<Message> getMessages() {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public static Message getMessage(Long id) {
        Message m = null;
        for (int i = 0; i < messages.size(); i++) {
            if (Objects.equals(messages.get(i).getId(), id)) {
                m = messages.get(i);
            }
        }
        return m;
    }

    public static List<Message> getChatMessages(String me, String other) {
        List<Message> result = new ArrayList<>();

        if (messages != null && !messages.isEmpty()) {
            if (other.equalsIgnoreCase("GROUP")) {
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getReceiver().equalsIgnoreCase("GROUP")) {
                        result.add(messages.get(i));
                    }
                }
            } else {
                for (int i = 0; i < messages.size(); i++) {
                    Message m = messages.get(i);
                    if ((m.getReceiver().equalsIgnoreCase(me) && m.getSender().equalsIgnoreCase(other)) && m.getReceiverVisibility()
                            || (m.getReceiver().equalsIgnoreCase(other) && m.getSender().equalsIgnoreCase(me)) && m.getSenderVisibility()) {
                        result.add(messages.get(i));
                    }
                }
            }
        }
        return result;
    }

    public static void loadLocalDatabase() {
        List<User> users = UserFacade.getAllUsers();
        List<Message> messages = MessageFacade.getAllMessages();
        try {

            loadUsers(users);
            loadMessages(messages);

            System.out.println("LOCAL DATABASE UPDATED");

            System.out.println("- Users : ");
            System.out.println(LocalDatabase.getUsers().toString());

            System.out.println("- Messages : ");
            System.out.println(LocalDatabase.getMessages().toString());

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void loadUsers(List<User> users) {
        if (users != null && !users.isEmpty()) {
            users.stream().map((u) -> {
                users.stream().filter((u1) -> (!u.getUsername().equals(u1.getUsername()))).map((u1) -> {
                    u.getUserMessages().put(u1, new LinkedList<>());
                    return u1;
                }).forEachOrdered((u1) -> {
                    u.getBufferUserMessages().put(u1, new LinkedList<>());
                });
                return u;
            }).forEachOrdered((u) -> {
                LocalDatabase.getUsers().put(u.getUsername().toUpperCase(), u);
            });
        } else {
            System.out.println("The User table is empty.");
        }
    }

    private static void loadMessages(List<Message> messages) {

        if (messages != null && !messages.isEmpty()) {
            for (Message m : messages) {
                User sender = LocalDatabase.getUsers().get(m.getSender().toUpperCase());
                User receiver = LocalDatabase.getUsers().get(m.getReceiver().toUpperCase());

                if (sender.getUserMessages() == null) {
                    sender.getUserMessages().put(receiver, new LinkedList<>());
                }

                if (sender.getBufferUserMessages() == null) {
                    sender.getBufferUserMessages().put(receiver, new LinkedList<>());
                }

                if (receiver.getUserMessages() == null) {
                    receiver.getUserMessages().put(sender, new LinkedList<>());
                }

                if (receiver.getBufferUserMessages() == null) {
                    receiver.getBufferUserMessages().put(sender, new LinkedList<>());
                }

                sender.getUserMessages().get(receiver).add(m);
                receiver.getUserMessages().get(sender).add(m);

                if (m.getReceiver().equalsIgnoreCase("GROUP")) {
                    for (Map.Entry<String, User> entry : LocalDatabase.getUsers().entrySet()) {
                        LocalDatabase.getUsers().get(entry.getKey().toUpperCase()).getChatRoomMessages().add(m);
                    }
                }

                LocalDatabase.getMessages().add(m);
            }
        } else {
            System.out.println("Message Table Empty.");
        }

    }

    public static Map<User, Message> getLastMessages(String username) {
        
        Map<User, Message> map = new HashMap<>();

        User user = LocalDatabase.getUsers().get(username.toUpperCase());
        Message message = null;
        for (User u : LocalDatabase.getUsers().values()) {
            if (u != user) {
                switch (u.getUsername()) {
                    case "GROUP":
                        if (user.getChatRoomMessages() != null && !user.getChatRoomMessages().isEmpty()) {
                            message = user.getChatRoomMessages().get(user.getChatRoomMessages().size() - 1);
                        } else {
                            message = null;
                        }
                        break;
                    default:
                        List<Message> m = user.getUserMessages().get(LocalDatabase.getUsers().get(u.getUsername().toUpperCase()));
                        if (m != null && !m.isEmpty()) {
                            message = user.getUserMessages().get(LocalDatabase.getUsers().get(u.getUsername().toUpperCase())).getLast();
                        } else {
                            message = null;
                        }
                }
                map.put(u, message);
            }
        }
        return map;
    }
}
