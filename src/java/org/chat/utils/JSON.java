/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chat.utils;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Roberta
 */
public class JSON {
    
    public static String create(String key, String value) {

        JSONObject json = new JSONObject();
        json.put(key, value);
        return json.toJSONString();
        
    }

    public static String create(List<Object> key, List<Object> value) {

        JSONObject json = new JSONObject();

        if (key.size() == value.size()) {
            for (int i = 0; i < key.size(); i++) {
                json.put(key.get(i), value.get(i));
            }
        } else {
            return "Key size != Value size";
        }

        return json.toJSONString();
    }

    public static String create(List<Object> key, List<Object> value, List<Object> array) {

        JSONObject json = new JSONObject();
        JSONObject mainJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        int k = 0;
        if (key.size() == value.size()) {
            for (int i = 0; i < array.size(); i++) {
                for (int j = 0; j < key.size()/array.size(); j++) {
                    json.put(key.get(k), value.get(k));
                    k++;
                }
                jsonArray.add(json);
                mainJson.put(array.get(i), jsonArray);
                json = new JSONObject();
            }
        } else {
            return "Key size != Value size";
        }

        return mainJson.toJSONString();
    }
    
}
