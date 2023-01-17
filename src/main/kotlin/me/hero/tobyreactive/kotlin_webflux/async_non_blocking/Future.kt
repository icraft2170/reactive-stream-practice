package me.hero.tobyreactive.kotlin_webflux.async_non_blocking

import java.util.concurrent.Executors

fun sum(a: Int, b: Int) = a + b

fun main() {
    val pool = Executors.newSingleThreadExecutor()
    val future = pool.submit<Int> {
        Thread.sleep(2000)
        sum(100, 200)
    }

    println("계산 시작")
    println("계산 완료 ${future.isDone}")
    println("결과 : ${future.get()}") // Future.get()은 해당 위치에서 Blocking 하고 Callable 에서 반환 값이 오기를 기다림.
    println("계산 종료")
}
