package boardgame.game

import boardgame.entitybase.BinaryId
import boardgame.player.Player
import java.time.LocalDateTime

class GameTestFixturesUtil {
    fun createGame(player: Player) =
        Game(
            id = BinaryId.new(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            title = Game.Title("제목"),
            gameCreator = player,
        )
}
