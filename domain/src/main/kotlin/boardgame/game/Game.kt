package boardgame.game

import boardgame.entitybase.BinaryId
import boardgame.entitybase.EntityBase
import boardgame.exception.CustomException
import boardgame.exception.HttpStatus
import boardgame.player.Player
import java.time.LocalDateTime

class Game internal constructor(
    id: BinaryId,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    val title: Title,
    val gameCreator: Player,
) : EntityBase(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
) {
    private val _players: MutableList<Player> = mutableListOf()
    val players: List<Player>
        get() = _players.toList()
    var status = Status.BEFORE_START
        private set
    var winner: Player? = null
        private set

    init {
        _players.add(this.gameCreator)
    }

    fun start() {
        if (this.status != Status.BEFORE_START) {
            throw CustomException("게임을 시작할 수 없습니다", HttpStatus.BAD_REQUEST)
        }
        update(status = Status.PROGRESS)
    }

    fun end(winner: Player) {
        if (this.status != Status.PROGRESS) {
            throw CustomException("시작하지 않은 게임입니다", HttpStatus.BAD_REQUEST)
        }
        update(status = Status.END, winner = winner)
    }

    private fun update(
        status: Status? = null,
        winner: Player? = null,
    ) {
        status?.let {
            if (players.size < 2) {
                throw CustomException("게임 참가자가 2명이 아닙니다", HttpStatus.BAD_REQUEST)
            }
            this.status = it
        }
        winner?.let {
            if (this.status != Status.END) {
                throw CustomException("게임이 끝나지 않았습니다", HttpStatus.BAD_REQUEST)
            }
            this.winner = it
        }
    }

    internal fun addPlayer(player: Player) {
        if (_players.contains(player)) {
            throw CustomException("이미 게임에 참여한 참가자입니다.", HttpStatus.BAD_REQUEST)
        }
        if (_players.size >= 2) {
            throw CustomException("게임 참가자는 최대 2명입니다", HttpStatus.BAD_REQUEST)
        }
        _players.add(player)
    }

    @JvmInline
    value class Title(
        val value: String,
    ) {
        init {
            if (this.value.length > 20) {
                throw CustomException("방 제목은 20글자를 넘을 수 없습니다", HttpStatus.BAD_REQUEST)
            }
        }
    }

    enum class Status {
        BEFORE_START,
        END,
        PROGRESS,
    }
}
