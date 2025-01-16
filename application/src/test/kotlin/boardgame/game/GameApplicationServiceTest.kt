package boardgame.game

import boardgame.core.shouldSuccess
import boardgame.escapee.EscapeeApplicationService
import boardgame.escapee.EscapeeDomainService
import boardgame.escapee.StubEscapeeRepository
import boardgame.player.PlayerApplicationService
import boardgame.player.PlayerDomainService
import boardgame.player.PlayerTestFixturesUtil
import boardgame.player.StubPlayerRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

val gameDomainService = GameDomainService()
val playerDomainService = PlayerDomainService()
val escapeeDomainService = EscapeeDomainService()
val escapeeApplicationService = EscapeeApplicationService(StubEscapeeRepository, escapeeDomainService)
val playerApplicationService =
    PlayerApplicationService(playerDomainService, StubPlayerRepository, escapeeApplicationService)
val gameApplicationService =
    GameApplicationService(
        gameRepository = StubGameRepository,
        gameDomainService = gameDomainService,
        playerApplicationService = playerApplicationService
    )
val playerTestFixturesUtil = PlayerTestFixturesUtil()
val gameTestFixturesUtil = GameTestFixturesUtil()

class GameApplicationServiceTestFunSpec :
    FunSpec({
        test("create Game") {
            val gameTitle = "Game Title"
            val gameCreator = playerTestFixturesUtil.createPlayer()

            val game =
                gameApplicationService.createGame(
                    GameDomainService.CreateGameCommand(
                        title = Game.Title(gameTitle),
                        gameCreator = gameCreator,
                    ),
                )

            val savedGame = StubGameRepository.getOrNull(game.id).shouldSuccess()
            savedGame shouldBe game
            savedGame.gameCreator.id shouldBe game.gameCreator.id
            savedGame.title.value shouldBe gameTitle
        }
    })

class GameApplicationServiceTestBehaviorSpec :
    BehaviorSpec({
        given("게임이 생성되어있고") {
            val playerNickname = "creator"
            val player = playerTestFixturesUtil.createPlayerWithNickname(playerNickname)
            val game = gameTestFixturesUtil.createGame(player)
            StubGameRepository.save(game)

            `when`("새로운 사용자를 추가하면") {
                val newPlayerNickname = "new"
                val newPlayer = playerTestFixturesUtil.createPlayerWithNickname(newPlayerNickname)

                gameApplicationService.addPlayer(
                    id = game.id,
                    player = newPlayer,
                )

                then("게임 참가자가 추가된다") {
                    val savedGame = StubGameRepository.getOrNull(game.id).shouldSuccess()
                    savedGame.players.size shouldBe 2
                    savedGame.players.map { it.nickname.value } shouldBe listOf(playerNickname, newPlayerNickname)
                }

                and("게임을 시작하면") {
                    gameApplicationService.startGame(game.id)

                    then("게임이 시작된다") {
                        game.status shouldBe Game.Status.PROGRESS
                    }
                }
            }
        }
    })
