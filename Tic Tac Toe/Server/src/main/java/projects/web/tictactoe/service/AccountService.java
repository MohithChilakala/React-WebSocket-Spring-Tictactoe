package projects.web.tictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projects.web.tictactoe.entity.Account;
import projects.web.tictactoe.repository.AccountRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

      @SuppressWarnings("unused")
      @Autowired private AccountRepository accountRepository;

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<Account> accountOptional = accountRepository.findByUsername(username);
            if(accountOptional.isPresent()) {
                  Account account = accountOptional.get();
                  return new User(account.getUsername(), account.getPassword(), new ArrayList<>());
            } else throw new UsernameNotFoundException(username + " not found");
      }
}
