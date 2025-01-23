package boardgame.game

import boardgame.stub.StubRepository
import java.util.*

object StubGameResultRepository : StubRepository<GameResult>(), GameResultRepository {
    override fun findByGame(game: Game): Optional<GameResult> {
        val results = database.values.filter { it.game == game }

        return when {
            results.isEmpty() -> Optional.empty()
            results.size == 1 -> Optional.of(results.first())
            else -> throw RuntimeException("Duplicate GameResult")
        }
    }
}
