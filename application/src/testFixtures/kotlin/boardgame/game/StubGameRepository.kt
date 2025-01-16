package boardgame.game

import boardgame.StubRepository

object StubGameRepository :
    StubRepository<Game>(),
    GameRepository
