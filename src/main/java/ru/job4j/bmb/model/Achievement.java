package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "award_id")
    private Award award;

    public Achievement(Long id, long createAt, User user, Award award) {
        this.id = id;
        this.createAt = createAt;
        this.user = user;
        this.award = award;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Achievement that = (Achievement) o;
        return createAt == that.createAt && Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(award, that.award);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Long.hashCode(createAt);
        result = 31 * result + (user == null ? 0 : user.hashCode());
        result = 31 * result + (award == null ? 0 : award.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Achievement{"
                + "id=" + id
                + ", createAt=" + createAt
                + ", user=" + user
                + ", award=" + award
                + '}';
    }
}
