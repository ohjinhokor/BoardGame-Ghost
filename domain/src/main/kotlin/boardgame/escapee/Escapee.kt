package boardgame.escapee

import boardgame.entitybase.BinaryId
import boardgame.entitybase.EntityBase
import boardgame.escapee.Escapee.Position
import boardgame.exception.CustomException
import boardgame.exception.HttpStatus
import boardgame.player.Player
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
    val index: Index,
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
        val minIndexValue = 1
        val maxIndexValue = 4
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

    class Position private constructor(
        row: Row,
        column: Column,
    ) {
        private var row: Row = row
            private set
        private var column: Column = column
            private set

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

    @JvmInline
    value class Index(
        private val value: Int,
    ) {
        init {
            if (value < minIndexValue || value > maxIndexValue) {
                throw CustomException("${ESCAPEE_KOREAN_NAME}은 1부터 4까지만 만들 수 있습니다", HttpStatus.BAD_REQUEST)
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

    fun escape() {
        if (this.position in escapablePositions) {
            this.status = Status.ESCAPE
        } else {
            throw CustomException("탈출을 시도할 수 없는 위치입니다.", HttpStatus.BAD_REQUEST)
        }
    }

    fun die() {
        update(status = Status.DEAD)
    }

    fun update(
        status: Status? = null,
        position: Position? = null,
    ) {
        status?.let { this.status = it }
        position?.let { this.position = it }
    }
}
