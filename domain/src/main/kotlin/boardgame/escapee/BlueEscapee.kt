package boardgame.escapee

import boardgame.entitybase.BinaryId
import boardgame.player.Player
import java.time.LocalDateTime

class BlueEscapee internal constructor(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    position: Position,
    index: Index,
    player: Player,
) : Escapee(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        position = position,
        index = index,
        player = player,
        status = Status.ALIVE,
        type = Type.BLUE,
    )
