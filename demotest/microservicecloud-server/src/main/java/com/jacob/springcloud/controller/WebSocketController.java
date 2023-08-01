package com.jacob.springcloud.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacob.springcloud.dto.PrivateMessage;
import com.jacob.springcloud.dto.UserMessage;
import com.jacob.springcloud.dto.WebSocketResponse;


@Controller
public class WebSocketController {
	
	
	
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 保留WebSocket部分
	@MessageMapping("/user")
	@SendTo("/topic/user")
	public WebSocketResponse getUser(UserMessage user, @RequestParam("token") String token) {
	    String message = user.getUsername() + " : " + user.getMessage();
	    return new WebSocketResponse(message);
	}
	


	private final ObjectMapper objectMapper = new ObjectMapper();

	@MessageMapping("/private")
	public void sendPrivateMessage(PrivateMessage privateMessage) {
	    String senderUsername = privateMessage.getSenderUsername();
	    String recipientUsername = privateMessage.getRecipientUsername();
	    String messageContent = privateMessage.getMessage();

	    // Create a JSON object containing the message content and sender username
	    Map<String, String> message = new HashMap<>();
	    message.put("senderUsername", senderUsername);
	    message.put("content", messageContent);
	    
	    System.out.println(message);
	    // Convert the message map to JSON
	    String jsonMessage;
	    try {
	        jsonMessage = objectMapper.writeValueAsString(message);
	        System.out.println("jsonMessage	"+jsonMessage);
	    } catch (JsonProcessingException e) {
	        System.err.println("Error converting message to JSON: " + e.getMessage());
	        return;
	    }

	    System.out.println("Private Message: From " + senderUsername + " to " + recipientUsername + ": " + messageContent);

	    // Send the private message to the recipient's WebSocket
	    String recipientTopic = "/topic/private/" + recipientUsername;
	    messagingTemplate.convertAndSend(recipientTopic, jsonMessage);

	    // Send the private message to the sender's WebSocket
	    String senderTopic = "/topic/private/" + senderUsername;
	    messagingTemplate.convertAndSend(senderTopic, jsonMessage);
	}



	
}
