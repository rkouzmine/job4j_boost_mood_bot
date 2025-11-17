package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;
    private final TgUI tgUI;
    private final MoodResponseService moodResponseService;

    private static final String START = "/start";

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository,
                           TgUI tgUI,
                           MoodResponseService moodResponseService) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
        this.tgUI = tgUI;
        this.moodResponseService = moodResponseService;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            long chatId = message.getChatId();
            String text = message.getText();
            Long clientId = message.getFrom().getId();

            if (START.equals(text)) {
                var user = new User();
                user.setClientId(clientId);
                user.setChatId(chatId);
                userRepository.save(user);

                SendMessage buttonsMessage = tgUI.sendButtons(chatId);
                buttonsMessage.setReplyMarkup(tgUI.buildButtons());
                send(buttonsMessage);
            }
        }
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}