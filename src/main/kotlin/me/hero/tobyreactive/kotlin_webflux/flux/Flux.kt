package me.hero.tobyreactive.kotlin_webflux.flux

import reactor.core.publisher.Flux


data class CellPhone(
    val name: String,
    val price: Int,
    val currency: Currency,
)

enum class Currency {
    KRW, USD
}

fun main() {
    val iphone = CellPhone(name = "IPhone", price = 100, currency = Currency.KRW)
    val galaxy = CellPhone(name = "Galaxy", price = 90, currency = Currency.KRW)

    val flux: Flux<CellPhone> =
        Flux.just(iphone, galaxy)

    flux.subscribe(::println)
}