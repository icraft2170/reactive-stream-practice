package me.hero.tobyreactive.kotlin_webflux.async_non_blocking

import java.util.concurrent.CompletableFuture


fun main() {
    val completableFuture = CompletableFuture.supplyAsync {
        Thread.sleep(2000)
        sum(100, 200)
    }

    println("계산 시작")
    completableFuture.thenApplyAsync(::println) // 논 블라킹으로 동작

    while (!completableFuture.isDone) {
        Thread.sleep(500)
        println("계산 집계 중")
    }
    println("계산 종료")
}