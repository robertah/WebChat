/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.model.db;

import java.util.List;
import org.chat.model.LocalDatabase;
import org.chat.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Roberta
 */
public class MessageFacade {
    
    public static List<Message> getAllMessages() {
        
        Session session = null;
        List<Message> messages = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            Query q = session.createQuery("FROM Message");

            messages = (List<Message>) q.list();

        } catch (Exception e) {
            System.out.println(e);
            
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return messages;
    }

    public static void addMessage(Message message) {
        
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            session.save(message);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e);
            
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
    
    public static void updateAllMessages() {
        
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for(int i = 0; i < LocalDatabase.getMessages().size(); i++)
                session.update(LocalDatabase.getMessages().get(i));
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e);
            
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
