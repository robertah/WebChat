/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var HTML = {

    isMe: function (username) {
        if (username !== document.getElementById('myUsername').getAttribute('datafld'))
            return false;
        return true;
    },

    createPerson: function (username, online, msg_text, msg_time, msg_unread) {
        var parent = document.getElementById('people');

        if (HTML.isMe(username))
            return;

        var li = document.createElement('li');
        li.className = 'person';
        li.id = "person-id-" + username;
        if (msg_unread > 0) {
            if (msg_unread <= 99)
                li.setAttribute('data-badge', msg_unread);
            else
                li.setAttribute('data-badge', '99+');
        }

        var avatar = document.createElement('span');
        avatar.className = 'avatar';
        if (username === "GROUP") {
            avatar.innerHTML = "<i class='fa fa-users' aria-hidden='true'></i>";
        } else {
            avatar.innerHTML = username.toUpperCase().charAt(0);
            avatar.style.backgroundColor = HTML.getColor(username.toUpperCase().charAt(0));
            if (online) {
                avatar.style.border = "1px solid #00f218";
            } else {
                avatar.style.border = "1px solid #ff0c0c";
            }
        }

        var name = document.createElement('span');
        name.className = 'name';
        if (username === "GROUP") {
            name.innerHTML = "Group Chat";
        } else {
            name.innerHTML = username;
        }

        var time = document.createElement('span');
        time.className = 'time';
        time.innerHTML = msg_time;


        var preview = document.createElement('span');
        preview.className = 'preview';
        if (msg_text !== " ") {
            preview.innerHTML = HTML.escape(msg_text);
        } else {
            preview.innerHTML = "Start a new conversation!";
        }


        li.appendChild(avatar);
        li.appendChild(name);
        li.appendChild(time);
        li.appendChild(preview);

        parent.appendChild(li);

        li.onclick = function () {
            Chat.showConversation(username);
            console.log("ho cliccatooooo " + li.id + li.className);
        };

    },

    changeClassName: function (oldClass, newClass) {
        var elem = document.getElementsByClassName(oldClass);
        console.log(elem);
        if (elem !== null && elem.length > 0) {
            for (let i = 0; i < elem.length; i++) {
                elem[i].className = newClass;
            }
        }
    },

    updateChat: function (username) {
        var name = document.getElementById('chatroom');
        name.innerHTML = username;
    },

    createMessage: function (sender, receiver, msg_text, msg_time, msg_id) {

        var parent = document.getElementById('chatwindow');

        var div = document.createElement('div');
        if (HTML.isMe(sender)) {
            div.className = 'bubble me';
        } else {
            div.className = 'bubble you';
        }
        div.id = "msg_id-" + msg_id;

        if (receiver !== "GROUP") {
            div.setAttribute('oncontextmenu', "Chat.showMenu('deleteMenu', event)");
        }

        var message = document.createElement('span');
        message.className = 'message';
        message.innerHTML = HTML.escape(msg_text);

        var time = document.createElement('span');
        time.className = 'time';
        time.innerHTML = msg_time;

        var id = document.createElement('span');
        id.className = 'msg_id';
        id.innerHTML = msg_id;

        div.appendChild(message);
        div.appendChild(time);
        div.appendChild(id);

        parent.appendChild(div);
    },

    clear: function (node) {
        var el = document.getElementById(node);
        while (el.hasChildNodes())
            el.removeChild(el.lastChild);
    },

    escape: function (text) {
        return text
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
    },

    getColor: function (char) {
        return Color[char];
    }

};

var Color = {
    A: '#CD6155',
    B: '#EC7063',
    C: '#AF7AC5',
    D: '#A569BD',
    E: '#5499C7',
    F: '#5DADE2',
    G: '#48C9B0',
    H: '#45B39D',
    I: '#52BE80',
    J: '#F4D03F',
    K: '#F5B041',
    L: '#EB984E',
    M: '#922B21',
    N: '#B03A2E',
    O: '#76448A',
    P: '#6C3483',
    Q: '#1F618D',
    R: '#2874A6',
    S: '#148F77',
    T: '#117A65',
    U: '#1E8449',
    V: '#239B56',
    W: '#B7950B',
    X: '#B9770E',
    Y: '#AF601A',
    Z: '#A04000'
};