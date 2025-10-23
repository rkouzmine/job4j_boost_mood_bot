package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood_log")
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    private long createdAt;

    public MoodLog() {
    }

    public MoodLog(Long id, User user, Mood mood, long createdAt) {
        this.id = id;
        this.user = user;
        this.mood = mood;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoodLog moodLog = (MoodLog) o;
        return createdAt == moodLog.createdAt && Objects.equals(id, moodLog.id) && Objects.equals(user, moodLog.user) && Objects.equals(mood, moodLog.mood);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (user == null ? 0 : user.hashCode());
        result = 31 * result + (mood == null ? 0 : mood.hashCode());
        result = 31 * result + Long.hashCode(createdAt);
        return result;
    }

    @Override
    public String toString() {
        return "MoodLog{"
                + "id=" + id
                + ", user=" + user
                + ", mood=" + mood
                + ", createdAt=" + createdAt
                + '}';
    }
}

