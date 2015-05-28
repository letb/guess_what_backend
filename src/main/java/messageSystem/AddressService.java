package messageSystem;

import accountService.AccountService;
import accountService.AccountServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class AddressService {
    private Address frontendService;
    private Address accountService;
//    private Address gameMechanics;

    public void registerAccountService(AccountServiceImpl accountService) {
        this.accountService = accountService.getAddress();
    }
}
