package ru.job4j.bmb.services;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.events.UserEvent;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {
    private final AchievementRepository achievementRepository;
    private final MoodLogRepository moodLogRepository;
    private final AwardRepository awardRepository;
    private final SentContent sentContent;

    public AchievementService(AchievementRepository achievementRepository,
                              MoodLogRepository moodLogRepository,
                              AwardRepository awardRepository,
                              SentContent sentContent) {
        this.achievementRepository = achievementRepository;
        this.moodLogRepository = moodLogRepository;
        this.awardRepository = awardRepository;
        this.sentContent = sentContent;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
        int markedDays = moodLogRepository.findByUserId(user.getId()).size();

        Optional<Award> award = awardRepository.findAll().stream()
                .filter(a -> a.getDays() == markedDays)
                .findFirst();

        Content content = new Content(user.getChatId());

        if (award.isEmpty()) {
            content.setText("У Вас нет наград 😟");
        } else {
            Award aw = award.get();

            boolean alreadyExists = achievementRepository
                    .existsByUserIdAndAwardId(user.getId(), aw.getId());

            if (!alreadyExists) {
                Achievement achievement = new Achievement();
                achievement.setUser(user);
                achievement.setAward(aw);
                achievement.setCreateAt(Instant.now().getEpochSecond());

                achievementRepository.save(achievement);

                content.setText("🎉 Поздравляем! Вы получили награду: "
                        + aw.getDescription());
            } else {
                content.setText("У Вас уже есть эта награда 👍");
            }
        }

        sentContent.sent(content);
    }
}
