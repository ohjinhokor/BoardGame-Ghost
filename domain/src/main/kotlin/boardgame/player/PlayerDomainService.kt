package boardgame.player

import boardgame.entitybase.BinaryId
import boardgame.escapee.Escapee
import boardgame.escapee.EscapeeDomainService
import boardgame.player.Player.Nickname
import java.time.LocalDateTime

class PlayerDomainService(
    val playerRepository: PlayerRepository,
    val escapeeDomainService: EscapeeDomainService,
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
        bluePositions: List<Escapee.Position>,
        redPositions: List<Escapee.Position>,
    ) {
        val blueEscapees =
            escapeeDomainService.createBlueEscapees(
                EscapeeDomainService.CreateEscapeesCommand(
                    positions = bluePositions,
                    player = player,
                ),
            )
        val redEscapees =
            escapeeDomainService.createRedEscapees(
                EscapeeDomainService.CreateEscapeesCommand(
                    positions = redPositions,
                    player = player,
                ),
            )

        player.addEscapee(blueEscapees = blueEscapees, redEscapees = redEscapees)
    }

    fun processGameResult(
        winner: Player,
        loser: Player,
    ) {
        winner.win()
        loser.lose()
        playerRepository.save(winner)
        playerRepository.save(loser)
    }
}
