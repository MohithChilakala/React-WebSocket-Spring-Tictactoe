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
      public static final String GAME = "/game/";
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
      public void startGame(@DestinationVariable String gameId) {
            Game game = gameService.RandomPlayerAssigner(gameId);
            game.setStatus("STARTED");
            messagingTemplate.convertAndSend(
                    "/game/%s".formatted(gameId),
                    new GameResponseDto(game)
            );
      }

      @MessageMapping("/move")
      public void  makeMove(@Payload GameMoveDto gameMoveDto) {
            try {
                  messagingTemplate.convertAndSend(
                          GAME + gameMoveDto.getGameId(),
                          gameService.makeMove(gameMoveDto)
                  );
            } catch (IllegalGameplayException e) {
                  messagingTemplate.convertAndSend(
                          GAME + gameMoveDto.getGameId(),
                          e.getMessage()
                  );
            }
      }
}
