package boardgame.player

import boardgame.entitybase.BinaryId
import boardgame.escapee.Escapee
import boardgame.escapee.EscapeeApplicationService
import boardgame.event.EventBus
import boardgame.game.Game

class PlayerApplicationService(
    private val playerDomainService: PlayerDomainService,
    private val playerRepository: PlayerRepository,
    private val escapeeApplicationService: EscapeeApplicationService,
) {
    init {
        EventBus.receive<Escapee.EscapeEvent> {
            processGameResult(it.player)
        }
    }

    private fun processGameResult(winner: Player) {
        val loser = playerDomainService.findLoser(winner)
        playerDomainService.processGameResult(winner = winner, loser = loser)
        playerRepository.save(winner)
        playerRepository.save(loser)
    }

    fun joinGame(player: Player, game: Game) {
        player.joinGame(game)
        playerRepository.save(player)
    }

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
        val escapees = escapeeApplicationService.createEscapees(
            player = player,
            bluePositions = bluePositions,
            redPositions = redPositions
        )
        playerDomainService.addEscapees(player = player, blueEscapees = escapees.first, redEscapees = escapees.second)
        playerRepository.save(player)
    }
}
