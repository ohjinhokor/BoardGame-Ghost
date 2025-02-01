package boardgame.game

import boardgame.core.entitybase.BinaryId
import boardgame.util.shouldSuccess
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

val testUtils = GameTestFixturesUtil()
val gameDomainService = GameDomainService(StubGameRepository, StubGameResultRepository)

class GameDomainServiceTestFunSpec :
    FunSpec(
        {
            test("start Game") {
                val game = testUtils.createGame()

                gameDomainService.startGame(game.id)

                val savedGame = StubGameRepository.getOrNull(game.id).shouldSuccess()
                savedGame.status shouldBe Game.Status.PROGRESS
            }
        },
    )

class GameDomainServiceTestBehaviorSpec :
    BehaviorSpec({
        Given("게임 생성, 게임 시작 후") {
            val game = testUtils.createGame()

            val winnerId = BinaryId.new()
            val loserId = BinaryId.new()
            game.start()

            `when`("게임이 종료되면") {
                gameDomainService.end(
                    GameDomainService.EndCommand(
                        game = game,
                        winnerId = winnerId,
                        loserId = loserId,
                    ),
                )

                then("게임 결과가 저장된다") {
                    val savedGame = StubGameRepository.findById(game.id)
                    val savedGameResult = StubGameResultRepository.findByGame(game)

                    savedGame.get().status shouldBe Game.Status.END
                    savedGameResult.get().winnerId shouldBe winnerId
                    savedGameResult.get().loserId shouldBe loserId
                }
            }
        }
    })
