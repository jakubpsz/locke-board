# LockeBoard (Spring Boot + plain JS) - Minimal initial implementation

This project is a minimal, local-network-accessible whiteboard built with:
- Java 25
- Spring Boot 4.0.0
- Plain JavaScript for the front-end
- WebSocket for low-latency drawing sync between browsers

**What this initial version does**
- Serve a single-page web app (index.html) with an HTML5 canvas.
- Allow drawing with mouse, touch, and stylus (pointer events).
- Broadcast drawing events over WebSocket to all connected clients so drawings appear live.
- Uses black drawing color by default. No eraser, shapes, or authentication yet.

**Project structure**
- `pom.xml` - Maven project descriptor
- `src/main/java/...` - Java source: Spring Boot app, WebSocket configuration and handler
- `src/main/resources/static/index.html` - Frontend single page app
- `src/main/resources/application.properties` - Spring properties

## How it works (high level)
1. Browser opens `http://<server-ip>:8080/` and loads `index.html`.
2. Front-end opens a WebSocket connection to `/ws/draw`.
3. When user draws on the canvas, the client sends small JSON draw messages over the socket.
4. The server receives messages and broadcasts them to all connected clients.
5. Each client receives draw messages and renders them on their local canvas.

The implementation favors OOP and SRP: WebSocket handler only manages socket sessions and broadcasting;
configuration is separate; the front-end handles rendering and pointer event logic.

## Run locally and access from other devices on the same network

1. Make sure you have:
   - JDK 25 installed
   - Maven installed
   - Devices on the same LAN (same Wi-Fi or network)

2. Build and run:
   ```bash
   mvn -f /path/to/project/pom.xml clean package
   # or run directly with Spring Boot plugin:
   mvn -f /path/to/project spring-boot:run
   ```

3. Allow access from other devices:
   By default Spring binds to `0.0.0.0` in this project (see `application.properties`) so the app is reachable from other devices.
   Find your machine's local IP address (e.g. `192.168.1.23` on Linux/macOS with `ip addr` or `ipconfig` on Windows).

4. Open the whiteboard in other devices' browsers:
   ```
   http://<your-machine-ip>:8080/
   ```

Example:
```
http://192.168.1.23:8080/
```

## Notes / next steps
- This initial version does not persist whiteboards or users.
- Next steps: user accounts, permissions, persistent storage of boards (Postgres), invite links, undo/redo, shapes, eraser, color palette.
- For production, configure allowed origins, TLS (wss://), authentication, and rate-limiting.

## Troubleshooting
- If you cannot connect from another device, check firewall settings and ensure your machine's IP is reachable.
- Check application logs for WebSocket connection errors.

