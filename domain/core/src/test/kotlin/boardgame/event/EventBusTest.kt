package boardgame.event

import boardgame.core.event.Event
import boardgame.core.event.EventBus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TestEvent(
    val number: Int
) : Event

class AnotherEvent(
    val number: Int
) : Event

class EventBusTest : FunSpec({
    test("이벤트 1개 발행 테스트") {
        EventBus.publish(TestEvent(1))

        var changeableNumber = 0
        EventBus.receive<TestEvent> {
            changeableNumber = it.number
        }
        Thread.sleep(500)

        changeableNumber shouldBe 1
    }

    test("이벤트 2개 발행 테스트") {
        EventBus.publish(TestEvent(1))

        var changeableNumber1 = 0
        EventBus.receive<TestEvent> {
            changeableNumber1 = it.number
        }

        var changeableNumber2 = 0
        EventBus.receive<TestEvent> {
            changeableNumber2 = it.number
            changeableNumber2++
        }

        Thread.sleep(500)

        changeableNumber1 shouldBe 1
        changeableNumber2 shouldBe 2
    }

    test("다른 종류의 이벤트는 받지 않는다") {
        EventBus.publish(AnotherEvent(1))

        var changeableNumber = 0
        // 아래 코드는 실행되지 않아야 함
        EventBus.receive<TestEvent> {
            changeableNumber = it.number
        }

        Thread.sleep(500)
        changeableNumber shouldBe 0
    }
})