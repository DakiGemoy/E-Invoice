package co.id.Asset.eInvoice.Service;

import co.id.Asset.eInvoice.Database.Entity.Account;
import co.id.Asset.eInvoice.Database.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> test(String address){
        return accountRepository.findByAddressContaining(address);
    }

}
