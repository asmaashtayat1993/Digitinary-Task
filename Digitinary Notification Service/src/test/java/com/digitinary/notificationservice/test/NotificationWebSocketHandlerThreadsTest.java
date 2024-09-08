package com.digitinary.notificationservice.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.digitinary.notificationservice.config.NotificationWebSocketHandler;
import com.digitinary.notificationservice.event.CustomerNotificationEvent;
import com.digitinary.notificationservice.event.CustomerNotificationListener;
import com.digitinary.notificationservice.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class NotificationWebSocketHandlerThreadsTest {
	@Mock
	private NotificationService notificationService; 

	@Mock
	private NotificationWebSocketHandler webSocketHandler; 
    
    private Executor executor;

	@Mock
	private SimpMessagingTemplate simpMessagingTemplate; 
	@InjectMocks
	private CustomerNotificationListener customerNotificationListener; 
	
	 @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	        executor = Executors.newFixedThreadPool(5);
	        customerNotificationListener.setExecutor(executor);
	    }

	    @Test
	    public void testSuccessNotificationHandledByMultipleThreads() throws InterruptedException {
	    	String event = "{\"type\":\"CUSTOMER_CREATED\",\"title\":\"Test Title\",\"body\":\"Test Body\",\"createdAt\":\"2024-09-09T12:00:00\"}";
	        CountDownLatch latch = new CountDownLatch(5);  // Expect 5 tasks
	        Set<String> threadNames = ConcurrentHashMap.newKeySet();

	        doAnswer(invocation -> {
	            String threadName = Thread.currentThread().getName();
	            log.info("Thread {} is starting", threadName);
	            threadNames.add(threadName);
	            Thread.sleep(1000); 
	            latch.countDown();
	            log.info("Thread {} finished. Latch count: {}", threadName, latch.getCount());	 
	            return null;
	        }).when(webSocketHandler).sendNotification(anyString());

	        
	        for (int i = 0; i < 5; i++) {
	            customerNotificationListener.receiveCustomerCreatedNotification(event);
	        }

	        // Assert
	        boolean completed = latch.await(60, TimeUnit.SECONDS);  
	        assertTrue(completed, "Not all notifications were handled by threads in parallel");

	        log.info("Unique thread names used: {}", threadNames);
	        assertEquals(5, threadNames.size(), "Expected 5 unique threads to handle notifications in parallel");

	        verify(webSocketHandler, times(5)).sendNotification(anyString());  
	        verify(notificationService, times(5)).saveNotification(any(CustomerNotificationEvent.class)); 
	        log.info("Verified sendNotification was called 5 times.");
	        log.info("Verified saveNotification  was called 5 times.");
	    }
	    
	 /*   @Test
	    public void testFaildNotificationHandledByMultipleThreads() throws InterruptedException {
	        String event = "{\"type\":\"CUSTOMER_CREATED\",\"title\":\"Test Title\",\"body\":\"Test Body\",\"createdAt\":\"2024-09-09T12:00:00\"}";
	        CountDownLatch latch = new CountDownLatch(5);
	        Set<String> threadNames = ConcurrentHashMap.newKeySet();

	        doAnswer((Answer<Void>) invocation -> {
	            String threadName = Thread.currentThread().getName();
	            log.info("Thread {} is starting", threadName);
	            threadNames.add(threadName);
	            Thread.sleep(1000); 
	            latch.countDown();
	            log.info("Thread {} finished. Latch count: {}", threadName, latch.getCount());
	            return null;
	        }).when(webSocketHandler).sendNotification(anyString());

	        // Simulate failure in NotificationService
	        doThrow(new RuntimeException("Simulated service failure")).when(notificationService).saveNotification(any(CustomerNotificationEvent.class));

	        try {
	            for (int i = 0; i < 5; i++) {
	                customerNotificationListener.receiveCustomerCreatedNotification(event);
	            }
	        } catch (RuntimeException e) {
	            log.error("Exception : {}", e.getMessage());
	        }

	        boolean completed = latch.await(60, TimeUnit.SECONDS);
	        log.error("Latch count after await: {}", latch.getCount());
	        assertTrue(latch.getCount() < 5, "Latch count should be less than 5 if NotificationService fails");

	        // Ensure that the thread names size is as expected even if service fails
	        assertTrue(threadNames.size() >= 5, "Expected at least 5 unique threads regardless of service failure");

	        // Verify interactions
	        verify(webSocketHandler, times(5)).sendNotification(anyString());
	        verify(notificationService, times(5)).saveNotification(any(CustomerNotificationEvent.class));
	    }*/
}

