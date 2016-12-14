/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var timerContacts, timerMessages;
var sender, receiver;
var active_conversation = null;
var delete_msg_id = null;
var title = "Chat";
var notification_count = 0;

var Chat = {

    unLoad: window.onload = function () {

        var JSONparameters = {
            "username": sender
        };
        Ajax.postAjax("Connection", "/Chat/Gateway", null, JSONparameters, null);

    },

    beforeUnload: window.onbeforeunload = function () {

        var JSONparameters = {
            "username": sender
        };
        Ajax.postAjax("LogOut", "/Chat/Gateway", null, JSONparameters, null);

    },

    updateTitle: function () {
        if (notification_count > 0) {
            document.title = '(' + notification_count.trim() + ') ' + title;
        }
    },

    logOut: function () {

        var r = confirm("Do you want to log out?");

        if (r === true) {
            var from = document.getElementById('myUsername').getAttribute('datafld');
            var JSONparameters = {
                "username": from
            };

            //Cookie.deleteCookie('username');
            //Cookie.deleteCookie('password');

            Ajax.postAjax("LogOut", "/Chat/Gateway", Chat.callbackLogOut, JSONparameters, null);
        }
    },

    callbackLogOut: function (responseText, parameterRequest, otherParameter) {
        window.location = "/Chat/?#";
    },

    loadContacts: function () {
        sender = document.getElementById('myUsername').getAttribute('datafld');
        clearInterval(timerContacts);
        timerContacts = setInterval(Chat.loadContacts, 1500);

        var JSONparameters = {
            "username": sender
        };

        Ajax.getAjax("LoadContacts", "/Chat/Gateway", Chat.callbackLoadContacts, JSONparameters, null);
    },

    callbackLoadContacts: function (responseText, parameterRequest, otherParameter) {

        notification_count = 0;
        var parseJSON = JSON.parse(responseText);
        if (parseJSON.contacts !== "empty") {
            HTML.clear('people')
            for (var i = 0; i < parseJSON.contacts.length; i++) {
                HTML.createPerson(parseJSON.contacts[i].username, parseJSON.contacts[i].online, 
                                  parseJSON.contacts[i].text, parseJSON.contacts[i].time, parseJSON.contacts[i].unread);
                notification_count += parseJSON.contacts[i].unread;
            }
        }
        Chat.updateTitle();
        console.log("CALL BACK LOAD CONTACTS : " + active_conversation.className);
        if (active_conversation !== null)
            active_conversation.className = "person active";
    },

    sendMessage: function () {
        var input = document.getElementById('message_input');
        var text = input.value;

        if (text.trim() !== "") {
            var JSONparameters = {
                "text": text,
                "sender": sender,
                "receiver": receiver
            };

            Ajax.postAjax("SendMessage", "/Chat/Gateway", null, JSONparameters, null);
        }

        input.value = "";
    },

    keyPressed: function (event) {
        if (event.which === 13 || event.keyCode === 13) {
            Chat.sendMessage();
            return false;
        }
        return true;
    },

    getMessages: function (me, other) {

        var JSONparameters = {
            "me": me,
            "other": other
        };
        Ajax.getAjax("GetMessages", "/Chat/Gateway", Chat.callbackGetMessages, JSONparameters, null);

    },

    callbackGetMessages: function (responseText, parameterRequest, otherParameter) {
        var parseJSON = JSON.parse(responseText);

        if (parseJSON.messages !== "empty") {
            HTML.clear('chatwindow');
            for (let i = 0; i < parseJSON.messages.length; i++) {
                HTML.createMessage(parseJSON.messages[i].sender, parseJSON.messages[i].receiver, parseJSON.messages[i].text, parseJSON.messages[i].datetime, parseJSON.messages[i].id);
            }
        }
        /*
         var elem = document.getElementById('scroll');
         elem.scrollTop = elem.scrollHeight;
         */
    },

    getNewMessages: function (me, other) {

        var JSONparameters = {
            "me": me,
            "other": other
        };
        Ajax.getAjax("GetNewMessages", "/Chat/Gateway", Chat.callbackGetNewMessages, JSONparameters, null);
    },

    callbackGetNewMessages: function (responseText, parameterRequest, otherParameter) {
        var parseJSON = JSON.parse(responseText);
        if (parseJSON.messages !== "empty") {
            for (let i = 0; i < parseJSON.messages.length; i++) {
                HTML.createMessage(parseJSON.messages[i].sender, parseJSON.messages[i].receiver, 
                                   parseJSON.messages[i].text, parseJSON.messages[i].datetime, parseJSON.messages[i].id);
            }
        }
    },

    showConversation: function (username) {
        receiver = username;
        //update chat name span
        var span = document.getElementById('chatroom');
        span.textContent = username;

        console.log("HELLOOO");
        //activate person
        HTML.changeClassName("person active", "person");
        if (event !== null) {
            console.log("target id " + "person-id-" + username);
            console.log(active_conversation);
            active_conversation = document.getElementById("person-id-" + username);
            active_conversation.className = "person active";
            active_conversation.removeAttribute('data-badge');
        }

        //clear current chat messages
        HTML.clear('chatwindow');
        var chat = document.getElementById('chatwindow');
        chat.className = "chat active-chat";
        clearInterval(timerMessages);
        Chat.getMessages(sender, receiver);
        timerMessages = setInterval(Chat.getNewMessages, 1500, sender, receiver);
        clearInterval(timerContacts);
        timerContacts = setInterval(Chat.loadContacts, 1500);
    },

    showMenu: function (control, e) {
        var posx = e.clientX + window.pageXOffset + 'px'; //Left Position of Mouse Pointer
        var posy = e.clientY + window.pageYOffset + 'px'; //Top Position of Mouse Pointer
        document.getElementById(control).style.position = 'absolute';
        document.getElementById(control).style.display = 'inline';
        document.getElementById(control).style.left = posx;
        document.getElementById(control).style.top = posy;

        var id = e.target.id;
        delete_msg_id = id.substring(7, id.length); //html elem id is "msg_id-" + msg_id;
    },

    hideMenu: function (control) {

        document.getElementById(control).style.display = 'none';
    },

    deleteMessage: function () {
        Chat.hideMenu('deleteMenu');
        var r = confirm("Do you want to delete the message?");

        if (r === true && delete_msg_id !== null) {
            var me = document.getElementById('myUsername').getAttribute('datafld');
            console.log("id " + delete_msg_id);
            var JSONparameters = {
                "me": me,
                "message": delete_msg_id
            };

            Ajax.postAjax("DeleteMessage", "/Chat/Gateway", Chat.callbackDeleteMessage, JSONparameters, null);

        }

        delete_msg_id = null;
    },

    callbackDeleteMessage: function (responseText, parameterRequest, otherParameter) {
        Chat.getMessages(sender, receiver);
    }

}
