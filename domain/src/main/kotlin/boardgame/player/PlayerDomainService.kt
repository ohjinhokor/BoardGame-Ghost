package boardgame.player

import boardgame.entitybase.BinaryId
import boardgame.escapee.BlueEscapee
import boardgame.escapee.RedEscapee
import boardgame.exception.CustomException
import boardgame.exception.HttpStatus
import boardgame.player.Player.Nickname
import java.time.LocalDateTime

class PlayerDomainService(
) {
    data class CreatePlayerCommand(
        val nickname: Nickname,
    )

    fun createPlayer(command: CreatePlayerCommand) =
        Player(
            id = BinaryId.new(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            nickname = command.nickname,
            joinedGame = null,
        )

    fun addEscapees(
        player: Player,
        blueEscapees: List<BlueEscapee>,
        redEscapees: List<RedEscapee>,
    ) {
        player.addEscapee(blueEscapees = blueEscapees, redEscapees = redEscapees)
    }

    fun processGameResult(
        winner: Player,
        loser: Player,
    ) {
        winner.win()
        loser.lose()
    }

    fun findLoser(
        winner: Player,
    ): Player {
        val game = winner.joinedGame ?: throw CustomException("플레이어가 참여하고 있는 게임이 없습니다", HttpStatus.BAD_REQUEST)
        return game.players.first { it != winner }
    }
}
