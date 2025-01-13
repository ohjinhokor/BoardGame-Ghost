package escapee

import boardgame.escapee.Escapee

/*
 Test Fixtures Utils
 원래는 1칸씩만 움직일 수 있으나, test를 위해서 어디로나 움직일 수 있도록
 */
fun Escapee.moveToAnyWhere(position: Escapee.Position) {
    update(position = position)
}
