package boardgame.escapee

import boardgame.player.Player
import boardgame.stub.StubRepository

object StubEscapeeRepository :
    StubRepository<Escapee>(),
    EscapeeRepository {
    override fun findByPlayer(player: Player): List<Escapee> = database.values.filter { it.player == player }
}
