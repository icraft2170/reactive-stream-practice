package me.hero.tobyreactive.tv05

import java.util.*
import java.util.concurrent.Executors

/**
 * Duality ( 쌍대성 )
 * - Observer <---> Iterable
 *  - Iterable 은 Pull(당기는) 방식
 *  - Observer 은 Push(밀어 넣는) 방식
 *
 * Observer Pattern ( 옵저버 패턴 )의 부족한 점
 * 1. 이벤트 처리를 해주는 Observer에게 완료(Complete)를 알려주지 않는다.
 * 2. `Observer`에게 Error(Exception) 이 발생했다는 사실을 알리지 않는다.
 *
 * 위 두 문제를 해결한 내용을 `Reactive Streams`에 추가했다.
 *
 */


@Suppress("deprecation")
class IntObservable : Observable(), Runnable {
    override fun run() {
        for (i in 1..10) {
            setChanged()
            notifyObservers(i) // PUSH
        }
    }
}

@Suppress("deprecation")
fun main() {
    // Source -> Event 를 Observer( Observable에 등록된 관찰자 )에게 던진다.
    val ob = Observer { o, arg -> println("${Thread.currentThread().name} : ${arg}") }

    val io = IntObservable()
    io.addObserver(ob)

    val es = Executors.newSingleThreadExecutor()

    es.execute(io)
    es.shutdown()
    println("${Thread.currentThread().name} : 메인스레드 종료")

}