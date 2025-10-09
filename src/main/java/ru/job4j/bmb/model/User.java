package ru.job4j.bmb.model;

import java.util.Objects;

public class User {
    private Long id;
    private long clientId;
    private long chatId;

    public User(Long id, long clientId, long chatId) {
        this.id = id;
        this.clientId = clientId;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return clientId == user.clientId && chatId == user.chatId && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.hashCode());
        result = 31 * result + Long.hashCode(clientId);
        result = 31 * result + Long.hashCode(chatId);
        return result;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", clientId=" + clientId
                + ", chatId=" + chatId
                + '}';
    }
}
