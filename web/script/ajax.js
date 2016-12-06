/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var Ajax = {
    
    postAjax: function(requestType, url, callback, parameterRequest, otherParameter){
        
        var ajax = new XMLHttpRequest();

        ajax.onreadystatechange = function(){
            
            if(ajax.readyState === 4){
                if(callback !== null){
                    callback(ajax.responseText, parameterRequest, otherParameter);
                }
            }
        };

        var parameterUrl = "code=" + requestType;
        for(var key in parameterRequest)
            parameterUrl = parameterUrl + "&" + key + "=" + parameterRequest[key]; 
        
        ajax.open('POST', url, true);
        ajax.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        ajax.send(parameterUrl); 
    },

    getAjax: function(requestType, url, callback, parameterRequest, otherParameter){
        var ajax = new XMLHttpRequest();

        ajax.onreadystatechange = function(){
            if(ajax.readyState === 4){
                if(callback !== null)
                    callback(ajax.responseText, parameterRequest, otherParameter);
            }    
        };

        var finalUrl = url + "?code=" + requestType;
        for(var key in parameterRequest)
            finalUrl = finalUrl + "&" + key + "=" + parameterRequest[key]; 

        ajax.open('get', finalUrl);
        ajax.send();
    }
};