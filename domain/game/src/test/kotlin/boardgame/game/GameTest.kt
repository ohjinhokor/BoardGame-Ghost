package boardgame.game

import boardgame.core.exception.CustomException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GameTest :
    FunSpec({
        val gameTestFixturesUtil = GameTestFixturesUtil()

        test("Game 생성시 Status는 BEFORE_START") {
            val game = gameTestFixturesUtil.createGame()
            game.status shouldBe Game.Status.BEFORE_START
        }

        test("게임 시작 후 Status는 PROGRESS") {
            val game = gameTestFixturesUtil.createGame()
            game.start()
            game.status shouldBe Game.Status.PROGRESS
        }

        test("게임 Status가 BEFORE_START가 아니면, 게임을 끝낼 수 없다") {
            val game = gameTestFixturesUtil.createGame()
            game.start()
            game.end()

            shouldThrow<CustomException> {
                game.start()
            }
        }

        test("게임을 끝내면, Status는 END") {
            val game = gameTestFixturesUtil.createGame()
            game.start()
            game.end()
            game.status shouldBe Game.Status.END
        }

        test("게임이 시작하지 않은경우, 끝낼 수 없다") {
            val game = gameTestFixturesUtil.createGame()
            shouldThrow<CustomException> {
                game.end()
            }
        }
    })
