package boardgame.push.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    val properties: WebSocketConfigProperties,
) : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint(properties.endPoint)
            .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry
            .enableStompBrokerRelay(properties.stompRelayPrefix)
            .setRelayHost(properties.relayHost)
            .setRelayPort(properties.relayPort)
            .setClientLogin(properties.clientLogin)
            .setClientPasscode(properties.clientPasscode)
    }
}
