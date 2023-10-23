package projects.web.tictactoe.ws;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import projects.web.tictactoe.service.GameService;

import java.util.Map;

@Component
public class CustomHandShakeInterceptor extends HttpSessionHandshakeInterceptor {
      @Autowired private GameService gameService;

      @Override
      public boolean beforeHandshake(
              ServerHttpRequest request,
              @NonNull ServerHttpResponse response,
              @NonNull WebSocketHandler wsHandler,
              @NonNull Map<String, Object> attributes
      ) throws Exception {
            String query = request.getURI().getQuery();
            if(query != null && query.contains("game_id"))  {
                  String game = query.substring(query.indexOf("game_id=") + 8);
                  gameService.getGame(game);
                  if(gameService.getGame(game) == null) {
                        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        response.setStatusCode(HttpStatus.NOT_FOUND);

                        String jsonResponse = "{\"error\": \"Game not found\"}";
                        response.getBody().write(jsonResponse.getBytes());
                        return false;
                  }
            }
            return super.beforeHandshake(request, response, wsHandler, attributes);
      }
}