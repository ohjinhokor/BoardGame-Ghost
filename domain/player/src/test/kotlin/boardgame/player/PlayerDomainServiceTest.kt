package boardgame.player

import boardgame.core.exception.CustomException
import boardgame.game.Game
import boardgame.game.GameDomainService
import boardgame.game.GameTestFixturesUtil
import boardgame.game.StubGameRepository
import boardgame.game.StubGameResultRepository
import boardgame.player.Player.Nickname
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

val testFixtures = PlayerTestFixturesUtil()
val gameTestFixturesUtil = GameTestFixturesUtil()
val gameDomainService = GameDomainService(StubGameRepository, StubGameResultRepository)
val domainService = PlayerDomainService(StubPlayerRepository, gameDomainService)

class PlayerDomainServiceFunSpecTest :
    FunSpec({
        test("createPlayer") {
            val nickname = Nickname("nickname")
            val player = domainService.createPlayer(PlayerDomainService.CreatePlayerCommand(nickname = nickname))
            player.nickname shouldBe nickname
        }

        test("updatePlayer") {
            val player = testFixtures.createPlayer()

            val newNickname = Nickname("nickname2")
            player.updateNickname(newNickname)
            player.nickname shouldBe newNickname
        }

        test("플레이어는 동일한 게임의 참가자로 중복될 수 없음") {
            val player = testFixtures.createPlayer()

            val game = gameTestFixturesUtil.createGame()
            joinGame(player, game)

            shouldThrow<CustomException> {
                joinGame(player, game)
            }
        }

        test("플레이어는 두개의 게임에 동시에 참여할 수 없음") {
            val player = testFixtures.createPlayer()

            val game = gameTestFixturesUtil.createGame()
            joinGame(player, game)

            val game2 = gameTestFixturesUtil.createGame()
            shouldThrow<CustomException> {
                joinGame(player, game2)
            }
        }

        test("게임 참가 인원은 최대 2명") {
            val player = testFixtures.createPlayer()
            val player2 = testFixtures.createPlayer()

            val game = gameTestFixturesUtil.createGame()
            joinGame(player, game)
            joinGame(player2, game)
            shouldThrow<CustomException> {
                joinGame(player2, game)
            }
        }

        test("게임 참가 성공") {
            val player = testFixtures.createPlayer()
            val game = gameTestFixturesUtil.createGame()
            joinGame(player, game)

            player.joinedGame shouldBe game
            player.status shouldBe Player.Status.JOINED

            val player2 = testFixtures.createPlayer()
            joinGame(player2, game)

            player2.joinedGame shouldBe game
            player2.status shouldBe Player.Status.JOINED
        }

        test("플레이어 한 명만 시작 요청") {
            val player = testFixtures.createPlayer()
            val game = gameTestFixturesUtil.createGame()
            joinGame(player, game)

            val player2 = testFixtures.createPlayer()
            joinGame(player2, game)

            domainService.startGame(player)
            player.status shouldBe Player.Status.READY
        }

        test("참가자가 두명인 경우 게임 시작 가능") {
            val player = testFixtures.createPlayer()
            val player2 = testFixtures.createPlayer()

            val game = gameTestFixturesUtil.createGame()
            joinGame(player, game)
            joinGame(player2, game)

            domainService.startGame(player)
            domainService.startGame(player2)

            game.status shouldBe Game.Status.PROGRESS
            player.status shouldBe Player.Status.PLAYING
            player2.status shouldBe Player.Status.PLAYING
        }
    })

class PlayerDomainServiceBehaviorSpecTest :
    BehaviorSpec({
        given("게임 생성") {
            val player1 = testFixtures.createPlayer()
            val player2 = testFixtures.createPlayer()
            val game = gameTestFixturesUtil.createGame()

            `when`("게임 참여 안하면") {
                then("게임 시작 못함") {
                    shouldThrow<CustomException> {
                        domainService.startGame(player1)
                    }
                }
            }

            and("게임 시작") {
                joinGame(player1, game)
                joinGame(player2, game)

                domainService.startGame(player1)
                domainService.startGame(player2)

                `when`("player1이 게임을 승리") {
                    domainService.win(winner = player1)

                    then("player의 winCount, loseCount가 update") {
                        val winner = StubPlayerRepository.findById(player1.id).get()
                        val loser = StubPlayerRepository.findById(player2.id).get()

                        winner.winCount shouldBe 1
                        winner.loseCount shouldBe 0

                        loser.winCount shouldBe 0
                        loser.loseCount shouldBe 1
                    }

                    then("게임 상태 저장") {
                        val savedGame = StubGameRepository.findById(game.id).get()
                        savedGame.status shouldBe Game.Status.END
                    }
                }
            }
        }
    })

fun joinGame(
    player: Player,
    game: Game,
) {
    domainService.joinGame(player, game)
    StubPlayerRepository.save(player)
}
