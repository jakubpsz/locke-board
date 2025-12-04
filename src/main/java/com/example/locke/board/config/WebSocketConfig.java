package com.example.locke.board.config;

import com.example.locke.board.ws.DrawingWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DrawingWebSocketHandler drawingWebSocketHandler;

    public WebSocketConfig(DrawingWebSocketHandler drawingWebSocketHandler) {
        this.drawingWebSocketHandler = drawingWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(drawingWebSocketHandler, "/ws/draw")
                .setAllowedOriginPatterns("*"); // allow from LAN devices for development
    }
}
