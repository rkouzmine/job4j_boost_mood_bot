package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood")
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private boolean good;

    public Mood() {
    }

    public Mood(String text, boolean good) {
        this.text = text;
        this.good = good;
    }

    public Mood(Long id, String text, boolean good) {
        this.id = id;
        this.text = text;
        this.good = good;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mood mood = (Mood) o;
        return good == mood.good && Objects.equals(id, mood.id) && Objects.equals(text, mood.text);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.hashCode());
        result = 31 * result + (text == null ? 0 : text.hashCode());
        result = 31 * result + Boolean.hashCode(good);
        return result;
    }

    @Override
    public String toString() {
        return "Mood{"
                + "id=" + id
                + ", text='" + text + '\''
                + ", good=" + good
                + '}';
    }
}