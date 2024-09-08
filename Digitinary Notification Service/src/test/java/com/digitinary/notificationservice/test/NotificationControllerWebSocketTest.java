package com.digitinary.notificationservice.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.digitinary.notificationservice.controller.NotificationController;

@SpringBootTest
public class NotificationControllerWebSocketTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendMessage() {
        String message = "Test Message";

        String response = notificationController.SendMessage(message);

        assertEquals(message, response);
    }
}
