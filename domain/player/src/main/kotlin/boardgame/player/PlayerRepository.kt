package boardgame.player

import boardgame.core.repository.Repository
import boardgame.game.Game

interface PlayerRepository : Repository<Player> {
    fun findByJoinedGame(game: Game): List<Player>
}
