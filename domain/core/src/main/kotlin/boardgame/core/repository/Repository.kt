package boardgame.core.repository

import boardgame.core.entitybase.BinaryId
import boardgame.core.entitybase.EntityBase
import boardgame.core.exception.CustomException
import boardgame.core.exception.HttpStatus
import java.util.*
import kotlin.jvm.optionals.getOrNull

interface Repository<Entity : EntityBase> {
    fun findById(id: BinaryId): Optional<Entity>

    fun getOrNull(id: BinaryId): Entity? = findById(id).getOrNull()

    fun getOrException(
        id: BinaryId,
        exception: Throwable = CustomException("존재하지 않는 대상입니다", HttpStatus.NOT_FOUND),
    ): Entity = findById(id).orElseThrow { throw exception }

    fun findAll(): List<Entity>

    fun findAll(ids: List<BinaryId>): List<Entity>

    fun save(entity: Entity): Entity

    fun saveAll(entities: List<Entity>): List<Entity>

    fun delete(entity: Entity)
}
