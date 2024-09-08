package com.digitinary.notificationservice.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitinary.notificationservice.entity.Notification;
import com.digitinary.notificationservice.enums.NotificationType;
import com.digitinary.notificationservice.event.CustomerNotificationEvent;
import com.digitinary.notificationservice.repository.NotificationRepository;
import com.digitinary.notificationservice.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class NotificationServiceTest  {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessSaveNotification() {

    	    CustomerNotificationEvent event = new CustomerNotificationEvent(
    	        NotificationType.CUSTOMER_CREATED, "Test - Title", "Test - Body", LocalDateTime.now()
    	    );

    	    Notification notification = new Notification();
    	    notification.setNotificationId(6L);
    	    notification.setType(event.getType());
    	    notification.setTitle(event.getTitle());
    	    notification.setBody(event.getBody());
    	    notification.setCreatedAt(event.getCreatedAt());

    	    when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

    	    log.info("Executing testSaveNotificationSuccess with event: {}", event);

    	    Notification result = notificationService.saveNotification(event);
    	    log.info("Notification saved successfully: {}", result);

    	    assertEquals(notification, result);
    	    verify(notificationRepository, times(1)).save(any(Notification.class));
    }

  /*  @Test
    public void testFailureSaveNotification() {
        CustomerNotificationEvent event = new CustomerNotificationEvent(
         NotificationType.CUSTOMER_CREATED, "Test - Title", "Test -  Body", LocalDateTime.now()
        );

        when(notificationRepository.save(any(Notification.class))).thenThrow(DataAccessException.class);
        log.info("Executing testSaveNotificationSuccess with event: {}", event);

        NotificationSaveException exception = assertThrows(NotificationSaveException.class, () -> notificationService.saveNotification(event));
        log.error("error n occurred: {}", exception.getMessage());

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
*/
    @Test
    public void testSuccessGetNotificationByType() {
        Notification notification = new Notification();
     	notification.setNotificationId(1L); 
        notification.setType(NotificationType.CUSTOMER_CREATED);
        notification.setTitle("Test - Title");
        notification.setBody("Test - Body");
        notification.setCreatedAt(LocalDateTime.now());

        when(notificationRepository.findByType(NotificationType.CUSTOMER_CREATED)).thenReturn(Collections.singletonList(notification));
        log.info("Executing testGetNotificationByType for type: {}", NotificationType.CUSTOMER_CREATED);

        List<Notification> notifications = notificationService.getNotificationByType(NotificationType.CUSTOMER_CREATED);
        log.info("Notifications retrieved: {}", notifications);

        assertEquals(1, notifications.size());
        assertEquals(notification, notifications.get(0));
        verify(notificationRepository, times(1)).findByType(NotificationType.CUSTOMER_CREATED);
    }
}


