package projects.web.tictactoe.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import projects.web.tictactoe.dto.GameMoveDto;
import projects.web.tictactoe.dto.GameResponseDto;
import projects.web.tictactoe.exception.IllegalGameplayException;
import projects.web.tictactoe.model.Game;
import projects.web.tictactoe.service.GameService;

import java.util.Objects;

@Controller
public class GamePlayHandler {
      @Autowired private SimpMessagingTemplate messagingTemplate;
      @Autowired private GameService gameService;

      @EventListener
      public void joinGame(SessionSubscribeEvent event) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            messagingTemplate.convertAndSend(
                    Objects.requireNonNull(headerAccessor.getDestination()),
                    headerAccessor.getFirstNativeHeader("player") + " joined"
            );
      }

      @MessageMapping("/start/{game_id}")
      public void startGame(@DestinationVariable String game_id) {
            Game game = gameService.RandomPlayerAssigner(game_id);
            game.setStatus("STARTED");
            messagingTemplate.convertAndSend(
                    "/game/" + game_id,
                    new GameResponseDto(game)
            );
      }

      @MessageMapping("/move")
      public void  makeMove(@Payload GameMoveDto gameMoveDto) {
            try {
                  messagingTemplate.convertAndSend(
                          "/game/" + gameMoveDto.getGame_id(),
                          gameService.makeMove(gameMoveDto)
                  );
            } catch (IllegalGameplayException e) {
                  messagingTemplate.convertAndSend(
                          "/game/" + gameMoveDto.getGame_id(),
                          e.getMessage()
                  );
            }
      }
}
