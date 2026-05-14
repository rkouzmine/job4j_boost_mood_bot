package ru.job4j.bmb.conditional.mode;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.conditional.OnFakeCondition;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.SentContent;

@Component
@Conditional(OnFakeCondition.class)
public class FakeTelegramBot extends TelegramLongPollingBot implements SentContent {
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Fake-bot: " + update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return "Fake-bot";
    }

    @Override
    public void sent(Content content) {
        System.out.println("Fake sent: " + content.getText());
    }
}