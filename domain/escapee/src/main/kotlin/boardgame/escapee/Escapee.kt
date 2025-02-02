package boardgame.escapee

import boardgame.core.entitybase.BinaryId
import boardgame.core.entitybase.EntityBase
import boardgame.core.event.Event
import boardgame.core.event.EventBus
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import boardgame.player.Player
import org.jetbrains.annotations.TestOnly
import java.time.LocalDateTime
import kotlin.math.abs

const val MAXIMUM_MOVE_RANGE: Int = 1
const val ESCAPEE_KOREAN_NAME = "탈출인"

abstract class Escapee protected constructor(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    position: Position,
    status: Status,
    val player: Player,
    val type: Type,
) : EntityBase(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
    ) {
    var position: Position = position
        private set

    var status: Status = status
        private set

    init {
        if (position !in startPositions) {
            throw CustomException("${ESCAPEE_KOREAN_NAME}을 만들 수 있는 위치가 아닙니다.", HttpStatus.BAD_REQUEST)
        }
    }

    companion object {
        val minRowValue = 0
        val maxRowValue = 5
        val minColumnValue = 0
        val maxColumnValue = 5
        val escapablePositions = listOf(Position.of(0, 0), Position.of(0, 5))
        val startPositions =
            listOf(
                Position.of(4, 1),
                Position.of(4, 2),
                Position.of(4, 3),
                Position.of(4, 4),
                Position.of(5, 1),
                Position.of(5, 2),
                Position.of(5, 3),
                Position.of(5, 4),
            )
    }

    data class Position private constructor(
        val row: Row,
        val column: Column,
    ) {
        fun getDistanceFrom(position: Position) = abs(position.row.value - this.row.value) + abs(position.column.value - this.column.value)

        companion object {
            fun of(
                row: Int,
                column: Int,
            ) = Position(row = Row(row), column = Column(column))
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Position) return false

            if (row != other.row) return false
            if (column != other.column) return false

            return true
        }

        override fun hashCode(): Int = row.hashCode() + column.hashCode()
    }

    @JvmInline
    value class Row(
        val value: Int,
    ) {
        init {
            if (value < minRowValue || value > maxRowValue) {
                throw CustomException("${ESCAPEE_KOREAN_NAME}은 보드판 위에만 존재할 수 있습니다", HttpStatus.BAD_REQUEST)
            }
        }
    }

    @JvmInline
    value class Column(
        val value: Int,
    ) {
        init {
            if (value < minColumnValue || value > maxColumnValue) {
                throw CustomException("${ESCAPEE_KOREAN_NAME}은 보드판 위에만 존재할 수 있습니다", HttpStatus.BAD_REQUEST)
            }
        }
    }

    enum class Type {
        BLUE,
        RED,
    }

    enum class Status {
        ALIVE,
        DEAD,
        ESCAPE,
    }

    fun moveTo(position: Position) {
        if (this.position.getDistanceFrom(position) > MAXIMUM_MOVE_RANGE) {
            throw CustomException("이동할 수 없는 위치입니다.", HttpStatus.BAD_REQUEST)
        }
        update(position = position)
    }

    data class EscapeEvent(
        val player: Player,
    ) : Event

    internal fun escape() {
        if (this.type == Type.RED) {
            throw CustomException("탈출할 수 없는 색깔입니다.", HttpStatus.BAD_REQUEST)
        }
        if (this.status != Status.ALIVE) {
            throw CustomException("이미 죽은 탈출인입니다.", HttpStatus.BAD_REQUEST)
        }
        if (this.position !in escapablePositions) {
            throw CustomException("탈출을 시도할 수 없는 위치입니다.", HttpStatus.BAD_REQUEST)
        }
        update(status = Status.ESCAPE)
        EventBus.publish(EscapeEvent(player = this.player))
    }

    fun die() {
        update(status = Status.DEAD)
    }

    private fun update(
        status: Status? = null,
        position: Position? = null,
    ) {
        status?.let { this.status = it }
        position?.let { this.position = it }
    }

    /**
     * 테스트 코드의 확장함수를 위해 internal로 열어둠.
     * 비즈니스 로직에서 이 함수의 직접적인 호출은 지양함
     */
    @TestOnly
    internal fun unsafeUpdate(
        status: Status? = null,
        position: Position? = null,
    ) {
        status?.let { this.status = it }
        position?.let { this.position = it }
    }
}
