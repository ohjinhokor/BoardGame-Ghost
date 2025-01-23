package boardgame.core.entitybase

import java.time.LocalDateTime

abstract class EntityBase(
    val id: BinaryId,
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {
    var updatedAt = updatedAt
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false
        other as EntityBase
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
