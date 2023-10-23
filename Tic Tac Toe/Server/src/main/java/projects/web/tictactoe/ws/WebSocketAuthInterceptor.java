package projects.web.tictactoe.ws;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import projects.web.tictactoe.exception.UnAuthorizedException;
import projects.web.tictactoe.model.Game;
import projects.web.tictactoe.service.GameService;

import java.util.Objects;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
      @Autowired private GameService gameService;

      @Override
      public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) throws UnAuthorizedException {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

            if(Objects.equals(headerAccessor.getCommand(), StompCommand.SUBSCRIBE)) {
                  String url = headerAccessor.getDestination();
                  String host = headerAccessor.getFirstNativeHeader("host");
                  String password = headerAccessor.getFirstNativeHeader("password");
                  String player = headerAccessor.getFirstNativeHeader("player");

                  if(url != null && url.startsWith("/game/") && url.length() == 9) {
                        String game_id = url.substring(6);
                        validate(game_id, host, password, player);
                  } else throw new UnAuthorizedException("Invalid path");
            }
            return ChannelInterceptor.super.preSend(message, channel);
      }

      private void validate(String game_id, String host, String password, String player) throws UnAuthorizedException {
            Game game = gameService.getGame(game_id);
            if(game == null) throw new UnAuthorizedException("Game not available");
            if(!game.getStatus().equals("CREATED")) throw new UnAuthorizedException("Game already started");
            if(!game.getHost().equals(host) || !game.getPassword().equals(password) ||
                    (!game.getPlayer1().equals(player) && !game.getPlayer2().equals(player) || player.isEmpty())
            ) throw new UnAuthorizedException("Invalid credentials");
      }
}
