package projects.web.tictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.web.tictactoe.dto.CreateRoomDto;
import projects.web.tictactoe.dto.GameJoinDto;
import projects.web.tictactoe.model.Game;
import projects.web.tictactoe.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {
      @Autowired private GameService gameService;

      @PostMapping("/create-room")
      public ResponseEntity<String> createRoom(@RequestBody CreateRoomDto createRoomDto) {
            return new ResponseEntity<>(
                    gameService.createRoom(
                            createRoomDto.getHost(),
                            createRoomDto.getPassword()
                    ),
                    HttpStatus.CREATED
            );
      }

      @PostMapping("/join")
      public ResponseEntity<String> joinGame(@RequestBody GameJoinDto gameJoinDto) {
            Game game = gameService.getGame(gameJoinDto.getGameId());
            if(game == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else {
                  if(!game.getPassword().equals(gameJoinDto.getPassword())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                  else {
                        game.setLastUpdatedInMilli(System.currentTimeMillis() + 2* 60 * 1000);
                        if(game.getPlayer1().isEmpty()) game.setPlayer1(gameJoinDto.getPlayer());
                        else {
                              if(game.getPlayer1().equals(gameJoinDto.getPlayer())) return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
                              else if(game.getPlayer2().isEmpty()) game.setPlayer2(gameJoinDto.getPlayer());
                              else if(game.getPlayer2().equals(gameJoinDto.getPlayer())) return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
                              else return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                        }
                  }
            }
            return new ResponseEntity<>(HttpStatus.OK);
      }
}
