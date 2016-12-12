<%-- 
    Document   : chat
    Created on : 29-nov-2016, 10.45.48
    Author     : Roberta
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Chat</title>
        <link rel="icon" type="image/x-icon" href="image/favicon.ico" />
        <link rel="stylesheet" href="style/body.css">
        <link rel="stylesheet" href="style/chat.css">
        <script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.1.1.min.js"></script>
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    </head>

    <body>
        <div id="myUsername" datafld="${sessionScope.username}"></div>
        <div class="wrapper">    
            <div class="container" oncontextmenu="return false" onmousedown="Chat.hideMenu('deleteMenu')" onmouseup="Chat.hideMenu('deleteMenu')">        
                <div class="left">                   
                    <ul id="people" class="people"> 

                        <!-- 
                        <li class="person" data-chat="person1" data-badge="6">            
                            
                            <span class="avatar">T</span>
                            <span class="name">Thomas Bangalter</span>                
                            <span class="time">2:09 PM</span>                
                            <span class="preview">I was wondering...</span>  



                        </li>                
                        <li class="person" data-chat="person2">    
                            <span class="avatar">D</span>
                            <span class="name">Dog Woofson</span>                 
                            <span class="time">1:44 PM</span>                
                            <span class="preview">I've forgotten how it felt before</span>     
                        </li> 
                        -->
                    </ul>       
                </div>     
                <div class="right">   
                    <div class="top">
                        <span>To: 
                            <span id="chatroom" class="name"></span>
                        </span>
                    </div>        
                    <ul id="chatwindow" class="chat">               
                        
                       <!--
                        <div class="bubble you">  
                            <span class="message">Hello,</span>
                            <span class="time">5:30</span>
                        </div>            
                        <div class="bubble you">  
                            it's me.           
                        </div>            
                        <div class="bubble you">    
                            I was wondering...         
                        </div>           
                       -->
                    </ul>         
                    <!--
                    <div class="chat" data-chat="person2">      
                        <div class="conversation-start">     
                            <span>Today, 5:38 PM</span> 
                        </div>          
                        <div class="bubble you">      
                            Hello, can you hear me?     
                        </div>          
                        <div class="bubble you">    
                            I'm in California dreaming   
                        </div>        
                        <div class="bubble me">  
                            ... about who we used to be.    
                        </div>        
                        <div class="bubble me">   
                            Are you serious?  
                        </div>       
                        <div class="bubble you">   
                            When we were younger and free...    
                        </div>          
                        <div class="bubble you">   
                            I've forgotten how it felt before 
                        </div> 
                        <div class="conversation-start">     
                            <span>Today, 5:38 PM</span> 
                        </div>          
                        <div class="bubble you">      
                            Hello, can you hear me?     
                        </div>          
                        <div class="bubble you">    
                            I'm in California dreaming   
                        </div>        
                        <div class="bubble me">  
                            ... about who we used to be.    
                        </div>        
                        <div class="bubble me">   
                            Are you serious?  
                        </div>       
                        <div class="bubble you">   
                            When we were younger and free...    
                        </div>          
                        <div class="bubble you" oncontextmenu="Chat.showMenu('deleteMenu', event, event.target.id)">   
                            I've forgotten how it felt before 
                        </div>          
                    </div>    
                    -->

                    <div class="write">      
                        <input id="message_input" type="text" placeholder="Type a message here..." onkeypress="Chat.keyPressed(event)"/>
                        <a class="write-link send" title="Send" onclick="Chat.sendMessage()">
                            <i class="fa fa-paper-plane-o fa-lg" aria-hidden="true"></i>
                        </a>

                    </div>     
                </div>   
                        
                        <button class="signout" title="Log Out" onclick="Chat.logOut()">
                            <i class="fa fa-sign-out fa-2x" aria-hidden="true"></i>
                        </button>

                    </div>
                </div>
                <div id="deleteMenu" class="contextMenu">
                    <table>
                        <tr>
                            <td >
                                <div class="ContextItem" onclick="Chat.deleteMessage()">Delete message</div>
                            </td>
                        </tr>
                    </table>
                </div>
                </body>

                <script src='script/ajax.js'></script>
                <script src="script/chat.js"></script>
                <script src='script/cookie.js'></script>
                <script src='script/html.js'></script>
                <script>
                    /*
                                $('.chat[data-chat=person2]').addClass('active-chat');
                                $('.person[data-chat=person2]').addClass('active');
                                $('.left .person').mousedown(function () {
                                    if ($(this).hasClass('.active')) {
                                        return false;
                                    } else {
                                        var findChat = $(this).attr('data-chat');
                                        var personName = $(this).find('.name').text();
                                        $('.right .top .name').html(personName);
                                        $('.chat').removeClass('active-chat');
                                        $('.left .person').removeClass('active');
                                        $(this).addClass('active');
                                        $('.chat[data-chat = ' + findChat + ']').addClass('active-chat');
                                    }
                                });
                                */
                                Chat.loadContacts();
                </script>
                </html>

