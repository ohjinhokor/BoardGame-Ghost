package boardgame.game

import boardgame.exception.CustomException
import game.GameTestFixturesUtil
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import player.PlayerTestFixturesUtil

class GameTest :
    FunSpec({
        val gameTestFixturesUtil = GameTestFixturesUtil()
        val playerTestFixturesUtil = PlayerTestFixturesUtil()
        val player = playerTestFixturesUtil.createPlayer()

        test("Game 생성시 Status는 BEFORE_START") {
            val game = gameTestFixturesUtil.createGame(player)
            game.status shouldBe Game.Status.BEFORE_START
        }

        test("Game 생성 직후 참가자는 한 명") {
            val game = gameTestFixturesUtil.createGame(player)
            game.players.size shouldBe 1
        }

        test("같은 사람이 동일하게 참가자로 있을 수 없음") {
            val game = gameTestFixturesUtil.createGame(player)
            game.players.size shouldBe 1

            shouldThrow<CustomException> {
                game.addPlayer(player)
            }
        }

        test("참가자 2명인 경우") {
            val game = gameTestFixturesUtil.createGame(player)
            game.players.size shouldBe 1

            val player2 = playerTestFixturesUtil.createPlayer()
            game.addPlayer(player2)
            game.players.size shouldBe 2
        }

        test("참가자는 2명 이상 불가능 하다.") {
            val game = gameTestFixturesUtil.createGame(player)
            game.players.size shouldBe 1

            val player2 = playerTestFixturesUtil.createPlayer()
            game.addPlayer(player2)
            game.players.size shouldBe 2

            val player3 = playerTestFixturesUtil.createPlayer()
            shouldThrow<CustomException> {
                game.addPlayer(player3)
            }
        }

        test("게임 시작은 참가자가 2명인 경우 가능하다") {
            val game = gameTestFixturesUtil.createGame(player)
            game.players.size shouldBe 1

            shouldThrow<CustomException> {
                game.start()
            }

            val player2 = playerTestFixturesUtil.createPlayer()
            game.addPlayer(player2)

            game.start()
            game.status shouldBe Game.Status.PROGRESS
        }

        test("게임이 시작하지 않은경우, 끝낼 수 없다.") {
            val game = gameTestFixturesUtil.createGame(player)
            game.addPlayer(playerTestFixturesUtil.createPlayer())

            shouldThrow<CustomException> {
                game.endWith(player)
            }
        }

        test("게임이 끝나면 우승자가 저장된다") {
            val game = gameTestFixturesUtil.createGame(player)
            game.addPlayer(playerTestFixturesUtil.createPlayer())
            val playerWinCount = player.winCount

            game.start()
            game.endWith(player)
            game.winner shouldBe player
            game.winner?.winCount shouldBe playerWinCount + 1
        }
    })
