package boardgame.escapee

import boardgame.core.repository.Repository
import boardgame.player.Player

interface EscapeeRepository : Repository<Escapee> {
    fun findByPlayer(player: Player): List<Escapee>
}
