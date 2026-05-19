package ru.job4j.tg;

public abstract class LongPoll {
    abstract void receive(String message);

    public final void sent(String message) {
        System.out.println(message);
    }
}