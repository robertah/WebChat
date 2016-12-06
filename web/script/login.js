/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var Login = {

    addToggleHandler: function () {

        var toggle = document.querySelector('.toggle');
        toggle.addEventListener("click", function () {

            $(this).children('i').toggleClass('fa-user-plus');
            $('.form').animate({
                height: "toggle",
                'padding-top': 'toggle',
                'padding-bottom': 'toggle',
                opacity: "toggle"
            }, "slow");

        });
    },

    addLoginCookie: function (username, password) {
        if (Cookie.getCookie('username') === "" || Cookie.getCookie('password') === "") {
            Cookie.setCookie('username', username, 8);
            Cookie.setCookie('password', password, 8);
        }
    },

    login: function () {

        var JSONparam = {
            "username": document.getElementById('username').value,
            "password": document.getElementById('password').value
        };

        Ajax.postAjax("LogIn", "/Chat/Gateway", Login.callbackLogin, JSONparam, null);

    },

    callbackLogin: function (responseText, parameterRequest, otherParameter) {

        var parseJSON = JSON.parse(responseText);

        if (parseJSON.LogIn === "success") {
            
            Login.addLoginCookie(document.getElementById('username').value, document.getElementById('password').value);
            window.location = "/Chat/chat.jsp";
        } else {
            alert(parseJSON.LogIn);
        }
    },

    loginFromCookies: function (username, password) {

        var r = confirm("Do you want to continue as " + username + "?");

        if (r === true) {
            var JSONparam = {
                "username": username,
                "password": password
            };

            Ajax.postAjax("LogIn", "/Chat/Gateway", Login.callbackLogin, JSONparam, null);
        }
    },

    signup: function () {

        var JSONparam = {
            "username": document.getElementById('new_username').value,
            "password": document.getElementById('new_password').value,
            "repeat_password": document.getElementById('repeat_password').value
        };

        Ajax.postAjax("SignUp", "/Chat/Gateway", Login.callbackSignUp, JSONparam, null);
    },

    callbackSignUp: function (responseText, parameterRequest, otherParameter) {

        var parseJSON = JSON.parse(responseText);

        if (parseJSON.SignUp === "success") {
            alert("You have successfully signed up!");
            Login.addLoginCookie(document.getElementById('new_username').value, document.getElementById('new_password').value);
            window.location = "/Chat/chat.jsp";
        } else
            alert(parseJSON.SignUp);
    }

};


