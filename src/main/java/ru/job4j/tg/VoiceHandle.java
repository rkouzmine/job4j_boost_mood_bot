package ru.job4j.tg;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class VoiceHandle {
    public CompletableFuture<Void> process(String message, Consumer<String> consumer) {
        return CompletableFuture.runAsync(() -> {
            IntStream.range(0, 5).forEach(x -> {
                try {
                    Thread.sleep(1000);
                    consumer.accept(String.format("Message: %s", x)
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}