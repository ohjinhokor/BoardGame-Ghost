package boardgame.util

fun <T> T?.shouldSuccess(): T {
    if (this == null) throw AssertionError()
    return this
}
