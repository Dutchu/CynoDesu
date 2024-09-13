package edu.weeia.cynodesu.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SseController {

    // A thread-safe list to store all active SSE emitters
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Endpoint to establish an SSE connection
    @GetMapping(value = "/sse/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getNotifications() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Keep connection open
        emitters.add(emitter);

        // Remove the emitter when the connection is completed
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    // Method to send notifications to all connected clients
    public void sendNotification(String eventName, Object data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                emitters.remove(emitter); // Remove emitter if sending fails
            }
        }
    }
}