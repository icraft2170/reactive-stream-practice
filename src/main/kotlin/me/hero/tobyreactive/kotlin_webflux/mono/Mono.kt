package me.hero.tobyreactive.kotlin_webflux.mono

import reactor.core.publisher.Mono

fun main() {
    val mono = Mono.just("Hello Reactive World!")
    mono.subscribe(::println)
//    println(mono)
}