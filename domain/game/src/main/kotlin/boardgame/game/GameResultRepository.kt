package boardgame.game

import boardgame.core.repository.Repository
import java.util.*

interface GameResultRepository : Repository<GameResult> {
    fun findByGame(game: Game): Optional<GameResult>
}
