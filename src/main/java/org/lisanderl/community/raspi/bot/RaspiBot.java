package org.lisanderl.community.raspi.bot;

import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.lisanderl.community.raspi.hardware.dht.DHTxSensor;
import org.lisanderl.community.raspi.hardware.mq.MQxAirQualitySensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@PropertySource("classpath:application.properties")
@Log4j2
public class RaspiBot extends TelegramLongPollingBot {
    private static final String BOT_USER_NAME = "raspiBot";
    private static final String ACTION_START = "/start";
    private static final String ACTION_TEMPERATURE = "/temperature";
    private static final String ACTION_AIR_QUALITY = "/air";
    private static final String ACTION_UNSUPPORTED = "Unsupported operation";
    private static final String GREETINGS_MESSAGE = ", Hi from " + BOT_USER_NAME + " \n"
            + "You can use a few actions -> " + ACTION_TEMPERATURE + " or " + ACTION_AIR_QUALITY;

    private final DHTxSensor temperatureSensor;
    private final MQxAirQualitySensor airQualitySensor;
    private final String botToken;

    @Autowired
    public RaspiBot(@Value("${raspi.bot.token}") String botToken, DHTxSensor temperatureSensor,
                    MQxAirQualitySensor airQualitySensor) {
        this.botToken = botToken;
        this.temperatureSensor = temperatureSensor;
        this.airQualitySensor = airQualitySensor;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info("Update received");
            if (update.getMessage().hasText() && update.getMessage().getText().contains("/")) {
                actionProcessing(update.getMessage());
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

    private void actionProcessing(Message message) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());

        switch (message.getText()) {
            case ACTION_START:
                sendMessage.setText(message.getChat().getUserName() + GREETINGS_MESSAGE);
                break;
            case ACTION_TEMPERATURE:
                sendMessage.setText(temperatureSensor.getLastCorrectMeasure().toString());
                break;
            case ACTION_AIR_QUALITY:
                sendMessage.setText(airQualitySensor.getAirQuality().toString());
                break;
            default:
                sendMessage.setText(ACTION_UNSUPPORTED);
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
