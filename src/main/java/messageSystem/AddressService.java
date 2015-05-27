package messageSystem;

import accountService.AccountServiceImpl;
import mechanics.GameMechanicsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class AddressService {
    private Address frontendService;
    private Address accountService;
    private Address gameMechanics;

    public void registerAccountService(AccountServiceImpl accountService) {
        this.accountService = accountService.getAddress();
    }

    public void registerGameMechanics(GameMechanicsImpl gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }
}
