package ru.job4j.bmb.conditional.mode;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.job4j.bmb.conditional.OnRealCondition;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.SentContent;

@Component
@Conditional(OnRealCondition.class)
public class RealTelegramBot extends TelegramLongPollingBot implements SentContent {

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Real-bot: " + update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return "Real-bot";
    }

    @Override
    public void sent(Content content) {
        System.out.println("Real sent: " + content.getText());
    }
}