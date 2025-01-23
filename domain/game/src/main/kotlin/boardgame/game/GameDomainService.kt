package boardgame.game

import boardgame.core.entitybase.BinaryId
import boardgame.game.Game.Title
import java.time.LocalDateTime

class GameDomainService(
    private val gameRepository: GameRepository,
    private val gameResultRepository: GameResultRepository,
) {
    data class CreateGameCommand(
        val title: Title,
    )

    fun createGame(command: CreateGameCommand) =
        Game(
            id = BinaryId.new(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            title = command.title,
        )

    data class CreateGameResultCommand(
        val game: Game,
        val winnerId: BinaryId,
        val loserId: BinaryId,
    )

    private fun createGameResult(command: CreateGameResultCommand) =
        GameResult(
            id = BinaryId.new(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            game = command.game,
            winnerId = command.winnerId,
            loserId = command.loserId,
        )

    data class EndCommand(
        val game: Game,
        val winnerId: BinaryId,
        val loserId: BinaryId,
    )

    fun end(command: EndCommand) {
        command.game.end()
        val gameResult =
            createGameResult(
                CreateGameResultCommand(
                    game = command.game,
                    winnerId = command.winnerId,
                    loserId = command.loserId,
                ),
            )

        gameRepository.save(command.game)
        gameResultRepository.save(gameResult)
    }
}
