package boardgame.entitybase

import boardgame.exception.CustomException
import boardgame.exception.HttpStatus
import ulid.ULID

@JvmInline
value class BinaryId(
    val value: ByteArray,
) : Comparable<BinaryId> {
    companion object {
        fun new(): BinaryId = BinaryId(ULID.nextULID().toBytes())

        fun fromString(value: String): BinaryId = BinaryId(ULID.parseULID(value).toBytes())
    }

    init {
        runCatching { ULID.fromBytes(value).toBytes() }.getOrNull()
            ?: throw CustomException("Invalid ULID payload", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    override fun toString(): String = ULID.fromBytes(value).toString()

    fun toHexString(): String = "0x${this.value.joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }}"

    override fun compareTo(other: BinaryId): Int = java.util.Arrays.compare(this.value, other.value)
}
