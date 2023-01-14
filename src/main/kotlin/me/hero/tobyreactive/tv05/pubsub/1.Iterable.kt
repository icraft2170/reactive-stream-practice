package me.hero.tobyreactive.tv05.pubsub

/**
 * Duality ( 쌍대성 )
 * - Observer <---> Iterable
 *  - Iterable 은 Pull(당기는) 방식
 *  - Observer 은 Push(밀어 넣는) 방식
 *
 * Observer Pattern ( 옵저버 패턴 )
 *
 * Reactive Streams - 표준 ( Java9 API로 구현체가 추가 됨 )
 *
 */


// Iterable
fun main() {
    val iter: Iterable<Int> = Iterable {
        object :Iterator<Int> {
            var i = 0
            val MAX = 10

            override fun hasNext(): Boolean {
                return i < MAX
            }

            override fun next(): Int { // PULL
                return ++i
            }
        }
    }

    for (i in iter) { // for-each
        println("i : ${i}")
    }
}