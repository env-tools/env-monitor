package org.envtools.monitor.ui.app.config;

/**
 * Created: 9/20/15 1:11 AM
 *
 * @author Yury Yakovlev
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //Set up destination prefix for subscriptions
        config.enableSimpleBroker("/subscribe");
        //Set up destination prefix for calls
        config.setApplicationDestinationPrefixes("/call", "/message");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/monitor").setAllowedOrigins("*").withSockJS();
    }

}
