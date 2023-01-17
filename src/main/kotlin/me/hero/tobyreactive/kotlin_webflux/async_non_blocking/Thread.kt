package me.hero.tobyreactive.kotlin_webflux.async_non_blocking

fun main() {
    for (i in 0..5) {
        Thread {
            println("current Thread name : ${Thread.currentThread().name}")
        }.start()
    }
    println("current Thread name : ${Thread.currentThread().name}")
}