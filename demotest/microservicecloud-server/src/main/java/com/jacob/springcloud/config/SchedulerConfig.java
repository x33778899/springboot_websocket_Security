package com.jacob.springcloud.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.jacob.springcloud.dto.UserResponse;
import com.jacob.springcloud.dto.WebSocketResponse;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Autowired
    SimpMessagingTemplate template;


    public void sendAdhocMessages() {
        template.convertAndSend("/topic/user", new WebSocketResponse("Fixed Delay Scheduler"));
    }
}