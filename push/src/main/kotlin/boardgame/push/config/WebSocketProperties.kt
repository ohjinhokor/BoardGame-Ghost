package boardgame.push.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

data class WebSocketConfigProperties(
    val endPoint: String,
    val stompRelayPrefix: String,
    val relayHost: String,
    val relayPort: Int,
    val clientLogin: String,
    val clientPasscode: String,
)

@Configuration
class WebSocketProperties(
    private val env: Environment,
) {
    @Bean
    fun websocketProperties(): WebSocketConfigProperties =
        WebSocketConfigProperties(
            endPoint = env.getRequiredProperty("push.endpoint"),
            stompRelayPrefix = env.getRequiredProperty("push.stomp-relay-prefix"),
            relayHost = env.getRequiredProperty("push.relay-host"),
            relayPort = env.getRequiredProperty("push.relay-port").toInt(),
            clientLogin = env.getRequiredProperty("push.username"),
            clientPasscode = env.getRequiredProperty("push.password"),
        )
}
