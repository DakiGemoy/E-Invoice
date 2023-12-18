package co.id.Asset.eInvoice.Configuration;

import co.id.Asset.eInvoice.Database.Entity.Account;
import co.id.Asset.eInvoice.Database.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findById(username)
                .orElseThrow(()-> new UsernameNotFoundException("Username not recognized"));
        return new DetailUser(account);
    }
}
