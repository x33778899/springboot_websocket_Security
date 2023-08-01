    var stompClient = null;

    function setConnected(connected) {
        $("#connect").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
        $("#sendMessage").prop("disabled", !connected);
        $("#sendPrivateMessage").prop("disabled", !connected);
        if (connected) {
            $("#conversation").show();
            $("#message").prop("disabled", false);
            $("#privateMessage").prop("disabled", false);
        } else {
            $("#conversation").hide();
            $("#message").prop("disabled", true);
            $("#privateMessage").prop("disabled", true);
        }
        $("#userinfo").html("");
    }

    function connect() {
        console.log("Connecting to WebSocket...");
        var socket = new SockJS('/websocket-example');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);

            // Subscribe to both public and private message topics
            stompClient.subscribe('/topic/user', function (response) {
                var message = JSON.parse(response.body);
                console.log('Received public message: ' + message.content);
                showGreeting(message.content, "Public");
            });

            var username = getUsername();
            stompClient.subscribe('/topic/private/' + username, function (response) {
                var message = JSON.parse(response.body);
                console.log('Received private message: ' + message.content);
                showGreeting(message.content, "Private");
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
            });
        }
        console.log("Disconnected");
    }

    function sendMessage() {
        var token = $("#jwtToken").val();
        if (!token) {
            alert("Please log in to access this page.");
            return;
        }

        console.log("Sending message...");
        var message = $("#message").val();
        console.log("Username: " + getUsername());
        console.log("Message: " + message);
        stompClient.send("/app/user", {}, JSON.stringify({ 'username': getUsername(), 'message': message }));
        // Clear the message input field after sending the message
        $("#message").val("");
    }

    function sendPrivateMessage() {
        var token = $("#jwtToken").val();
        if (!token) {
            alert("Please log in to access this page.");
            return;
        }

        var recipient = $("#recipient").val();
        var privateMessage = $("#privateMessage").val();

        stompClient.send(
            "/app/private",
            {},
            JSON.stringify({ 'senderUsername': getUsername(), 'recipientUsername': recipient, 'message': getUsername()+" : "+privateMessage })
        );

        // Clear the private message input field after sending the message
        $("#privateMessage").val("");
    }

	function showGreeting(message, messageType) {

      $("#userinfo").append("<tr><td>" + messageType + "</td><td>" + message + "</td></tr>");


	}

    // Function to get the username from the input field
    function getUsername() {
        return $("#username").val();
    }

    $(function () {
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
        $("#recipient, #privateMessage").on("input", function () {
            var recipient = $("#recipient").val();
            var privateMessage = $("#privateMessage").val();
            $("#sendPrivateMessage").prop("disabled", recipient.length === 0 || privateMessage.length === 0);
        });

        // Handle Send Private Message button click
        $("#sendPrivateMessage").click(function () {
            sendPrivateMessage();
        });
    });