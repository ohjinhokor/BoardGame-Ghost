package boardgame.player

import boardgame.core.entitybase.BinaryId
import boardgame.core.entitybase.EntityBase
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import boardgame.game.Game
import java.time.LocalDateTime

class Player internal constructor(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    nickname: Nickname,
    joinedGame: Game?,
    status: Status,
) : EntityBase(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
    ) {
    var nickname: Nickname = nickname
        private set

    var winCount: Int = 0
        private set

    var loseCount: Int = 0
        private set

    var joinedGame: Game? = joinedGame
        private set

    var status: Status = status
        private set

    @JvmInline
    value class Nickname(
        val value: String,
    ) {
        init {
            if (value.isBlank()) {
                throw CustomException("닉네임을 입력해주세요", HttpStatus.BAD_REQUEST)
            }
            if (value.length > 30) {
                throw CustomException("닉네임 가능 길이를 초과했습니다", HttpStatus.BAD_REQUEST)
            }
        }
    }

    internal fun win() {
        update(UpdatePlayerCommand(status = Status.NONE))
        winCount++
    }

    internal fun lose() {
        update(UpdatePlayerCommand(status = Status.NONE))
        loseCount++
    }

    internal fun joinGame(game: Game) {
        update(UpdatePlayerCommand(status = Status.JOINED, joinedGame = game))
    }

    fun quitGame() {
        update(UpdatePlayerCommand(quitGame = true))
    }

    fun updateNickname(nickname: Nickname) = run { update(UpdatePlayerCommand(nickname = nickname)) }

    internal fun readyForGame() {
        update(UpdatePlayerCommand(status = Status.READY))
    }

    internal fun startGame() {
        update(UpdatePlayerCommand(status = Status.PLAYING))
    }

    data class UpdatePlayerCommand(
        val nickname: Nickname? = null,
        val joinedGame: Game? = null,
        val quitGame: Boolean? = null,
        val status: Status? = null,
    )

    private fun update(command: UpdatePlayerCommand) {
        command.nickname?.let { this.nickname = it }
        command.joinedGame?.let { this.joinedGame = it }
        command.quitGame?.let { this.joinedGame = null }
        command.status?.let { this.status = it }
    }

    enum class Status {
        NONE,
        JOINED,
        READY,
        PLAYING,
    }
}
