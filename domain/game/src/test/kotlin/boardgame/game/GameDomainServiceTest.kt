package boardgame.game

import boardgame.core.entitybase.BinaryId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GameDomainServiceTest :
    FunSpec({
        val testUtils = GameTestFixturesUtil()
        val gameDomainService = GameDomainService()
        test("게임을 끝내면, 게임 결과를 반환한다") {
            val game = testUtils.createGame()

            val winnerId = BinaryId.new()
            val loserId = BinaryId.new()
            game.start()

            val result =
                gameDomainService.end(
                    GameDomainService.EndCommand(
                        game = game,
                        winnerId = winnerId,
                        loserId = loserId,
                    ),
                )

            result.first.status shouldBe Game.Status.END
            result.second.winnerId shouldBe winnerId
            result.second.loserId shouldBe loserId
        }
    })
