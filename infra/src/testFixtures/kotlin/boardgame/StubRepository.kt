package boardgame

import boardgame.common.Repository
import boardgame.entitybase.BinaryId
import boardgame.entitybase.EntityBase
import java.util.*

abstract class StubRepository<ENTITY : EntityBase> : Repository<ENTITY> {
    val database = mutableMapOf<BinaryId, ENTITY>()

    override fun findById(id: BinaryId): Optional<ENTITY> {
        val entity: ENTITY = database.get(id) ?: return Optional.empty()
        return Optional.of(entity)
    }

    override fun findAll(): List<ENTITY> = database.values.toList()

    override fun findAll(ids: List<BinaryId>): List<ENTITY> = ids.mapNotNull { database.get(it) }.toList()

    override fun delete(entity: ENTITY) {
        database.remove(entity.id)
    }

    override fun save(entity: ENTITY): ENTITY {
        database[entity.id] = entity
        return database[entity.id]!!
    }

    override fun saveAll(entities: List<ENTITY>): List<ENTITY> {
        return entities.map {
            database[it.id] = it
            it
        }
    }
}
