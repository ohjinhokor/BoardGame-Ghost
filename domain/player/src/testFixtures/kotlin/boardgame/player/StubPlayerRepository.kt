package boardgame.player

import boardgame.game.Game
import boardgame.stub.StubRepository

object StubPlayerRepository : PlayerRepository, StubRepository<Player>() {
    override fun findByJoinedGame(game: Game): List<Player> = database.values.filter { it.joinedGame == game }
}
