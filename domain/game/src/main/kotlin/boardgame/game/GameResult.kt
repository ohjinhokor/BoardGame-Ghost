package boardgame.game

import boardgame.core.entitybase.BinaryId
import boardgame.core.entitybase.EntityBase
import java.time.LocalDateTime

class GameResult(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    val game: Game,
    val winnerId: BinaryId,
    val loserId: BinaryId,
) : EntityBase(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
