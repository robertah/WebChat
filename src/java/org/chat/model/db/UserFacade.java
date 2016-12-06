/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.model.db;

import java.util.List;
import org.chat.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Roberta
 */
public class UserFacade {
    
    public static List<User> getAllUsers() {
        
        Session session = null;
        List<User> users = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            Query q = session.createQuery("FROM User");
            users = (List<User>) q.list();
            
            return users;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return users;
    }

    public synchronized static void addUser(User user) {
        
        Session session = null;
        
        try {
            
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            
            session.save(user);
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
