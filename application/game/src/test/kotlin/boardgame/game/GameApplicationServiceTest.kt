package boardgame.game

import boardgame.core.entitybase.BinaryId
import boardgame.core.event.EventBus
import boardgame.util.shouldSuccess
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

val gameDomainService = GameDomainService(StubGameRepository, StubGameResultRepository)
val gameApplicationService =
    GameApplicationService(
        gameRepository = StubGameRepository,
        gameDomainService = gameDomainService,
    )

class GameApplicationServiceTest :
    FunSpec({
        test("create Game") {
            val gameTitle = "Game Title"
            val creatorId = BinaryId.new()

            val game = gameApplicationService.createGame(creatorId, gameTitle)
            val savedGame = StubGameRepository.getOrNull(game.id).shouldSuccess()

            savedGame shouldBe game
            savedGame.title.value shouldBe gameTitle
            savedGame.status shouldBe Game.Status.BEFORE_START

            var eventReceive = false
            EventBus.receive<GameApplicationService.CreateGameEvent> {
                eventReceive = true
            }

            Thread.sleep(500)
            eventReceive shouldBe true
        }
    })
