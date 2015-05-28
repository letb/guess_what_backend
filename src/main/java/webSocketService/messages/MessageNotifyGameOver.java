package webSocketService.messages;

import messageSystem.Address;
import user.GameUser;
import webSocketService.WebSocketService;

public class MessageNotifyGameOver extends MessageToWebSocket {
    private GameUser user;
    private String keyword;
    private boolean win;

    public MessageNotifyGameOver(Address from, Address to, GameUser user, boolean win, String keyword) {
        super(from, to);
        this.user = user;
        this.win = win;
        this.keyword= keyword;
    }

    @Override
    protected void exec(WebSocketService webSocketService) {
        webSocketService.notifyGameOver(user, win, keyword);
    }
}