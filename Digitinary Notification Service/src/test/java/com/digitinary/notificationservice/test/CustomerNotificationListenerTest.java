package com.digitinary.notificationservice.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.digitinary.notificationservice.config.NotificationWebSocketHandler;
import com.digitinary.notificationservice.enums.NotificationType;
import com.digitinary.notificationservice.event.CustomerNotificationEvent;
import com.digitinary.notificationservice.event.CustomerNotificationListener;
import com.digitinary.notificationservice.service.NotificationService;

class CustomerNotificationListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private Executor executor;

    @Mock
    private NotificationWebSocketHandler webSocketHandler;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private CustomerNotificationListener customerNotificationListener;

    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        latch = new CountDownLatch(1);
    }

    @Test
    void testReceiveCustomerCreatedNotification() throws InterruptedException {
        // Given
        String eventJson = "{\"type\":\"CUSTOMER_CREATED\",\"title\":\"New Customer\",\"body\":\"A new customer has been created\",\"createdAt\":\"2024-09-07T10:00:00\"}";
        CustomerNotificationEvent event = new CustomerNotificationEvent(
               NotificationType.CUSTOMER_CREATED, "New Customer", "A new customer has been created", 
                LocalDateTime.parse("2024-09-07T10:00:00")
        );

        doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            latch.countDown(); 
            return null;
        }).when(executor).execute(any(Runnable.class));

        customerNotificationListener.receiveCustomerCreatedNotification(eventJson);

        latch.await();

        verify(notificationService).saveNotification(event);
        verify(executor).execute(any(Runnable.class));
        verify(webSocketHandler).sendNotification(eventJson);
        verify(simpMessagingTemplate).convertAndSend("/topic/notifications", event);
    }
}