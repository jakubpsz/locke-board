package com.example.locke.board.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles incoming text messages (JSON) from clients and broadcasts them to all connected clients.
 * Separation of concerns: this class manages sessions and broadcasting only.
 */
@Component
public class DrawingWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(DrawingWebSocketHandler.class);

    // Thread-safe set of sessions
    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        logger.info("WebSocket connected: {}", session.getRemoteAddress());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        logger.info("WebSocket disconnected: {}", session.getRemoteAddress());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Received a drawing event (JSON) from one client. Broadcast to others.
        String payload = message.getPayload();

        broadcast(payload);
    }

    /**
     * Broadcast a text payload to all connected sessions.
     * If an error occurs for a session, log and close it.
     */
    private void broadcast(String payload) {
        for (WebSocketSession s : sessions) {
            try {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(payload));
                }
            } catch (Exception e) {
                logger.warn("Error sending message to session {}: {}", s.getId(), e.getMessage());
                try {
                    s.close(CloseStatus.SERVER_ERROR);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }
}
