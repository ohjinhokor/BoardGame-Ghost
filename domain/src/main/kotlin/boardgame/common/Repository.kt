package boardgame.common

import boardgame.entitybase.BinaryId
import boardgame.entitybase.EntityBase
import java.time.LocalDateTime

interface Repository<Entity : EntityBase> {
    fun findById(id: BinaryId): Entity

    fun findAll(): List<Entity>

    fun findAll(ids: List<BinaryId>): List<Entity>

    fun save(entity: Entity): Entity

    fun delete(entity: Entity): LocalDateTime
}
