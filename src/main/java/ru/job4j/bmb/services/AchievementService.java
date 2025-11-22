package ru.job4j.bmb.services;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {
    private final AchievementRepository achievementRepository;
    private final MoodLogRepository moodLogRepository;
    private final SentContent sentContent;

    public AchievementService(AchievementRepository achievementRepository,
                              MoodLogRepository moodLogRepository,
                              SentContent sentContent) {
        this.achievementRepository = achievementRepository;
        this.moodLogRepository = moodLogRepository;
        this.sentContent = sentContent;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
    }
}