package boardgame.escapee

import boardgame.escapee.Escapee.Status.ESCAPE
import boardgame.game.Game
import boardgame.game.Game.Status.END
import boardgame.game.GameApplicationService
import boardgame.game.GameDomainService
import boardgame.game.StubGameRepository
import boardgame.player.PlayerApplicationService
import boardgame.player.PlayerDomainService
import boardgame.player.PlayerTestFixturesUtil
import boardgame.player.StubPlayerRepository
import boardgame.player.createPlayerAndSave
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class EscapeeApplicationServiceTest : BehaviorSpec({
    val escapeeDomainService = EscapeeDomainService()
    val escapeeApplicationService = EscapeeApplicationService(StubEscapeeRepository, escapeeDomainService)
    val playerTestFixturesUtil = PlayerTestFixturesUtil()
    val gameDomainService = GameDomainService()
    val playerDomainService = PlayerDomainService()
    val playerApplicationService =
        PlayerApplicationService(
            playerDomainService,
            StubPlayerRepository,
            escapeeApplicationService
        )
    val gameApplicationService = GameApplicationService(StubGameRepository, gameDomainService, playerApplicationService)
    given("게임 시작") {
        val player = playerTestFixturesUtil.createPlayerAndSave()
        val player2 = playerTestFixturesUtil.createPlayer()
        val game = gameApplicationService.createGame(
            GameDomainService.CreateGameCommand(title = Game.Title("게임"), gameCreator = player)
        )
        gameDomainService.addPlayer(GameDomainService.AddPlayerCommand(game, player2))
        game.start()

        and("탈출자를 생성하고") {
            playerApplicationService.addEscapees(
                playerId = player.id,
                bluePositions =
                listOf(
                    Escapee.Position.of(5, 1),
                    Escapee.Position.of(5, 1),
                    Escapee.Position.of(5, 2),
                    Escapee.Position.of(5, 3),
                ),
                redPositions =
                listOf(
                    Escapee.Position.of(4, 1),
                    Escapee.Position.of(4, 1),
                    Escapee.Position.of(4, 2),
                    Escapee.Position.of(4, 3),
                ),
            )
            val blueEscapee = player.escapees.filterIsInstance<BlueEscapee>().first()

            and("탈출 가능한 위치로 이동한 후") {
                blueEscapee.moveToAnyWhere(
                    Escapee.Position.of(ESCAPABLE_ROW, ESCAPABLE_COLUMN),
                )

                and("탈출자가 탈출하면") {
                    escapeeApplicationService.escape(blueEscapee.id)
                    Thread.sleep(5000)

                    then("탈출자의 상태가 바뀌어야 한다") {
                        blueEscapee.status shouldBe ESCAPE
                    }

                    then("게임이 끝났어야 한다") {
                        game.status shouldBe END
                    }

                    then("게임의 승리자가 정해져야 한다") {
                        game.winner shouldBe player
                    }

                    then("승자는 winCount가 올라간다") {
                        player.winCount shouldBe 1
                        player.loseCount shouldBe 0
                    }

                    then("패자 winCount가 올라간다") {
                        player2.winCount shouldBe 0
                        player2.loseCount shouldBe 1
                    }
                }
            }
        }
    }
})