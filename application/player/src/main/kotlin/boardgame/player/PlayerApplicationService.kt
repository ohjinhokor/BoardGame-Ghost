package boardgame.player

import boardgame.core.entitybase.BinaryId
import boardgame.core.event.EventBus
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import boardgame.game.Game
import boardgame.game.GameApplicationService

class PlayerApplicationService(
    private val playerDomainService: PlayerDomainService,
    private val playerRepository: PlayerRepository,
    private val gameApplicationService: GameApplicationService,
) {
    init {
        EventBus.receive<GameApplicationService.CreateGameEvent> {
            joinGame(player = playerRepository.getOrException(it.creatorId), game = it.game)
        }
    }

    fun joinGame(
        player: Player,
        gameId: BinaryId,
    ) {
        val game =
            gameApplicationService.findById(gameId) ?: throw CustomException("존재하지 않는 게임입니다", HttpStatus.BAD_REQUEST)
        joinGame(player, game)
    }

    private fun joinGame(
        player: Player,
        game: Game,
    ) {
        playerDomainService.joinGame(player, game)
        playerRepository.save(player)
    }

    fun startGame(playerId: BinaryId) {
        val player = playerRepository.getOrException(playerId)
        playerDomainService.startGame(player)
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
}
