package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood_content")
public class MoodContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    private String text;

    public MoodContent(Long id, Mood mood, String text) {
        this.id = id;
        this.mood = mood;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoodContent that = (MoodContent) o;
        return Objects.equals(id, that.id) && Objects.equals(mood, that.mood) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (mood == null ? 0 : mood.hashCode());
        result = 31 * result + (text == null ? 0 : text.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "MoodContent{"
                + "id=" + id
                + ", mood=" + mood
                + ", text='" + text + '\''
                + '}';
    }
}

