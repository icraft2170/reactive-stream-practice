package me.hero.tobyreactive.tv07.scheduler

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.concurrent.TimeUnit

//fun main() {
//    Flux.range(1, 10)
//        .publishOn(Schedulers.newSingle("pub"))
//        .log()
//        .subscribeOn(Schedulers.newSingle("sub"))
//        .log()
//        .subscribe(System.out::println)
//
//
//    println("종료")
//}

fun main() {
    // Daemon Thread 를 만들기 때문에 main or User Thread 가 모두 삭제되면 종료 됨
    Flux.interval(Duration.ofMillis(200))
        .take(10) // 받을 데이터의 개수를 결정 함
        .subscribe { i -> println("[ ${Thread.currentThread().name} ] - onNext($i)") }

    TimeUnit.SECONDS.sleep(5)
}