package boardgame.player

import boardgame.entitybase.BinaryId
import boardgame.player.Player.Nickname
import java.time.LocalDateTime

class PlayerDomainService {
    data class CreatePlayerCommand(
        val nickname: Nickname,
    )

    fun createPlayer(command: CreatePlayerCommand) =
        Player(
            id = BinaryId.new(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            nickname = command.nickname,
        )

    data class UpdatePlayerCommand(
        val nickname: Nickname? = null,
    )

    fun updatePlayer(
        player: Player,
        command: UpdatePlayerCommand,
    ) {
        player.update(command)
    }
}
