package org.lisanderl.community.raspi.bot;

import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.lisanderl.community.raspi.hardware.dht.DHTxSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@PropertySource("classpath:application.properties")
@Log4j2
public class RaspiBot extends TelegramLongPollingBot {
    private static final String BOT_USER_NAME = "raspiBot";
    private final DHTxSensor sensor;
    private final String botToken;

    @Autowired
    public RaspiBot(@Value("${raspi.bot.token}") String botToken, DHTxSensor sensor) {
        this.botToken = botToken;
        this.sensor = sensor;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info("Update received");
            if (update.getMessage().getText().contains("/") &&
                    update.getMessage().getText().equalsIgnoreCase(BotFeaturesEnum.TEMPERATURE.getFeatureName())) {
                var sensorData = sensor.getLastCorrectMeasure();
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("Sensor data: " + sensorData);

                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
