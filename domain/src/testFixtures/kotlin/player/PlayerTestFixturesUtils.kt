package player

import boardgame.player.Player.Nickname
import boardgame.player.PlayerDomainService
import boardgame.player.PlayerDomainService.CreatePlayerCommand

class PlayerTestFixturesUtils {
    val domainService: PlayerDomainService = PlayerDomainService()

    fun createPlayer() = domainService.createPlayer(CreatePlayerCommand(Nickname("test")))
}
