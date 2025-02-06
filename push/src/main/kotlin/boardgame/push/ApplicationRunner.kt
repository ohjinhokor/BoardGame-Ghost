package boardgame.push

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ApplicationRunner

fun main(args: Array<String>) {
    val application = SpringApplication(ApplicationRunner::class.java)
    application.run(*args)
}
