    var stompClient = null;
	var jwtToken = /*[[${token}]]*/ null;
    function setConnected(connected) {
        $("#connect").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
        $("#send").prop("disabled", !connected);
        if (connected) {
            $("#conversation").show();
        } else {
            $("#conversation").hide();
        }
        $("#userinfo").html("");
    }

	function connect(callback) {
	    console.log("Connecting to WebSocket...");
	    var socket = new SockJS('/websocket-example/info?token=' + jwtToken);
	    stompClient = Stomp.over(socket);
	    stompClient.connect({}, function (frame) {
	        // Rest of the code
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

	function sendName() {
	    connect(function() {
	        console.log("Sending name...");
	        var name = $("#username").val();
	        var message = $("#message").val();
	        var token = "jwtToken"; // Replace this with your actual authorization token
	        stompClient.send("/app/user", 
	            {
	                Authorization: "Bearer " + token // Add the Authorization header
	            }, 
	            JSON.stringify({ 'username': name, 'message': message })
	        );
	    });
	}
	
	

    

    function showGreeting(message) {
		alert(123);
        $("#userinfo").append("<tr><td>" + message + "</td></tr>");
    }
    

    $(function () {
        $("#connect").click(function () { connect(); });
        $("#disconnect").click(function () { disconnect(); });
        $("#send").click(function () { sendName(); });
    });