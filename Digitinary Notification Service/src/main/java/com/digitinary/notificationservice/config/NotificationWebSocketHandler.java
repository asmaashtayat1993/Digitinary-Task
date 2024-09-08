package com.digitinary.notificationservice.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {

	private Executor executor;
	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	@Autowired
	public NotificationWebSocketHandler(Executor executor, Map<String, WebSocketSession> sessions) {
		this.executor = executor;
		this.sessions = sessions;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.remove(session.getId());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}

	public void sendNotification(final String message) {
		log.info("send notification: {}", message);

		executor.execute(() -> {
			log.debug("notification send Starting ..");

			for (WebSocketSession session : sessions.values()) {
				if (session.isOpen()) {
					try {
						log.debug("Sending message to session {}: {}", session.getId(), message);
						session.sendMessage(new TextMessage(message));
					} catch (IOException e) {
						log.error("Failed to send message to session {}: {}", session.getId(), e.getMessage());
					}
				} else {
					log.error("Session {} is not open", session.getId());
				}
			}
			log.info("Notification send task completed");
		});
	}

    //for testing 
	public void setSessions(Map<String, WebSocketSession> sessions) {
		this.sessions.clear();
		this.sessions.putAll(sessions);
	}

	// for testing
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
}
