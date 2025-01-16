package boardgame.player

import boardgame.StubRepository

object StubPlayerRepository : PlayerRepository, StubRepository<Player>()

val playerRepository = StubPlayerRepository

fun PlayerTestFixturesUtil.createPlayerAndSave(): Player {
    val player = createPlayer()
    return playerRepository.save(player)
}