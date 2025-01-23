package boardgame.player

import boardgame.player.Player.Nickname
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

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
            player.updateNickname(newNickname)
            player.nickname shouldBe newNickname
        }
    })
