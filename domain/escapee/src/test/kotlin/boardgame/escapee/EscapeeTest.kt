package boardgame.escapee

import boardgame.core.exception.CustomException
import boardgame.escapee.Escapee.Position
import boardgame.player.PlayerTestFixturesUtil
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class EscapeeTest :
    FunSpec({
        val playerTestFixturesUtil = PlayerTestFixturesUtil()
        val escapeeDomainService = EscapeeDomainService(StubEscapeeRepository)
        val escapeeTestFixturesUtil = EscapeeTestFixturesUtil()

        test("처음 Escapee를 만들때, 정해진 위치 외에는 만들 수 없음") {
            val player = playerTestFixturesUtil.createPlayer()
            var outOfRangeRow = 6
            shouldThrow<CustomException> {
                escapeeDomainService.createBlueEscapees(
                    EscapeeDomainService.CreateEscapeesCommand(
                        positions =
                            listOf(
                                Position.of(outOfRangeRow, 1),
                                Position.of(5, 1),
                                Position.of(5, 2),
                                Position.of(5, 3),
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
                                Position.of(outOfRangeRow, 1),
                                Position.of(6, 1),
                                Position.of(5, 1),
                                Position.of(5, 2),
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
                                Position.of(notStartPositionRow, 1),
                                Position.of(6, 1),
                                Position.of(5, 1),
                                Position.of(5, 2),
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
                    position = Position.of(startRow, startCol),
                    player = player,
                )

            shouldThrow<CustomException> {
                blueEscapee.moveTo(Position.of(startRow - 1, startCol + 1))
            }
            shouldThrow<CustomException> {
                blueEscapee.moveTo(Position.of(startRow + 2, startCol))
            }
        }

        test("탈출 가능 위치에 제한이 있음") {
            val player = playerTestFixturesUtil.createPlayer()

            val startRow = 5
            val startCol = 1
            val blueEscapee1 =
                escapeeTestFixturesUtil.createBlueEscapee(
                    position = Position.of(startRow, startCol),
                    player = player,
                )

            var escapableRow = 0
            var escapableCol = 5
            blueEscapee1.moveToAnyWhere(Position.of(escapableRow, escapableCol))
            blueEscapee1.escape()
            blueEscapee1.status shouldBe Escapee.Status.ESCAPE

            val blueEscapee2 =
                escapeeTestFixturesUtil.createBlueEscapee(
                    position = Position.of(startRow, startCol),
                    player = player,
                )
            escapableRow = 0
            escapableCol = 0
            blueEscapee2.moveToAnyWhere(Position.of(escapableRow, escapableCol))
            blueEscapee2.escape()
            blueEscapee2.status shouldBe Escapee.Status.ESCAPE

            val notEscapableRow = 1
            val notEscapableCol = 4
            blueEscapee1.moveToAnyWhere(Position.of(notEscapableRow, notEscapableCol))
            shouldThrow<CustomException> {
                blueEscapee1.escape()
            }
        }

        test("할당하는 빨간색 탈출자의 size가 ${INITIAL_RED_ESCAPEE_COUNT}가 아니면 예외처리") {
            val player = playerTestFixturesUtil.createPlayer()
            shouldThrow<CustomException> {
                escapeeDomainService.createRedEscapees(
                    EscapeeDomainService.CreateEscapeesCommand(
                        positions =
                            listOf(
                                Position.of(5, 1),
                                Position.of(5, 2),
                                Position.of(5, 3),
                            ),
                        player = player,
                    ),
                )
            }
        }
        test("할당하는 파란색 탈출자의 size가 ${INITIAL_BLUE_ESCAPEE_COUNT}가 아니면 예외처리") {
            val player = playerTestFixturesUtil.createPlayer()
            shouldThrow<CustomException> {
                escapeeDomainService.createBlueEscapees(
                    EscapeeDomainService.CreateEscapeesCommand(
                        positions =
                            listOf(
                                Position.of(4, 1),
                                Position.of(4, 2),
                                Position.of(4, 3),
                            ),
                        player = player,
                    ),
                )
            }
        }
    })
