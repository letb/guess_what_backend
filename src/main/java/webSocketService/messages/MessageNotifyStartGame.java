package webSocketService.messages;

import messageSystem.Address;
import user.GameUser;
import webSocketService.WebSocketService;

public class MessageNotifyStartGame extends MessageToWebSocket {
    private GameUser user;
    private String keyword;

    public MessageNotifyStartGame(Address from, Address to, GameUser user, String keyword) {
        super(from, to);
        this.user = user;
        this.keyword= keyword;
    }

    @Override
    protected void exec(WebSocketService webSocketService) {
        webSocketService.notifyStartGame(user, keyword);
    }
}
