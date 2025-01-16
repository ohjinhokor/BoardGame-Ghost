package boardgame.game

import boardgame.entitybase.BinaryId
import boardgame.game.GameDomainService.AddPlayerCommand
import boardgame.game.GameDomainService.CreateGameCommand
import boardgame.player.Player

class GameApplicationService(
    private val gameRepository: GameRepository,
    private val gameDomainService: GameDomainService,
) {
    fun createGame(command: CreateGameCommand): Game {
        val game = gameDomainService.createGame(command)
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
