package boardgame.player

import boardgame.player.Player.Nickname
import boardgame.player.PlayerDomainService.UpdatePlayerCommand
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import player.PlayerTestFixturesUtil

class PlayerDomainServiceTest :
    FunSpec({
        val testFixtures = PlayerTestFixturesUtil()
        val domainService = PlayerDomainService()

        test("createPlayer") {
            val nickname = Nickname("nickname")
            val player = domainService.createPlayer(PlayerDomainService.CreatePlayerCommand(nickname = nickname))
            player.nickname shouldBe nickname
        }

        test("updatePlayer") {
            val player = testFixtures.createPlayer()

            val newNickname = Nickname("nickname2")
            domainService.updatePlayer(player, UpdatePlayerCommand(nickname = newNickname))
            player.nickname shouldBe newNickname
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
    })
