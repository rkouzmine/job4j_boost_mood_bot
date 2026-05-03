package ru.job4j.bmb.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.bmb.content.FakeSentContent;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repository.*;
import ru.job4j.bmb.content.Content;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AchievementServiceTest {

    private MoodLogFakeRepository moodLogRepository;
    private AwardFakeRepository awardRepository;
    private AchievementFakeRepository achievementRepository;
    private FakeSentContent sentContent;

    private AchievementService service;
    private User user;

    @BeforeEach
    void setUp() {
        moodLogRepository = new MoodLogFakeRepository();
        awardRepository = new AwardFakeRepository();
        achievementRepository = new AchievementFakeRepository();
        sentContent = new FakeSentContent();

        service = new AchievementService(
                achievementRepository,
                moodLogRepository,
                awardRepository,
                sentContent
        );

        user = new User();
        user.setId(1L);
        user.setChatId(100L);
    }

    /**
     * Нет записей настроения - награда не выдаётся
     */
    @Test
    void whenNoMoodLogsThenNoAchievement() {
        awardRepository.save(new Award("3 days", "test", 3));
        service.onApplicationEvent(new UserEvent(this, user));

        assertThat(achievementRepository.findAll()).isEmpty();

        assertThat(sentContent.getSentMessages())
                .hasSize(1)
                .first()
                .extracting(Content::getText)
                .isEqualTo("У Вас нет наград 😟");
    }

    /**
     * Есть 3 дня активности - выдаётся награда
     */
    @Test
    void when3MoodLogsAndAwardExistsThenCreateAchievement() {
        awardRepository.save(new Award("3 days", "test", 3));
        for (int i = 0; i < 3; i++) {
            MoodLog log = new MoodLog();
            log.setUser(user);
            log.setCreatedAt(Instant.now().minusSeconds(i * 86400).toEpochMilli());
            moodLogRepository.save(log);
        }

        service.onApplicationEvent(new UserEvent(this, user));

        assertThat(achievementRepository.findAll()).hasSize(1);

        Achievement saved = achievementRepository.findAll().get(0);
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getAward().getDays()).isEqualTo(3);

        assertThat(sentContent.getSentMessages())
                .hasSize(1)
                .first()
                .extracting(Content::getText)
                .asString()
                .contains("Поздравляем");
    }

    /**
     * Нет подходящей награды - ничего не создается
     */
    @Test
    void whenNoMatchingAwardThenNoAchievementCreated() {
        awardRepository.save(new Award("10 days", "test", 10));

        for (int i = 0; i < 3; i++) {
            MoodLog log = new MoodLog();
            log.setUser(user);
            moodLogRepository.save(log);
        }

        service.onApplicationEvent(new UserEvent(this, user));

        assertThat(achievementRepository.findAll()).isEmpty();

        assertThat(sentContent.getSentMessages())
                .hasSize(1)
                .first()
                .extracting(Content::getText)
                .isEqualTo("У Вас нет наград 😟");
    }

    /**
     * Повторное событие не создаёт дубликат награды
     */
    @Test
    void whenEventTriggeredTwiceThenOnlyOneAchievementCreated() {
        awardRepository.save(new Award("3 days", "test", 3));

        for (int i = 0; i < 3; i++) {
            MoodLog log = new MoodLog();
            log.setUser(user);
            moodLogRepository.save(log);
        }

        service.onApplicationEvent(new UserEvent(this, user));
        service.onApplicationEvent(new UserEvent(this, user));

        assertThat(achievementRepository.findAll()).hasSize(1);
    }
}