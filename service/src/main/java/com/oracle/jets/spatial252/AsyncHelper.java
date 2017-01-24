package com.oracle.jets.spatial252;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class AsyncHelper {

    private Map<String, SseEmitter> pool = new ConcurrentHashMap<>();

    private static final long timeout = 1000 * 60 * 5;  //5 minutes

    SseEmitter getSseEmitter() {
        String key;
        do {
            key = RandomStringUtils.randomAlphanumeric(32);
        } while (pool.keySet().contains(key));
        SseEmitter emitter = new SseEmitter(timeout);
        emitter.onCompletion(new Remover(key));
        pool.put(key, emitter);
        return emitter;
    }

    @Async
    void sendToAllClients(Object object) {
        pool.values().stream().parallel().forEach(s -> {
            try {
                s.send(object);
            } catch (ClientAbortException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private class Remover implements Runnable {

        private final String key;

        Remover(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            pool.remove(key);
        }

    }

}