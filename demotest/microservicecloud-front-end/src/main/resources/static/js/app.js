let stompClient = null;
$("#messageType").hide();
let recipientInput = $("#recipient");
let recipientText = $("#recipienttext");
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#sendMessage").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        $("#message").prop("disabled", false);
    } else {
        $("#conversation").hide();
        $("#message").prop("disabled", true);
    }
    $("#userinfo").html("");
}

function connect() {
    console.log("Connecting to WebSocket...");
    let socket = new SockJS('/websocket-example');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        
        $("#messageType").show();
        $("#messageType").val("public");


        // Subscribe to both public and private message topics
        stompClient.subscribe('/topic/user', function (response) {
            let message = JSON.parse(response.body);
            console.log('Received public message: ' + message.content);
            showGreeting(message.content, "公開頻道");
        });

        let username = getUsername();
        stompClient.subscribe('/topic/private/' + username, function (response) {
            let message = JSON.parse(response.body);
            console.log('Received private message: ' + message.content);
            showGreeting(message.content, "私人頻道");
        });
    }, function (error) {
        console.error('Error while connecting to WebSocket: ' + error);
        setConnected(false);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect(function () {
            setConnected(false);
            stompClient = null; // Reset stompClient after disconnecting
            $("#messageType").hide();
            recipientInput.hide();
            recipientText.hide();
        });
    }
    console.log("Disconnected");
}

function sendMessage() {
    let token = $("#jwtToken").val();
    if (!token) {
        alert("Please log in to access this page.");
        return;
    }

    console.log("Sending message...");
    let messageType = $("#messageType").val();
    let message = $("#message").val();
    console.log("Username: " + getUsername());
    console.log("Message Type: " + messageType);
    console.log("Message: " + message);

    if (messageType === "public") {
        stompClient.send("/app/user", {}, JSON.stringify({ 'username': getUsername(), 'message': message }));
    } else if (messageType === "private") {
        let recipient = $("#recipient").val();
        stompClient.send(
            "/app/private",
            {},
            JSON.stringify({ 'senderUsername': getUsername(), 'recipientUsername': recipient, 'message': getUsername() + " : " + message })
        );
    }

    // Clear the message input field after sending the message
    $("#message").val("");
}

function showGreeting(message, messageType) {
    let userinfoDiv = $("#userinfo");
    let conversationDiv = $("#conversation");
    let isScrolledToBottom = conversationDiv.scrollTop() + conversationDiv.innerHeight() >= conversationDiv[0].scrollHeight;

    userinfoDiv.append("<tr><td>" + messageType + "</td><td>" + message + "</td></tr>");

    // Scroll to the bottom of the conversation div if already at the bottom before adding a new message
    if (isScrolledToBottom) {
        conversationDiv.scrollTop(conversationDiv[0].scrollHeight);
    }
}

// Function to get the username from the input field
function getUsername() {
    return $("#username").val();
}

$(function () {
    recipientInput.hide();
    recipientText.hide();

    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#sendMessage").click(function () {
        sendMessage();
    });

    // Enable the Send Private Message button when recipient and private message are filled
    $("#recipient, #message").on("input", function () {
        let recipient = $("#recipient").val();
        let message = $("#message").val();
   
    });

    // Hide recipient input field based on messageType selection
    $("#messageType").change(function () {
        let messageType = $(this).val();
        if (messageType === "public") {
            recipientInput.hide();
            recipientText.hide();
            recipientInput.val(""); // Clear the recipient input field
        } else if (messageType === "private") {
            recipientInput.show();
            recipientText.show();
        }
    });

    // Trigger the change event to initialize the visibility of the recipient input field
    $("#messageType").trigger("change");

    // Trigger sendMessage when "Enter" key is pressed in the message input field
    $("#message").keydown(function (event) {
        if (event.keyCode === 13) {
            sendMessage();
        }
    });
});