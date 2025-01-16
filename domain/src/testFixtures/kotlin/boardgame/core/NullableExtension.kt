package boardgame.core

fun <T> T?.shouldSuccess(): T {
    if (this == null) throw AssertionError()
    return this
}
