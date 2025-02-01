package boardgame.escapee

import boardgame.core.entitybase.BinaryId
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import boardgame.player.Player
import java.time.LocalDateTime

class EscapeeDomainService {
    data class CreateEscapeesCommand(
        val positions: List<Escapee.Position>,
        val player: Player,
    )

    fun createRedEscapees(command: CreateEscapeesCommand): List<RedEscapee> =
        command.positions.mapIndexed { index, position ->
            createEscapee(
                CreateEscapeeCommand(
                    position = position,
                    player = command.player,
                    type = Escapee.Type.RED,
                ),
            ) as RedEscapee
        }

    fun createBlueEscapees(command: CreateEscapeesCommand): List<BlueEscapee> =
        command.positions.mapIndexed { index, position ->
            createEscapee(
                CreateEscapeeCommand(
                    position = position,
                    player = command.player,
                    type = Escapee.Type.BLUE,
                ),
            ) as BlueEscapee
        }

    data class CreateEscapeeCommand(
        val position: Escapee.Position,
        val player: Player,
        val type: Escapee.Type,
    )

    private fun createEscapee(command: CreateEscapeeCommand): Escapee {
        if (command.type == Escapee.Type.RED) {
            return RedEscapee(
                id = BinaryId.new(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                position = command.position,
                player = command.player,
            )
        }
        if (command.type == Escapee.Type.BLUE) {
            return BlueEscapee(
                id = BinaryId.new(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                position = command.position,
                player = command.player,
            )
        }
        throw CustomException("존재하지 않는 타입입니다", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
