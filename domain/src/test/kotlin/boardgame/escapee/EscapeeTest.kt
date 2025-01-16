package boardgame.escapee

import boardgame.exception.CustomException
import escapee.EscapeeTestFixturesUtil
import escapee.moveToAnyWhere
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import player.PlayerTestFixturesUtil

class EscapeeTest :
    FunSpec({
        val playerTestFixturesUtil: PlayerTestFixturesUtil = PlayerTestFixturesUtil()
        val escapeeDomainService: EscapeeDomainService = EscapeeDomainService()
        val escapeeTestFixturesUtil: EscapeeTestFixturesUtil = EscapeeTestFixturesUtil()

        test("처음 Escapee를 만들때, 정해진 위치 외에는 만들 수 없음") {
            val player = playerTestFixturesUtil.createPlayer()
            var outOfRangeRow = 6
            shouldThrow<CustomException> {
                escapeeDomainService.createBlueEscapees(
                    EscapeeDomainService.CreateEscapeesCommand(
                        positions =
                            listOf(
                                Escapee.Position.of(outOfRangeRow, 1),
                                Escapee.Position.of(5, 1),
                                Escapee.Position.of(5, 2),
                                Escapee.Position.of(5, 3),
                            ),
                        player = player,
                    ),
                )
            }

            outOfRangeRow = -1
            shouldThrow<CustomException> {
                escapeeDomainService.createBlueEscapees(
                    EscapeeDomainService.CreateEscapeesCommand(
                        positions =
                            listOf(
                                Escapee.Position.of(outOfRangeRow, 1),
                                Escapee.Position.of(6, 1),
                                Escapee.Position.of(5, 1),
                                Escapee.Position.of(5, 2),
                            ),
                        player = player,
                    ),
                )
            }

            val notStartPositionRow = 3
            shouldThrow<CustomException> {
                escapeeDomainService.createBlueEscapees(
                    EscapeeDomainService.CreateEscapeesCommand(
                        positions =
                            listOf(
                                Escapee.Position.of(notStartPositionRow, 1),
                                Escapee.Position.of(6, 1),
                                Escapee.Position.of(5, 1),
                                Escapee.Position.of(5, 2),
                            ),
                        player = player,
                    ),
                )
            }
        }

        test("최대 이동 거리 제한이 있음") {
            val player = playerTestFixturesUtil.createPlayer()

            val startRow = 5
            val startCol = 1
            val blueEscapee =
                escapeeTestFixturesUtil.createBlueEscapee(
                    position = Escapee.Position.of(startRow, startCol),
                    player = player,
                )

            shouldThrow<CustomException> {
                blueEscapee.moveTo(Escapee.Position.of(startRow - 1, startCol + 1))
            }
            shouldThrow<CustomException> {
                blueEscapee.moveTo(Escapee.Position.of(startRow + 2, startCol))
            }
        }

        test("탈출 가능 위치에 제한이 있음") {
            val player = playerTestFixturesUtil.createPlayer()

            val startRow = 5
            val startCol = 1
            val blueEscapee1 =
                escapeeTestFixturesUtil.createBlueEscapee(
                    position = Escapee.Position.of(startRow, startCol),
                    player = player,
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
