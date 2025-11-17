package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MoodService {
    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        Mood mood = new Mood();
        mood.setId(moodId);
        MoodLog moodLog = new MoodLog();
        moodLog.setUser(user);
        moodLog.setMood(mood);
        moodLog.setCreatedAt(Instant.now().getEpochSecond());

        moodLogRepository.save(moodLog);

        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        Instant now = Instant.now();
        long weekAgo = now.minus(Duration.ofDays(7)).getEpochSecond();
        var logs = moodLogRepository.findAll().stream()
                .filter(user -> user.getUser().getId().equals(clientId))
                .filter(log -> log.getCreatedAt() >= weekAgo)
                .toList();
        var content = new Content(chatId);
        content.setText(formatMoodLogs(logs, "Лог настроений за неделю"));
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        Instant now = Instant.now();
        long monthAgo = now.minus(Duration.ofDays(30)).getEpochSecond();
        var logs = moodLogRepository.findAll().stream()
                .filter(user -> user.getUser().getId().equals(clientId))
                .filter(log -> log.getCreatedAt() >= monthAgo)
                .toList();
        var content = new Content(chatId);
        content.setText(formatMoodLogs(logs, "Лог настроений за месяц"));
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var achievementAwards = achievementRepository.findAll().stream()
                .filter(user -> user.getUser().getId().equals(clientId))
                .toList();
        var content = new Content(chatId);
        StringBuilder sb = new StringBuilder();
        for (Achievement a : achievementAwards) {
            sb.append(a.getAward().getDescription()).append("\n");
        }
        content.setText(sb.toString());
        return Optional.of(content);
    }
}