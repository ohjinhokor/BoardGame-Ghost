package boardgame.player

import boardgame.player.Player.Nickname
import boardgame.player.PlayerDomainService.CreatePlayerCommand

class PlayerTestFixturesUtil {
    val domainService: PlayerDomainService = PlayerDomainService()

    fun createPlayer() = domainService.createPlayer(CreatePlayerCommand(Nickname("test")))

    fun createPlayerWithNickname(nickname: String) = domainService.createPlayer(CreatePlayerCommand(Nickname(nickname)))
}
