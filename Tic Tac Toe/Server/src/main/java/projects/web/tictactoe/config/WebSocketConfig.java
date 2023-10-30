package projects.web.tictactoe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import projects.web.tictactoe.ws.CustomHandShakeInterceptor;
import projects.web.tictactoe.ws.WebSocketAuthInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
      @Autowired private CustomHandShakeInterceptor customHandShakeInterceptor;
      @Autowired private WebSocketAuthInterceptor webSocketAuthInterceptor;

      @Override
      public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry
                    .addEndpoint("/ws")
                    .addInterceptors(customHandShakeInterceptor)
                    .setAllowedOrigins("http://tic-tac-toe-4124.s3-website.ap-south-1.amazonaws.com")
                    .withSockJS();
      }

      @Override
      public void configureClientInboundChannel(ChannelRegistration registration) {
            registration.interceptors(webSocketAuthInterceptor);
      }

      @Override
      public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.setApplicationDestinationPrefixes("/client");
            registry.enableSimpleBroker("/game");
      }
}
