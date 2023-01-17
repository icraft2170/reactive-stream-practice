package me.hero.tobyreactive.kotlin_webflux.async_non_blocking

import java.util.concurrent.Executors

fun main() {
    val pool = Executors.newFixedThreadPool(5)
    try {
        for (i in 0..5) {
            pool.execute {
                println("current Thread Name : ${Thread.currentThread().name}")
            }
        }
    } finally {
        pool.shutdown()
    }
    println("current Thread Name : ${Thread.currentThread().name}")
}