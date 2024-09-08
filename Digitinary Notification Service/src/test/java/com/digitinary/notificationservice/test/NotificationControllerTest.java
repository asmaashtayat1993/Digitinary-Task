package com.digitinary.notificationservice.test;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.digitinary.notificationservice.controller.NotificationController;
import com.digitinary.notificationservice.entity.Notification;
import com.digitinary.notificationservice.enums.NotificationType;
import com.digitinary.notificationservice.service.NotificationService;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    public void testGetNotificationByType() throws Exception {
        Notification notification = new Notification();
        notification.setType(NotificationType.CUSTOMER_CREATED);
        notification.setTitle("Test Notification");
        notification.setBody("This is a test notification");
        
        when(notificationService.getNotificationByType(NotificationType.CUSTOMER_CREATED))
                .thenReturn(Collections.singletonList(notification));

        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/type/CUSTOMER_CREATED")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Notification"));
    }
}
