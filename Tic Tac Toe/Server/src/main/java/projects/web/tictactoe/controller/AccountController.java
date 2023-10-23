package projects.web.tictactoe.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import projects.web.tictactoe.dto.ProfileDto;
import projects.web.tictactoe.dto.CredentialsDto;
import projects.web.tictactoe.entity.Account;
import projects.web.tictactoe.repository.AccountRepository;
import projects.web.tictactoe.service.AccountService;

import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/account")
public class AccountController {
      @Autowired private PasswordEncoder passwordEncoder;
      @Autowired private AccountRepository accountRepository;
      @Autowired private AccountService accountService;

      @Transactional
      @PostMapping
      public ResponseEntity<String> createUser(@RequestBody CredentialsDto credentialsDto) {
            Account account = new Account(
                    credentialsDto.getUsername(),
                    passwordEncoder.encode(credentialsDto.getPassword())
            );
            accountRepository.save(account);
            return new ResponseEntity<>("Created", HttpStatus.CREATED);
      }

      @GetMapping("/login")
      public ResponseEntity<String> checkLogin() {
            return new ResponseEntity<>(HttpStatus.OK);
      }

      @GetMapping("/{username}")
      public ResponseEntity<ProfileDto> getUser(@PathVariable String username) {
            Optional<Account> accountOptional = accountRepository.findByUsername(username);
            if(accountOptional.isPresent()) {
                  Account account = accountOptional.get();
                  ProfileDto profileDto = new ProfileDto(
                          account.getUsername(),
                          account.getWon(),
                          account.getLoss(),
                          account.getDraw()
                  );
                  return new ResponseEntity<>(profileDto, HttpStatus.FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      @Transactional
      @PatchMapping("/update-won/{username}")
      public void updateWon(@PathVariable String username) {
            accountRepository.updateWon(username);
      }

      @Transactional
      @PatchMapping("/update-loss/{username}")
      public void updateLoss(@PathVariable String username) {
            accountRepository.updateLoss(username);
      }

      @Transactional
      @PatchMapping("/update-draw/{username}")
      public void updateDraw(@PathVariable String username) {
            accountRepository.updateDraw(username);
      }
}
