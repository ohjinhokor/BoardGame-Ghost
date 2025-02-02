package boardgame.escapee

import boardgame.core.entitybase.BinaryId
import boardgame.player.Player
import java.time.LocalDateTime

const val INITIAL_RED_ESCAPEE_COUNT: Int = 4

class RedEscapee internal constructor(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    position: Position,
    player: Player,
) : Escapee(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        position = position,
        player = player,
        status = Status.ALIVE,
        type = Type.RED,
    )
