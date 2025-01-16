package boardgame.player

import boardgame.entitybase.BinaryId
import boardgame.entitybase.EntityBase
import boardgame.escapee.BlueEscapee
import boardgame.escapee.ESCAPEE_KOREAN_NAME
import boardgame.escapee.Escapee
import boardgame.escapee.RedEscapee
import boardgame.exception.CustomException
import boardgame.exception.HttpStatus
import boardgame.game.Game
import java.time.LocalDateTime

class Player internal constructor(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    nickname: Nickname,
    joinedGame: Game?,
) : EntityBase(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
    ) {
    companion object {
        val RED_ESCAPEE_SIZE = 4
        val BLUE_ESCAPEE_SIZE = 4
    }

    var nickname: Nickname = nickname
        private set

    var winCount: Int = 0
        private set

    var loseCount: Int = 0
        private set

    var joinedGame: Game? = joinedGame
        private set

    val escapees: MutableList<Escapee> = mutableListOf()

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

    internal fun addEscapee(
        redEscapees: List<RedEscapee>,
        blueEscapees: List<BlueEscapee>,
    ) {
        if (this.escapees.isNotEmpty()) {
            throw CustomException("이미 ${ESCAPEE_KOREAN_NAME}이 생성되었습니다", HttpStatus.BAD_REQUEST)
        }

        if (redEscapees.size != RED_ESCAPEE_SIZE) {
            throw CustomException("빨간색 ${ESCAPEE_KOREAN_NAME}은 ${RED_ESCAPEE_SIZE}개가 필수입니다", HttpStatus.BAD_REQUEST)
        }

        if (blueEscapees.size != BLUE_ESCAPEE_SIZE) {
            throw CustomException("파랑색 ${ESCAPEE_KOREAN_NAME}은 ${BLUE_ESCAPEE_SIZE}개가 필수입니다", HttpStatus.BAD_REQUEST)
        }

        redEscapees.forEach { this.escapees.add(it) }
        blueEscapees.forEach { this.escapees.add(it) }
    }

    internal fun win() = winCount++

    internal fun lose() = loseCount++

    fun updateNickname(nickname: Nickname) = run { update(UpdatePlayerCommand(nickname = nickname)) }

    data class UpdatePlayerCommand(
        val nickname: Nickname? = null,
    )

    private fun update(command: UpdatePlayerCommand) {
        command.nickname?.let { this.nickname = it }
    }
}
