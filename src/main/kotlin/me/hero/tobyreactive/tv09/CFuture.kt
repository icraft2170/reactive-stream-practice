package me.hero.tobyreactive.tv09

import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit

fun main() {
//    val completedFuture = CompletableFuture<Int>()
//    completedFuture.complete(2)
//    completedFuture.completeExceptionally(RuntimeException())
//    println(completedFuture.get())


//    CompletableFuture
//        .runAsync { println("${Thread.currentThread().name} - runAsync") }
//        .thenRun { println("${Thread.currentThread().name} - thenRunAsync") }
//        .thenRun { println("${Thread.currentThread().name} - thenRunAsync") }

    val es = Executors.newFixedThreadPool(10)
    CompletableFuture
        .supplyAsync {
            println("${Thread.currentThread().name} - runAsync")
//            if (1==1) throw RuntimeException()
            1
        }
        .thenCompose { i -> //FlatMap
            println("${Thread.currentThread().name} - thenCompose - $i")
            CompletableFuture.completedFuture(i + 1)
        }
        .thenApply { i -> //Map //thenApplyAsync 처럼 뒤에 Async가 붙는 경우 사용하는 ThreadPool 정책에 따라 Thread사용
            println("${Thread.currentThread().name} - thenApply - $i")
            i + 3
        }
        .thenApplyAsync({ i -> //thenApplyAsync 처럼 뒤에 Async가 붙는 경우 사용하는 ThreadPool 정책에 따라 Thread사용
            println("${Thread.currentThread().name} - thenApplyAsync - $i")
            i + 3
        }, es)
        .exceptionally { e ->
            -10 // 복구 작업 실무였다면 다른 서비스로의 조회를 실패하면 그냥 캐시에서 가져오는 것도 괜찮은 듯
        }
        .thenAcceptAsync({ i ->
            println("${Thread.currentThread().name} - thenAccept - $i")
        }, es)


    println("Exit")

    ForkJoinPool.commonPool().shutdown()
    ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS)



}