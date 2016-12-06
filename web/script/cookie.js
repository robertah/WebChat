/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var Cookie = {

    checkCookies: function () {

        var previousUrl = document.referrer;
        if (previousUrl.indexOf("chat.jsp") !== -1) {
            var username = Cookie.getCookie("username");
            var password = Cookie.getCookie("password");

            if (username !== "" && password !== "") {
                Login.loginFromCookies(username, password);
            }

            /*
             var par = {"userCookie": user, "passwordCookie": password};
             postAjax("cookieLogin", "/Chat/Gateway", processLoginCookies, par, null);
             */
        }
    },

    setCookie: function (cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    },

    getCookie: function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    },

    deleteCookie: function (cname) {
        Cookie.setCookie(cname, '', 0);
    }


}
