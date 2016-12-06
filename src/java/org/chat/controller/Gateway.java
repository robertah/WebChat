/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.chat.model.MessageHandler;
import org.chat.model.UserConnection;

/**
 *
 * @author Roberta
 */
@WebServlet(name = "Gateway", urlPatterns = {"/Gateway"})
public class Gateway extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        String result = "";
        response.setContentType("text/html");
        
        switch (request.getParameter("code")) {
            case "LoadContacts": {
                result = UserConnection.getContacts(request);
                out.println(result);
                break;
            }
            case "GetMessages": {
                result = MessageHandler.getMessages(request);
                out.println(result);
                break;
            }
            case "GetNewMessages": {
                result = MessageHandler.getNewMessages(request);
                out.println(result);
                break;
            }
            default:
                System.out.println("Invalid Code");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        String result = "";
        response.setContentType("text/html");
        
        switch (request.getParameter("code")) {
            case "LogIn": {
                result = UserConnection.logIn(request);
                out.println(result);
                break;
            }
            case "SignUp": {
                result = UserConnection.signUp(request);
                out.println(result);
                break;
            }
            case "LogOut": {
                UserConnection.logOut(request);
                break;
            }
            case "Connection": {
                UserConnection.connect(request);
                break;
            }
            case "SendMessage": {
                MessageHandler.sendMessage(request);
                break;
            }
            case "DeleteMessage": {
                MessageHandler.deleteMessage(request);
                break;
            }
            default:
                System.out.println("Invalid Code");
        }
    }
    
}
