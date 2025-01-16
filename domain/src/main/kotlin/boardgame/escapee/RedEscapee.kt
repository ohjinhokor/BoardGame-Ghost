package boardgame.escapee

import boardgame.entitybase.BinaryId
import boardgame.player.Player
import java.time.LocalDateTime

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
