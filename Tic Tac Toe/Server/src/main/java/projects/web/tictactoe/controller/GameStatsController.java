package projects.web.tictactoe.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.web.tictactoe.dto.GameStatsDto;
import projects.web.tictactoe.entity.GameStats;
import projects.web.tictactoe.repository.AccountRepository;
import projects.web.tictactoe.repository.GameStatsRepository;

import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/game_stats")
public class GameStatsController {

      @Autowired private GameStatsRepository gameStatsRepository;
      @Autowired private AccountController accountController;
      @Autowired private AccountRepository accountRepository;

      @GetMapping("/{player1}/{player2}")
      public ResponseEntity<GameStatsDto>  getStats(@PathVariable String player1, @PathVariable String player2) {
            Optional<GameStats> gameStatsOptional = gameStatsRepository.findGameStats(player1, player2);
            if(gameStatsOptional.isPresent()) {
                  GameStats gameStats = gameStatsOptional.get();
                  return new ResponseEntity<>(
                          new GameStatsDto (
                                  gameStats.getPlayer1().getUsername(),
                                  gameStats.getPlayer2().getUsername(),
                                  gameStats.getWon(),
                                  gameStats.getLoss(),
                                  gameStats.getDraw()
                          ),
                          HttpStatus.FOUND
                  );
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      @Transactional
      @PostMapping("/{player1}/{player2}/{status}")
      public ResponseEntity<String> updateStats (
              @PathVariable String player1,
              @PathVariable String player2,
              @PathVariable String status
      ) {
            GameStats gameStats;
            Optional<GameStats> gameStatsOptional = gameStatsRepository.findGameStats(player1, player2);
            if(gameStatsOptional.isPresent()) gameStats = gameStatsOptional.get();
            else {
                  gameStats = new GameStats(
                          accountRepository.findByUsername(player1).orElse(null),
                          accountRepository.findByUsername(player2).orElse(null)
                  );
                  if(accountRepository.findByUsername(player2).isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                  else gameStatsRepository.save(gameStats);
            }

            if(!gameStats.getPlayer1().getUsername().equals(player1)) {
                  player1 = gameStats.getPlayer1().getUsername();
                  player2 = gameStats.getPlayer2().getUsername();

                  if(status.equals("WON")) status = "LOSS";
                  if(status.equals("LOSS")) status = "WON";
            }

            switch (status) {
                  case "WON" -> {
                        accountController.updateWon(player1);
                        accountController.updateLoss(player2);
                        gameStatsRepository.GameWon(player1, player2);
                  }
                  case "LOSS" -> {
                        accountController.updateWon(player2);
                        accountController.updateLoss(player1);
                        gameStatsRepository.GameLoss(player1, player2);
                  }
                  case "DRAW" -> {
                        accountController.updateDraw(player1);
                        accountController.updateDraw(player2);
                        gameStatsRepository.GameDraw(player1, player2);
                  }
            }

            return new ResponseEntity<>(HttpStatus.OK);
      }
}
