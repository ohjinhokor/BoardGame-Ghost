package boardgame.game

import boardgame.core.entitybase.BinaryId
import boardgame.core.event.Event
import boardgame.core.event.EventBus

class GameApplicationService(
    private val gameRepository: GameRepository,
    private val gameDomainService: GameDomainService,
) {
    data class CreateGameEvent(
        val creatorId: BinaryId,
        val game: Game,
    ) : Event

    fun createGame(
        creatorId: BinaryId,
        title: String,
    ): Game {
        val game = gameDomainService.createGame(GameDomainService.CreateGameCommand(Game.Title(title)))
        gameRepository.save(game)
        EventBus.publish(CreateGameEvent(creatorId, game))
        return game
    }
}
