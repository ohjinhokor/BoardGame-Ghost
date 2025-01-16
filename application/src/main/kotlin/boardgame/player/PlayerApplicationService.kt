package boardgame.player

import boardgame.entitybase.BinaryId
import boardgame.escapee.Escapee

class PlayerApplicationService(
    private val playerDomainService: PlayerDomainService,
    private val playerRepository: PlayerRepository,
) {
    fun createPlayer(nickname: String) {
        val player =
            playerDomainService.createPlayer(PlayerDomainService.CreatePlayerCommand(nickname = Player.Nickname(nickname)))
        playerRepository.save(player)
    }

    fun updateNickname(
        playerId: BinaryId,
        nickname: String,
    ) {
        val player = playerRepository.getOrException(playerId)
        player.updateNickname(nickname = Player.Nickname(nickname))
        playerRepository.save(player)
    }

    fun addEscapees(
        playerId: BinaryId,
        bluePositions: List<Escapee.Position>,
        redPositions: List<Escapee.Position>,
    ) {
        val player = playerRepository.getOrException(playerId)
        playerDomainService.addEscapees(player = player, bluePositions = bluePositions, redPositions = redPositions)
        playerRepository.save(player)
    }
}
