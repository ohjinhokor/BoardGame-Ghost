package boardgame.game

import StubGameRepository
import core.shouldSuccess
import game.GameTestFixturesUtil
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import player.PlayerTestFixturesUtil

val stubGameRepository = StubGameRepository()
val gameDomainService = GameDomainService()
val gameApplicationService =
    GameApplicationService(
        gameRepository = stubGameRepository,
        gameDomainService = gameDomainService,
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

            val savedGame = stubGameRepository.getOrNull(game.id).shouldSuccess()
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
            stubGameRepository.save(game)

            `when`("새로운 사용자를 추가하면") {
                val newPlayerNickname = "new"
                val newPlayer = playerTestFixturesUtil.createPlayerWithNickname(newPlayerNickname)

                gameApplicationService.addPlayer(
                    id = game.id,
                    player = newPlayer,
                )

                then("게임 참가자가 추가된다") {
                    val savedGame = stubGameRepository.getOrNull(game.id).shouldSuccess()
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
