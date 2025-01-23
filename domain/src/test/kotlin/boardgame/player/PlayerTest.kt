package boardgame.player

import boardgame.escapee.Escapee.Position
import boardgame.escapee.EscapeeDomainService
import boardgame.exception.CustomException
import boardgame.game.GameTestFixturesUtil
import boardgame.player.Player.Companion.BLUE_ESCAPEE_SIZE
import boardgame.player.Player.Companion.RED_ESCAPEE_SIZE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PlayerTest :
    FunSpec({
        val testFixtures = PlayerTestFixturesUtil()
        val escapeeDomainService = EscapeeDomainService()
        val gameTestFixtureUtil = GameTestFixturesUtil()
        test("nickname 길이 테스트") {
            val nicknameLengthIsLonger = "this_is_too_long_nickname_for_this_board_game"
            shouldThrow<CustomException> {
                Player.Nickname(nicknameLengthIsLonger)
            }

            test("player win") {
                val player = testFixtures.createPlayer()
                val before = player.winCount
                player.win()
                val after = player.winCount
                after shouldBe before + 1
            }

            test("player lose") {
                val player = testFixtures.createPlayer()
                val before = player.loseCount
                player.lose()
                val after = player.loseCount
                after shouldBe before + 1
            }

            test("player join game") {
                val player = testFixtures.createPlayer()
                val game = gameTestFixtureUtil.createGame(player)
                player.joinGame(game)
                player.joinedGame shouldBe game

                player.quitGame()
                player.joinedGame shouldBe null
            }

            test("할당하는 빨간색 탈출자의 size가 ${RED_ESCAPEE_SIZE}가 아니면 예외처리") {
                val player = testFixtures.createPlayer()

                val blueEscapees =
                    escapeeDomainService.createBlueEscapees(
                        EscapeeDomainService.CreateEscapeesCommand(
                            positions =
                            listOf(
                                Position.of(4, 1),
                                Position.of(4, 2),
                                Position.of(4, 3),
                                Position.of(4, 4),
                            ),
                            player = player,
                        ),
                    )

                val redEscapees =
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

                shouldThrow<CustomException> {
                    player.addEscapee(redEscapees, blueEscapees)
                }
            }

            test("할당하는 파란색 탈출자의 size가 ${BLUE_ESCAPEE_SIZE}가 아니면 예외처리") {
                val player = testFixtures.createPlayer()

                val blueEscapees =
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

                val redEscapees =
                    escapeeDomainService.createRedEscapees(
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

                shouldThrow<CustomException> {
                    player.addEscapee(redEscapees, blueEscapees)
                }
            }

            test("이미 Escapees를 할당한 player에게 다시 Escapees를 할당하면 예외처리") {
                val player = testFixtures.createPlayer()

                val blueEscapees =
                    escapeeDomainService.createBlueEscapees(
                        EscapeeDomainService.CreateEscapeesCommand(
                            positions =
                            listOf(
                                Position.of(4, 1),
                                Position.of(4, 2),
                                Position.of(4, 3),
                                Position.of(4, 4),
                            ),
                            player = player,
                        ),
                    )

                val redEscapees =
                    escapeeDomainService.createRedEscapees(
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
                player.addEscapee(redEscapees, blueEscapees)

                val newRedEscapees =
                    escapeeDomainService.createRedEscapees(
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

                shouldThrow<CustomException> {
                    player.addEscapee(newRedEscapees, blueEscapees)
                }
            }
        }
    })

