package boardgame.escapee

import boardgame.entitybase.BinaryId
import boardgame.player.Player

class EscapeeApplicationService(
    private val escapeeRepository: EscapeeRepository,
    private val escapeeDomainService: EscapeeDomainService,
) {
    fun move(
        escapeeId: BinaryId,
        position: Escapee.Position,
    ) {
        val escapee = escapeeRepository.getOrException(escapeeId)
        escapee.moveTo(position)
        escapeeRepository.save(escapee)
    }

    fun escape(escapeeId: BinaryId) {
        val escapee = escapeeRepository.getOrException(escapeeId)
        escapee.escape()
        escapeeRepository.save(escapee)
    }

    fun createEscapees(
        player: Player,
        bluePositions: List<Escapee.Position>,
        redPositions: List<Escapee.Position>,
    ): Pair<List<BlueEscapee>, List<RedEscapee>> {
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

        escapeeRepository.saveAll(blueEscapees)
        escapeeRepository.saveAll(redEscapees)
        return Pair(blueEscapees, redEscapees)
    }
}
