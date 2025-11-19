package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;
    private final UserRepository userRepository;
    private final TgUI tgUI;
    private final MoodResponseService moodResponseService;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler,
                              UserRepository userRepository,
                              TgUI tgUI,
                              MoodResponseService moodResponseService) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
        this.userRepository = userRepository;
        this.tgUI = tgUI;
        this.moodResponseService = moodResponseService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage())
                    .ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getAudio() != null) {
                SendAudio sendAudio = new SendAudio();
                sendAudio.setChatId(content.getChatId());
                sendAudio.setAudio(content.getAudio());
                if (content.getText() != null) {
                    sendAudio.setCaption(content.getText());
                }
                execute(sendAudio);
                return;
            }
            if (content.getPhoto() != null) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(content.getChatId());
                sendPhoto.setPhoto(content.getPhoto());
                if (content.getText() != null) {
                    sendPhoto.setCaption(content.getText());
                }
                execute(sendPhoto);
                return;
            }
            if (content.getText() != null && content.getMarkup() != null) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(content.getChatId());
                sendMessage.setText(content.getText());
                sendMessage.setReplyMarkup(content.getMarkup());
                execute(sendMessage);
                return;
            }
            if (content.getText() != null) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(content.getChatId());
                sendMessage.setText(content.getText());
                execute(sendMessage);
                return;
            }

        } catch (Exception e) {
            throw new SentContentException("Ошибка отправки контента", e);
        }
    }

    public class SentContentException extends RuntimeException {
        public SentContentException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}