package boardgame.game

import boardgame.entitybase.BinaryId
import boardgame.escapee.Escapee
import boardgame.event.EventBus
import boardgame.game.GameDomainService.AddPlayerCommand
import boardgame.game.GameDomainService.CreateGameCommand
import boardgame.player.Player
import boardgame.player.PlayerApplicationService

class GameApplicationService(
    private val gameRepository: GameRepository,
    private val gameDomainService: GameDomainService,
    private val playerApplicationService: PlayerApplicationService,
) {
    init {
        EventBus.receive<Escapee.EscapeEvent> {
            val game = gameDomainService.endGame(it.player)
            gameRepository.save(game)
        }
    }

    fun createGame(command: CreateGameCommand): Game {
        val game = gameDomainService.createGame(command)
        playerApplicationService.joinGame(command.gameCreator, game)
        return gameRepository.save(game)
    }

    fun addPlayer(
        id: BinaryId,
        player: Player,
    ) {
        val game = gameRepository.getOrException(id)
        gameDomainService.addPlayer(AddPlayerCommand(game, player))
        gameRepository.save(game)
    }

    fun startGame(id: BinaryId) {
        val game = gameRepository.getOrException(id)
        game.start()
        gameRepository.save(game)
    }
}
