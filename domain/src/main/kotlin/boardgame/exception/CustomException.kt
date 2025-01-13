package boardgame.exception

enum class HttpStatus {
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR,
}

class CustomException(
    message: String,
    httpStatus: HttpStatus,
) : RuntimeException()
