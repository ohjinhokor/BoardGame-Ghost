package boardgame.player

import boardgame.core.entitybase.BinaryId
import boardgame.player.Player.Nickname
import java.time.LocalDateTime

class PlayerTestFixturesUtil {
    fun createPlayer() =
        StubPlayerRepository.save(
            Player(
                id = BinaryId.new(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                nickname = Nickname("nickname"),
                joinedGame = null,
                status = Player.Status.NONE,
            ),
        )

    fun createPlayerWithNickname(nickname: String) =
        StubPlayerRepository.save(
            Player(
                id = BinaryId.new(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                nickname = Nickname(nickname),
                joinedGame = null,
                status = Player.Status.NONE,
            ),
        )
}
