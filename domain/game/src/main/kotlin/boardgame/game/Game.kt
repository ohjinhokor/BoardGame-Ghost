package boardgame.game

import boardgame.core.entitybase.BinaryId
import boardgame.core.entitybase.EntityBase
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import java.time.LocalDateTime

class Game internal constructor(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    val title: Title,
) : EntityBase(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
    ) {
    var status = Status.BEFORE_START
        private set

    fun start() {
        if (this.status != Status.BEFORE_START) {
            throw CustomException("게임을 시작할 수 없습니다", HttpStatus.BAD_REQUEST)
        }
        update(status = Status.PROGRESS)
    }

    internal fun end() {
        if (this.status != Status.PROGRESS) {
            throw CustomException("시작하지 않은 게임입니다", HttpStatus.BAD_REQUEST)
        }
        update(status = Status.END)
    }

    private fun update(status: Status? = null) {
        status?.let { this.status = it }
    }

    @JvmInline
    value class Title(
        val value: String,
    ) {
        init {
            if (this.value.length > 20) {
                throw CustomException("방 제목은 20글자를 넘을 수 없습니다", HttpStatus.BAD_REQUEST)
            }
        }
    }

    enum class Status {
        BEFORE_START,
        END,
        PROGRESS,
    }
}
