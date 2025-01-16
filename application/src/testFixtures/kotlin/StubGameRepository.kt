import boardgame.StubRepository
import boardgame.game.Game
import boardgame.game.GameRepository

class StubGameRepository :
    StubRepository<Game>(),
    GameRepository
