package boardgame.game

import boardgame.core.entitybase.BinaryId
import java.time.LocalDateTime

class GameTestFixturesUtil {
    fun createGame() =
        Game(
            id = BinaryId.new(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            title = Game.Title("제목"),
        )
}
