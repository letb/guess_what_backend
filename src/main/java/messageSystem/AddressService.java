package messageSystem;

import accountService.AccountService;
import accountService.AccountServiceImpl;
import frontend.GameWebSocket;
import mechanics.GameMechanics;
import mechanics.GameMechanicsImpl;
import webSocketService.WebSocketService;
import webSocketService.WebSocketServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class AddressService {
    private Address frontendService;
    private Address accountService;
    private Address gameMechanics;
    private Address webSocketService;
    private Address gameWebSocket;

    public void registerAccountService(AccountService accountService) {
        this.accountService = accountService.getAddress();
    }

    public void registerGameMechanics(GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }

    public void registerWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService.getAddress();
    }

    public void registerGameWebSocket(GameWebSocket gameWebSocket) {
        this.gameWebSocket = gameWebSocket.getAddress();
    }

    public Address getWebSocketService() {
        return webSocketService;
    }

    public Address getGameMechanics() {
        return gameMechanics;
    }
}
