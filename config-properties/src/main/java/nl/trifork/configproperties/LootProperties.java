package nl.trifork.configproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("loot")
public class LootProperties {

    private Boolean allowTransfer;
    private Game game;

    public Boolean getAllowTransfer() {
        return allowTransfer;
    }

    public void setAllowTransfer(Boolean allowTransfer) {
        this.allowTransfer = allowTransfer;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    enum Game {
        OVERWATCH,
        FORTNITE,
        FIFA
    }
}
