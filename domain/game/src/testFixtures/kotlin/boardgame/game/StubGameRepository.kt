package boardgame.game

import boardgame.stub.StubRepository

object StubGameRepository : StubRepository<Game>(), GameRepository
