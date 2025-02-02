package boardgame.player

import boardgame.core.entitybase.BinaryId
import boardgame.core.event.Event
import boardgame.core.event.EventBus
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import boardgame.game.Game
import boardgame.game.GameDomainService
import boardgame.player.Player.Nickname
import java.time.LocalDateTime

class PlayerDomainService(
    private val repository: PlayerRepository,
    private val gameDomainService: GameDomainService,
) {
    init {
        EventBus.receive<GameFinishedEvent> {
            win(it.winner)
        }
    }

    data class GameFinishedEvent(
        val winner: Player,
    ) : Event

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
            status = Player.Status.NONE,
        )

    private fun win(winner: Player) {
        val loser = findLoserByWinner(winner)
        processGameResult(winner, loser)

        // TODO : 추후 GameResult를 Game으로부터 분리할 예정. 그러면 event 던져야 함
        gameDomainService.end(
            GameDomainService.EndCommand(
                game = winner.joinedGame!!,
                winnerId = winner.id,
                loserId = loser.id,
            ),
        )
    }

    private fun processGameResult(
        winner: Player,
        loser: Player,
    ) {
        winner.win()
        loser.lose()

        repository.save(winner)
        repository.save(loser)
    }

    private fun findLoserByWinner(winner: Player): Player {
        val game = winner.joinedGame ?: throw CustomException("플레이어가 참여하고 있는 게임이 없습니다", HttpStatus.BAD_REQUEST)
        try {
            return repository.findByJoinedGame(game).first { it != winner }
        } catch (exception: Exception) {
            throw CustomException("게임 상대가 없습니다", HttpStatus.BAD_REQUEST)
        }
    }

    fun startGame(player: Player) {
        val joinedGame = player.joinedGame ?: throw CustomException("플레이어가 참여하고 있는 게임이 없습니다", HttpStatus.BAD_REQUEST)
        val otherPlayers = repository.findByJoinedGame(joinedGame).filter { it != player }
        if (otherPlayers.size != 1) {
            throw CustomException("게임 시작 가능 인원이 아닙니다", HttpStatus.BAD_REQUEST)
        }

        val otherPlayer = otherPlayers.first()
        if (otherPlayer.status == Player.Status.JOINED) {
            player.readyForGame()
            repository.save(player)
            return
        }

        if (otherPlayer.status == Player.Status.READY) {
            otherPlayer.startGame()
            player.startGame()
            repository.save(player)
            repository.save(otherPlayer)
            gameDomainService.startGame(joinedGame.id)
            return
        }

        throw CustomException("상대 플레이어가 '참여 상태' 또는 '준비 상태'가 아닙니다", HttpStatus.BAD_REQUEST)
    }

    fun joinGame(
        player: Player,
        game: Game,
    ) {
        player.joinedGame?.let { throw CustomException("플레이어가 참여하고 있는 게임이 있습니다", HttpStatus.BAD_REQUEST) }
        if (repository.findByJoinedGame(game).size >= 2) {
            throw CustomException("게임에 참여 인원 초과입니다", HttpStatus.BAD_REQUEST)
        }
        player.joinGame(game)
    }
}
