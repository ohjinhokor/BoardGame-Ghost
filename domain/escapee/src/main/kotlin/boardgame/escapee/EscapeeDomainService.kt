package boardgame.escapee

import boardgame.core.entitybase.BinaryId
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import boardgame.player.Player
import java.time.LocalDateTime

class EscapeeDomainService(
    private val repository: EscapeeRepository,
) {
    data class CreateEscapeesCommand(
        val positions: List<Escapee.Position>,
        val player: Player,
    )

    fun createRedEscapees(command: CreateEscapeesCommand): List<RedEscapee> {
        if (command.positions.size != INITIAL_RED_ESCAPEE_COUNT) {
            throw CustomException("빨간색 $ESCAPEE_KOREAN_NAME 생성은 한 번에 4개만 가능합니다.", HttpStatus.BAD_REQUEST)
        }
        if (repository.findByPlayer(command.player).any { it.type == Escapee.Type.RED }) {
            throw CustomException("이미 생성된 ${ESCAPEE_KOREAN_NAME}이 있습니다.", HttpStatus.BAD_REQUEST)
        }
        return command.positions.map { position ->
            createEscapee(
                CreateEscapeeCommand(
                    position = position,
                    player = command.player,
                    type = Escapee.Type.RED,
                ),
            ) as RedEscapee
        }
    }

    fun createBlueEscapees(command: CreateEscapeesCommand): List<BlueEscapee> {
        if (command.positions.size != INITIAL_BLUE_ESCAPEE_COUNT) {
            throw CustomException("파란색 $ESCAPEE_KOREAN_NAME 생성은 한 번에 4개만 가능합니다.", HttpStatus.BAD_REQUEST)
        }
        if (repository.findByPlayer(command.player).any { it.type == Escapee.Type.BLUE }) {
            throw CustomException("이미 생성된 ${ESCAPEE_KOREAN_NAME}이 있습니다.", HttpStatus.BAD_REQUEST)
        }
        return command.positions.map { position ->
            createEscapee(
                CreateEscapeeCommand(
                    position = position,
                    player = command.player,
                    type = Escapee.Type.BLUE,
                ),
            ) as BlueEscapee
        }
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
