/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.utils;

import org.chat.model.db.Message;
import org.chat.model.db.MessageFacade;
import org.chat.model.db.User;
import org.chat.model.db.UserFacade;

/**
 *
 * @author Roberta
 */
public class ThreadManager implements Runnable {
    
    private final Integer code;
    private User user;
    private Message message;

    public ThreadManager(User user, Integer code) {
        this.user = user;
        this.code = code;
    }

    public ThreadManager(Message message, Integer code) {
        this.message = message;
        this.code = code;
    }
    public ThreadManager(Integer code) {
        this.code = code;
    }

    @Override
    public void run() {
        switch (code) {
            case 1:
                UserFacade.addUser(user);
                break;
            case 2:
                MessageFacade.addMessage(message);
                break;
            case 3:
                MessageFacade.updateAllMessages();
                break;
            default:
                System.out.println("Invalid code");
        }
    }
}
