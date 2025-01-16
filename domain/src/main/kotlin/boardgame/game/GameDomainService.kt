package boardgame.game

import boardgame.entitybase.BinaryId
import boardgame.game.Game.Title
import boardgame.player.Player
import java.time.LocalDateTime

class GameDomainService {
    data class CreateGameCommand(
        val title: Title,
        val gameCreator: Player,
    )

    fun createGame(command: CreateGameCommand) =
        Game(
            id = BinaryId.new(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            title = command.title,
            gameCreator = command.gameCreator,
        )

    data class AddPlayerCommand(
        val game: Game,
        val player: Player,
    )

    fun addPlayer(command: AddPlayerCommand) {
        val (game, player) = command
        game.addPlayer(player)
    }
}
