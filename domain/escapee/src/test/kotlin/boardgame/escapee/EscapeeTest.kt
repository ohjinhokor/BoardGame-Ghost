package boardgame.escapee

import boardgame.core.exception.CustomException
import boardgame.escapee.Escapee.Position
import boardgame.game.GameDomainService
import boardgame.game.GameTestFixturesUtil
import boardgame.game.StubGameRepository
import boardgame.game.StubGameResultRepository
import boardgame.player.Player
import boardgame.player.PlayerDomainService
import boardgame.player.PlayerTestFixturesUtil
import boardgame.player.StubPlayerRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class EscapeeTest :
    FunSpec({
        val playerTestFixturesUtil = PlayerTestFixturesUtil()
        val escapeeDomainService = EscapeeDomainService(StubEscapeeRepository)
        val escapeeTestFixturesUtil = EscapeeTestFixturesUtil()
        val gameTestFixturesUtil = GameTestFixturesUtil()
        val gameDomainService = GameDomainService(StubGameRepository, StubGameResultRepository)
        val playerDomainService = PlayerDomainService(StubPlayerRepository, gameDomainService)

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

        test("할당하는 빨간색 탈출자의 size가 4가 아니면 예외처리") {
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

        test("할당하는 파란색 탈출자의 size가 4가 아니면 예외처리") {
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

        test("탈출자가 탈출하면 승자와 패자 후처리(win count, lose count 변경)") {

            // player 생성, game 생성
            val player = playerTestFixturesUtil.createPlayerWithNickname("test")
            val player2 = playerTestFixturesUtil.createPlayer()
            val game = gameTestFixturesUtil.createGame()

            // game 참가 후 저장
            playerDomainService.joinGame(player, game)
            playerDomainService.joinGame(player2, game)
            StubPlayerRepository.save(player)
            StubPlayerRepository.save(player2)

            // 탈출인 생성
            val blueEscapees =
                escapeeDomainService.createBlueEscapees(
                    EscapeeDomainService.CreateEscapeesCommand(
                        positions =
                            listOf(
                                Position.of(5, 1),
                                Position.of(5, 2),
                                Position.of(5, 3),
                                Position.of(5, 4),
                            ),
                        player = player,
                    ),
                )

            // 탈출인 탈출
            val escapee = blueEscapees.first()
            escapee.moveToEscapablePosition()
            escapee.escape()

            Thread.sleep(1000)

            val savedPlayer = StubPlayerRepository.findById(player.id).get()
            val savedPlayer2 = StubPlayerRepository.findById(player2.id).get()

            println(savedPlayer.nickname)
            println(savedPlayer2.nickname)

            savedPlayer.status shouldBe Player.Status.NONE
            savedPlayer2.status shouldBe Player.Status.NONE

            savedPlayer.winCount shouldBe 1
            savedPlayer.loseCount shouldBe 0

            savedPlayer2.winCount shouldBe 0
            savedPlayer2.loseCount shouldBe 1
        }
    })
