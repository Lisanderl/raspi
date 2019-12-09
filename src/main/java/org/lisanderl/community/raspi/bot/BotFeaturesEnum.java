package org.lisanderl.community.raspi.bot;

import lombok.Getter;

@Getter
public enum BotFeaturesEnum {
    START("/start", "subscribe "),
    TEMPERATURE("/temperature", "read temperature and humidity"),
    ;

    BotFeaturesEnum(String featureName, String featureDescription) {
        this.featureName = featureName;
        this.featureDescription = featureDescription;
    }

    private String featureName;
    private String featureDescription;
}
