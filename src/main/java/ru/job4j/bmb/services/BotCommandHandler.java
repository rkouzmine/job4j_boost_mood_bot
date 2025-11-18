package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    private static final String START = "/start";
    private static final String WEEK_MOOD_LOG = "/week_mood_log";
    private static final String MONTH_MOOD_LOG = "/month_mood_log";
    private static final String AWARD = "/award";

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        Long chatId = message.getChatId();
        Long clientId = message.getFrom().getId();
        String text = message.getText();

        Optional<Content> result = Optional.empty();
        if (message.getText().equals(START)) {
            result = handleStartCommand(chatId, clientId);
        } else if (text.equals(WEEK_MOOD_LOG)) {
            result = moodService.weekMoodLogCommand(chatId, clientId);
        } else if (text.equals(MONTH_MOOD_LOG)) {
            result = moodService.monthMoodLogCommand(chatId, clientId);
        } else if (text.equals(AWARD)) {
            result = moodService.awards(chatId, clientId);
        }
        return result;
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findById(callback.getFrom().getId());
        return user.map(value -> moodService.chooseMood(value, moodId));
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}
