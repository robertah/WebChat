/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.chat.model.db.Message;
import org.chat.model.db.User;
import org.chat.utils.JSON;
import org.chat.utils.Error;
import org.chat.utils.ThreadManager;

/**
 *
 * @author Roberta
 */
public class UserConnection {

    public static void connect(HttpServletRequest request) {
        String username = request.getParameter("username");
        LocalDatabase.getUsers().get(username.toUpperCase()).setOnline(true);
    }

    public static void leave(HttpServletRequest request) {
        String username = request.getParameter("username");
        LocalDatabase.getUsers().get(username.toUpperCase()).setOnline(false);
    }

    public static String signUp(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeat_password = request.getParameter("repeat_password");

        String value = checkSignUpFields(username, password, repeat_password);

        if (value.length() > 1) {
            return JSON.create("SignUp", value);
        }

        User user = new User(username, password);
        LocalDatabase.getUsers().put(username.toUpperCase(), user);

        for (User u : LocalDatabase.getUsers().values()) {
            if (!user.getUsername().equalsIgnoreCase(u.getUsername())) {
                u.getUserMessages().put(user, new LinkedList<>());
                u.getBufferUserMessages().put(user, new LinkedList<>());
                user.getUserMessages().put(u, new LinkedList<>());
                user.getBufferUserMessages().put(u, new LinkedList<>());
            }
        }

        (new Thread(new ThreadManager(user, 1))).start();
        //UserFacade.addUser(user);

        request.setAttribute("username", username);
        request.getSession().setAttribute("username", username);

        return JSON.create("SignUp", "success");
    }

    private static String checkSignUpFields(String username, String password, String repeat) {

        StringBuilder sb = new StringBuilder("");

        if (username.equals("")) {
            sb.append("- ").append(Error.EMPTY_USERNAME.getMessage()).append("\n");
        } else if (username.length() < 3) {
            sb.append("- ").append(Error.SHORT_USERNAME.getMessage()).append("\n");
        } else if (username.contains(" ")) {
            sb.append("- ").append(Error.SPACE_USERNAME.getMessage()).append("\n");
        } else if (username.equalsIgnoreCase("GROUP")) {
            sb.append("- ").append(Error.INVALID_USERNAME.getMessage()).append("\n");
        }
        if (password.equals("")) {
            sb.append("- ").append(Error.EMPTY_PASSWORD.getMessage()).append("\n");
        } else if (password.length() < 5) {
            sb.append("- ").append(Error.SHORT_PASSWORD.getMessage()).append("\n");
        }

        if (LocalDatabase.getUsers().containsKey(username.toUpperCase())) {
            sb.append("- ").append(Error.CONFLICT_USERNAME.getMessage()).append("\n");
        }

        if (!password.equals(repeat)) {
            sb.append("- ").append(Error.UNMATCH_PASSWORD.getMessage()).append("\n");
        }

        return sb.toString();
    }

    public static String logIn(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String value = checkLogInFields(username, password);
        if (value.length() > 1) {
            return JSON.create("LogIn", value);
        }

        connect(request);

        request.setAttribute("username", username);
        request.getSession().setAttribute("username", username);

        return JSON.create("LogIn", "success");
   
    }

    private static String checkLogInFields(String username, String password) {

        StringBuilder sb = new StringBuilder("");

        if (username.equals("")) {
            sb.append("- ").append(Error.EMPTY_USERNAME.getMessage()).append("\n");
            if (password.equals("")) {
                sb.append("- ").append(Error.EMPTY_PASSWORD.getMessage()).append("\n");
            }
        } else if (username.equalsIgnoreCase("GROUP")) {
            sb.append("- ").append(Error.INVALID_USERNAME).append("\n");
        } else if (!LocalDatabase.getUsers().containsKey(username.toUpperCase())) {
            sb.append("- ").append(Error.UNFOUND_USERNAME.getMessage()).append("\n");
        } else if (!password.equals(LocalDatabase.getUsers().get(username.toUpperCase()).getPassword())) {
            sb.append("- ").append(Error.WRONG_PASSWORD.getMessage()).append("\n");
        }

        return sb.toString();
    }

    public static void logOut(HttpServletRequest request) {
        leave(request);
    }

    public static String getContacts(HttpServletRequest request) {

        String me = request.getParameter("username");
        Set<Entry<String, User>> userMap = LocalDatabase.getUsers().entrySet();

        //separate users with last message and with no last message
        List<User> usersWithLastMessage = new ArrayList<>();
        List<User> usersWithNoLastMessage = new ArrayList<>();
        for (Map.Entry<User, Message> entry : LocalDatabase.getLastMessages(me).entrySet()) {
            if (entry.getValue() != null) {
                usersWithLastMessage.add(entry.getKey());
            } else {
                usersWithNoLastMessage.add(entry.getKey());
            }
        }

        //sort users with last message by time
        Collections.sort(usersWithLastMessage, (User u1, User u2) -> 
                LocalDatabase.getLastMessages(me).get(u1).
                        compareTo(LocalDatabase.getLastMessages(me).get(u2)));
        //sort users with no last message by username
        Collections.sort(usersWithNoLastMessage, (User u1, User u2) -> 
                u1.getUsername().toLowerCase()
                        .compareTo(u2.getUsername().toLowerCase()));

        List<User> sortedUsers = new ArrayList<>();
        sortedUsers.addAll(usersWithLastMessage);
        sortedUsers.addAll(usersWithNoLastMessage);

        String json = "";
        if (sortedUsers != null && !sortedUsers.isEmpty()) {
            json = createContactJSON(sortedUsers, me);
        }
        if (json.equals("")) {
            return JSON.create("contacts", "empty");
        }

        return json;
    }

    private static String createContactJSON(List<User> users, String myUsername) {

        List<Object> key = new ArrayList<>();
        List<Object> value = new ArrayList<>();
        List<Object> array = new ArrayList<>();

        User me = LocalDatabase.getUsers().get(myUsername.toUpperCase());
        for (User u : users) {       
                array.add("contacts");
                key.add("username");
                value.add(u.getUsername());
                key.add("online");
                value.add(u.isOnline());
                if (LocalDatabase.getLastMessages(myUsername).get(u) != null) {
                    key.add("text");
                    value.add(LocalDatabase.getLastMessages(myUsername).get(u).getText());
                    key.add("time");
                    value.add(LocalDatabase.getLastMessages(myUsername).get(u).getFormattedDate());
                    key.add("unread");
                    value.add(me.unreadMessages(u.getUsername()));
                } else {
                    key.add("text");
                    value.add(" ");
                    key.add("time");
                    value.add(" ");
                    key.add("unread");
                    value.add(" ");
                }
        }
        return JSON.create(key, value, array);
    }

}
