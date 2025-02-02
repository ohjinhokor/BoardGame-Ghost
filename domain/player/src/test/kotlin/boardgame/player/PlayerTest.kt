package boardgame.player

import boardgame.core.exception.CustomException
import boardgame.game.GameTestFixturesUtil
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PlayerTest :
    FunSpec({
        val testFixtures = PlayerTestFixturesUtil()
        val gameTestFixtureUtil = GameTestFixturesUtil()
        test("nickname 길이 테스트") {
            val nicknameLengthIsLonger = "this_is_too_long_nickname_for_this_board_game"
            shouldThrow<CustomException> {
                Player.Nickname(nicknameLengthIsLonger)
            }
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
            val game = gameTestFixtureUtil.createGame()
            player.joinGame(game)
            player.joinedGame shouldBe game

            player.quitGame()
            player.joinedGame shouldBe null
        }
    })
