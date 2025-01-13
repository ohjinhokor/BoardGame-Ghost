package boardgame.escapee

import boardgame.exception.CustomException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PositionTest :
    BehaviorSpec({
        given("Position 2개가 주어졌을 때") {
            `when`("행,열이 같은 값이 주어지면") {
                then("동일하게 간주된다") {
                    Escapee.Position.of(1, 1) shouldBe Escapee.Position.of(1, 1)
                    Escapee.Position.of(4, 3) shouldBe Escapee.Position.of(4, 3)
                }
                then("Collection에 포함되었다고 간주한다.") {
                    (
                        Escapee.Position.of(1, 1) in
                            listOf(
                                Escapee.Position.of(1, 1),
                                Escapee.Position.of(4, 3),
                            )
                    ) shouldBe true
                }
            }
            `when`("행,열이 다른 값이 주어지면") {
                then("다르게 간주된다") {
                    Escapee.Position.of(1, 1) shouldNotBe Escapee.Position.of(1, 4)
                    Escapee.Position.of(1, 1) shouldNotBe Escapee.Position.of(3, 1)
                    Escapee.Position.of(1, 3) shouldNotBe Escapee.Position.of(3, 1)
                }
                then("Collection에 포함되지 않은것으로 간주한다.") {
                    (
                        Escapee.Position.of(1, 1) in
                            listOf(
                                Escapee.Position.of(1, 2),
                                Escapee.Position.of(1, 3),
                            )
                    ) shouldBe false
                    (
                        Escapee.Position.of(3, 1) in
                            listOf(
                                Escapee.Position.of(1, 2),
                                Escapee.Position.of(1, 3),
                            )
                    ) shouldBe false
                }
            }
        }
        given("Position 위치가 주어졌을 때") {
            `when`("정해진 위치를 벗어나면") {
                then("예외 처리를 한다.") {
                    var outOfRangeColumn = 6
                    shouldThrow<CustomException> {
                        Escapee.Position.of(1, outOfRangeColumn)
                    }
                    outOfRangeColumn = -1
                    shouldThrow<CustomException> {
                        Escapee.Position.of(1, outOfRangeColumn)
                    }

                    var outOfRangeRow = 6
                    shouldThrow<CustomException> {
                        Escapee.Position.of(1, outOfRangeRow)
                    }
                    outOfRangeColumn = -1
                    shouldThrow<CustomException> {
                        Escapee.Position.of(1, outOfRangeRow)
                    }
                }
            }
        }
    })
