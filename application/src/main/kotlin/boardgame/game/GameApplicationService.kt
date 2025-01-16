package boardgame.game

import boardgame.entitybase.BinaryId
import boardgame.game.GameDomainService.AddPlayerCommand
import boardgame.game.GameDomainService.CreateGameCommand
import boardgame.player.Player

class GameApplicationService(
    private val gameRepository: GameRepository,
    private val gameDomainService: GameDomainService,
) {
    fun createGame(command: CreateGameCommand) {
        val game = gameDomainService.createGame(command)
        gameRepository.save(game)
    }

    fun addPlayer(
        id: BinaryId,
        player: Player,
    ) {
        val game = gameRepository.getOrException(id)
        gameDomainService.addPlayer(AddPlayerCommand(game, player))
    }

    fun startGame(id: BinaryId) {
        val game = gameRepository.getOrException(id)
        game.start()
    }

    fun endGame(
        id: BinaryId,
        winner: Player,
    ) {
        val game = gameRepository.getOrException(id)
        game.endWith(winner)
    }
}
