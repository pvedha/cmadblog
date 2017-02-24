var Chat = {};
var chatControllerAngular;

jQuery(document).ready(function ($) {
    chatControllerAngular = angular.element($('#BlogChatController-Div')).scope();
});


Chat.socket = null;

Chat.connect = (function (host) {
    if ('WebSocket' in window) {
        Chat.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        Chat.socket = new MozWebSocket(host);
    } else {
        Console.log('Error: WebSocket is not supported by this browser.');
        return;
    }

    Chat.socket.onopen = function () {
        Console.log('Info: WebSocket connection opened.');
        document.getElementById('new-chat-message').onkeydown = function (event) {
            if (event.keyCode == 13) {
                Chat.sendMessage();
            }
        };
    };

    Chat.socket.onclose = function () {
        document.getElementById('chat').onkeydown = null;
        Console.log('Info: WebSocket closed.');
    };

    Chat.socket.onmessage = function (message) {
        Console.log(message.data);
    };
});

Chat.initialize = function () {
    if (window.location.protocol == 'http:') {
        Chat.connect('ws://' + window.location.host + '/examples/websocket/chat');
        //Chat.name="meow";
        //Chat.initialize.name="meow";
    } else {
        Chat.connect('wss://' + window.location.host + '/examples/websocket/chat');
    }
};

Chat.sendMessage = (function () {
    if (currentUserId == "") return;
    var message = document.getElementById('new-chat-message').value;
    if (message != '') {
        var msg = {
            type: "message",
            text: document.getElementById("new-chat-message").value,
            id: currentUserId,
            date: Date.now()
        };

        //Chat.socket.send(message);
        Chat.socket.send(JSON.stringify(msg));
        document.getElementById('new-chat-message').value = '';
    }
});

Chat.loginMessage = (function (message) {
    if (currentUserId == "") return;
    var msg = {
        type: "login",
        text: message,
        id: currentUserId,
        date: Date.now()
    };
    Chat.socket.send(JSON.stringify(msg));
    document.getElementById('new-chat-message').value = '';
});

var Console = {};

Console.log = (function (message) {
    var chatContent = document.getElementById('show-chat-div');
    var jsonMessage = true;
    var msg;
    var temp;
    try {
        temp = message.substring(message.indexOf("{"), message.indexOf("}") + 1);
        temp = temp.replace(/&quot;/g, '"');
        msg = JSON.parse(temp);
    } catch (err) {
        jsonMessage = false;
    }
    if (jsonMessage) {
        var timeStr = new Date(msg.date);
        //if(msg.type == "login"){
        //  chatControllerAngular.addChat("I just logged in",msg.id,getTime(timeStr));
        //} else {                    
        chatControllerAngular.addChat(msg.text, msg.id, getTime(timeStr));
        //}

    } else {
        if (message == "Info: WebSocket connection opened.") {
            $("#chat-connection").html("Connected to live chat...");
        } else {
            //console.log("Process this from chat.js : " + message);
        }
    }
    //            chatContent.appendChild(p);
    //            while (chatContent.childNodes.length > 25) {
    //                chatContent.removeChild(console.firstChild);
    //            }
    //chatControllerAngular.$apply(); //this not working in IE
    angular.element($('#BlogChatController-Div')).scope().$apply();
    //chatContent.scrollTop = console.scrollHeight;
    //console.log("show chat height" + $('#show-chat-div').height());
    $("#show-chat-div").animate({
        scrollTop: $('#show-chat-div').height() + 5000
    }, 100);
});

Chat.initialize();

function getTime(dateStr) {
    return dateStr.getHours() + ":" + dateStr.getMinutes() + ":" + dateStr.getSeconds();
}

document.addEventListener("DOMContentLoaded", function () {
    // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
    var noscripts = document.getElementsByClassName("noscript");
    for (var i = 0; i < noscripts.length; i++) {
        noscripts[i].parentNode.removeChild(noscripts[i]);
    }
}, false);
