/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.utils;

/**
 *
 * @author Roberta
 */
public enum Error {
    
    //General
    EMPTY_USERNAME("Username field can not be left empty"),
    EMPTY_PASSWORD("Password field can not be left empty"),
    INVALID_USERNAME("Username invalid"),
    
    //Sign Up
    SHORT_USERNAME("Username must be at least 3 characters long"),
    SHORT_PASSWORD("Password must be at least 5 characters long"),
    SPACE_USERNAME("Username can not contain space"),
    CONFLICT_USERNAME("Username already taken. Please, choose another one."),
    UNMATCH_PASSWORD("Password does not match. Please retry."),
    
    //Log In
    UNFOUND_USERNAME("Username unfound. Please retry."),
    WRONG_PASSWORD("Incorrect password. Please retry.");
    
    private final String message;

    private Error(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
}
