/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.chat.model.LocalDatabase;

/**
 *
 * @author Roberta
 */
public class ContextListener implements ServletContextListener{
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LocalDatabase.loadLocalDatabase();
        
        System.out.println("Server starting...");
        //System.out.println(this.DH.toString());
        //UserDatabase.printSingleton();
        
        //User ChatRoom = new User("ChatRoom", "P@ssw0rd");
        //ChatRoom.setIsGroup(Boolean.TRUE);
        //UserDatabase.getInstance().put("ChatRoom".toUpperCase(), ChatRoom);
        //UserFacade.addUser(ChatRoom);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Server stopping...");
    }
}
