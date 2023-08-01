            $(function () {

                var stompClient = null;
                var jwtToken = /*[[${token}]]*/ null;

                function setConnected(connected) {
                    $("#connect").prop("disabled", connected);
                    $("#disconnect").prop("disabled", !connected);
                    $("#sendMessage").prop("disabled", !connected);
                    if (connected) {
                        $("#conversation").show();
                    } else {
                        $("#conversation").hide();
                    }
                    $("#userinfo").html("");
                }

                function connect() {
                    console.log("Connecting to WebSocket...");
                    var socket = new SockJS('/websocket-example');
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function (frame) {
                        setConnected(true);
                        console.log("Connected: " + frame);
                        stompClient.subscribe('/user/topic/messages', function (message) {
                            showGreeting(JSON.parse(message.body).content);
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
                    console.log("Sending message...");
                    var messageType = $("#messageType").val();
                    var message = $("#message").val();
                    var token = "jwtToken"; // Replace this with your actual authorization token
                    var destination = messageType === "public" ? "/app/user" : "/app/private/" + $("#recipient").val();
                    stompClient.send(destination, {
                        Authorization: "Bearer " + token // Add the Authorization header
                    }, JSON.stringify({ 'message': message }));
                }

                function sendPrivateMessage() {
                    console.log("Sending private message...");
                    var recipient = $("#recipient").val();
                    var privateMessage = $("#message").val();
                    var token = "jwtToken"; // Replace this with your actual authorization token
                    stompClient.send("/app/private/" + recipient, {
                        Authorization: "Bearer " + token // Add the Authorization header
                    }, JSON.stringify({ 'message': privateMessage }));
                }

                function showGreeting(message) {
                    $("#userinfo").append("<tr><td>" + message + "</td></tr>");
                }

                $("#connect").click(function () { connect(); });
                $("#disconnect").click(function () { disconnect(); });
                $("#sendMessage").click(function () { sendMessage(); });
                $("#sendPrivateMessage").click(function () { sendPrivateMessage(); });
            });