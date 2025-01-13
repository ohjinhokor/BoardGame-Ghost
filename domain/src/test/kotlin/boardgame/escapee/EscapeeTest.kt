package boardgame.escapee

import boardgame.exception.CustomException
import boardgame.player.Player
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import moveToAnyWhere

class EscapeeTest :
    FunSpec({
        test("처음 Escapee를 만들때, 정해진 위치 외에는 만들 수 없음") {
            shouldThrow<CustomException> {
                BlueEscapee(
                    Escapee.Position.of(6, 1),
                    Escapee.Index(1),
                    Player(),
                )
            }
            shouldThrow<CustomException> {
                BlueEscapee(
                    Escapee.Position.of(-1, 1),
                    Escapee.Index(2),
                    Player(),
                )
            }
            shouldThrow<CustomException> {
                BlueEscapee(
                    Escapee.Position.of(3, 1),
                    Escapee.Index(3),
                    Player(),
                )
            }
        }

        test("최대 이동 거리 제한이 있음") {
            val startRow = 5
            val startCol = 1
            val blueEscapee =
                BlueEscapee(
                    Escapee.Position.of(startRow, startCol),
                    Escapee.Index(1),
                    Player(),
                )

            shouldThrow<CustomException> {
                blueEscapee.moveTo(Escapee.Position.of(startRow - 1, startCol + 1))
            }
            shouldThrow<CustomException> {
                blueEscapee.moveTo(Escapee.Position.of(startRow + 2, startCol))
            }
        }

        test("탈출 가능 위치에 제한이 있음") {
            val startRow = 5
            val startCol = 1
            val blueEscapee1 =
                BlueEscapee(
                    Escapee.Position.of(startRow, startCol),
                    Escapee.Index(1),
                    Player(),
                )
            var escapableRow = 0
            var escapableCol = 5
            blueEscapee1.moveToAnyWhere(Escapee.Position.of(escapableRow, escapableCol))
            blueEscapee1.escape()
            blueEscapee1.status shouldBe Escapee.Status.ESCAPE

            escapableRow = 0
            escapableCol = 0
            blueEscapee1.moveToAnyWhere(Escapee.Position.of(escapableRow, escapableCol))
            blueEscapee1.escape()
            blueEscapee1.status shouldBe Escapee.Status.ESCAPE

            val notEscapableRow = 1
            val notEscapableCol = 4
            blueEscapee1.moveToAnyWhere(Escapee.Position.of(notEscapableRow, notEscapableCol))
            shouldThrow<CustomException> {
                blueEscapee1.escape()
            }
        }
    })
