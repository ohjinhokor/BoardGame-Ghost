package boardgame.entitybase

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class TestEntity(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : EntityBase(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

class EntityBaseTest :
    FunSpec({
        val binaryId = BinaryId.new()
        val testEntity1 = TestEntity(binaryId, LocalDateTime.of(2024, 12, 31, 0, 0, 0), LocalDateTime.now())
        val testEntity2 = TestEntity(binaryId, LocalDateTime.of(2025, 1, 9, 0, 0, 0), LocalDateTime.now())

        test("Equals Test : id외의 다른 필드와 관계없이, id가 같으면 동일한 Entity로 취급") {
            (testEntity1 == testEntity2) shouldBe true
            testEntity1 shouldBe testEntity2
        }

        test("Hashcode Test : id외의 다른 필드와 관계없이, id가 같으면 동일한 Entity로 취급") {
            (testEntity1 in listOf(testEntity2)) shouldBe true
        }
    })
