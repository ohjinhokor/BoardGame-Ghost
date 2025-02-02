package boardgame.player

import boardgame.core.exception.CustomException
import boardgame.game.GameApplicationService
import boardgame.game.GameDomainService
import boardgame.game.GameTestFixturesUtil
import boardgame.game.StubGameRepository
import boardgame.game.StubGameResultRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PlayerApplicationTest :
    FunSpec(
        {
            val playerTestFixturesUtil = PlayerTestFixturesUtil()
            val gameDomainService = GameDomainService(StubGameRepository, StubGameResultRepository)
            val gameApplicationService = GameApplicationService(StubGameRepository, gameDomainService)
            val playerDomainService = PlayerDomainService(StubPlayerRepository, gameDomainService)
            val playerApplicationService =
                PlayerApplicationService(playerDomainService, StubPlayerRepository, gameApplicationService)
            val gameTestFixturesUtil = GameTestFixturesUtil()

            test("이미 참가한 게임이 있으면, 다른 게임에 참가할 수 없다") {
                val player = playerTestFixturesUtil.createPlayer()
                val gameTitle = "title"
                val game = gameApplicationService.createGame(player.id, gameTitle)

                val player2 = playerTestFixturesUtil.createPlayer()
                playerApplicationService.joinGame(player2, game.id)

                val game2 = gameTestFixturesUtil.createGame()
                shouldThrow<CustomException> {
                    playerApplicationService.joinGame(player2, game2.id)
                }
            }

            test("참가자는 2명을 넘길 수 없다") {
                val player = playerTestFixturesUtil.createPlayer()
                val gameTitle = "title"
                val game = gameApplicationService.createGame(player.id, gameTitle)

                val player2 = playerTestFixturesUtil.createPlayer()
                playerApplicationService.joinGame(player2, game.id)

                val player3 = playerTestFixturesUtil.createPlayer()
                shouldThrow<CustomException> {
                    playerApplicationService.joinGame(player3, game.id)
                }
            }

            test("참가한 게임이 없는 경우 게임을 시작할 수 없다") {
                val player = playerTestFixturesUtil.createPlayer()
                shouldThrow<CustomException> {
                    playerApplicationService.startGame(player.id)
                }
            }

            test("참가자가 혼자인 경우 게임을 시작할 수 없다") {
                val player = playerTestFixturesUtil.createPlayer()
                val gameTitle = "title"
                val game = gameApplicationService.createGame(player.id, gameTitle)

                shouldThrow<CustomException> {
                    playerApplicationService.startGame(player.id)
                }
            }

            test("참가자가 두명인 경우 게임을 시작할 수 없다") {
                val player = playerTestFixturesUtil.createPlayer()
                val gameTitle = "title"
                val game = gameApplicationService.createGame(player.id, gameTitle)

                val player2 = playerTestFixturesUtil.createPlayer()
                playerApplicationService.joinGame(player2, game.id)

                playerApplicationService.startGame(player.id)
                StubPlayerRepository.getOrException(player.id).status shouldBe Player.Status.READY

                playerApplicationService.startGame(player2.id)
                StubPlayerRepository.getOrException(player.id).status shouldBe Player.Status.PLAYING
                StubPlayerRepository.getOrException(player2.id).status shouldBe Player.Status.PLAYING
            }
        },
    )
