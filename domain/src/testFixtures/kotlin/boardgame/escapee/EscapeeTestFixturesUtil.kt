package boardgame.escapee

import boardgame.entitybase.BinaryId
import boardgame.player.Player
import java.time.LocalDateTime

/*
 Test Fixtures Utils
 원래는 1칸씩만 움직일 수 있으나, test를 위해서 어디로나 움직일 수 있도록
 */
val ESCAPABLE_ROW = 0
val ESCAPABLE_COLUMN = 0

fun Escapee.moveToAnyWhere(position: Escapee.Position) {
    unsafeUpdate(position = position)
}

class EscapeeTestFixturesUtil {
    fun createBlueEscapee(
        position: Escapee.Position,
        player: Player,
    ) = BlueEscapee(
        id = BinaryId.new(),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        position = position,
        player = player,
    )

    fun createRedEscapee(
        position: Escapee.Position,
        player: Player,
    ) = RedEscapee(
        id = BinaryId.new(),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        position = position,
        player = player,
    )
}
