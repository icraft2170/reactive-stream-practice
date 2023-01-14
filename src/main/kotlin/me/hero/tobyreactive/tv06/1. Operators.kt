package me.hero.tobyreactive.tv06

import java.util.concurrent.Flow.*
import java.util.stream.Stream.*

fun iterPub(itr: MutableList<Int>): Publisher<Int> {
    return Publisher<Int> { subscriber ->
        subscriber?.onSubscribe(object: Subscription {
            override fun request(n: Long) {
                try {
                    itr.forEach { i -> subscriber.onNext(i) }
                    subscriber.onComplete()
                } catch (t: Throwable) {
                    subscriber.onError(t)
                }
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }
        })
    }
}

fun mapPub(publisher: Publisher<Int>, function: (Int) -> Int): Publisher<Int> {
    return Publisher<Int> {subscriber ->
        subscriber?.onSubscribe(object: Subscription {
            override fun request(n: Long) {
                publisher.subscribe(object: Subscriber<Int> {
                    override fun onSubscribe(subscription: Subscription?) {
                        subscriber.onSubscribe(subscription)
                    }

                    override fun onNext(item: Int?) {
                        subscriber.onNext(function.invoke(item!!))
                    }

                    override fun onError(throwable: Throwable?) {
                        subscriber.onError(throwable)
                    }

                    override fun onComplete() {
                        subscriber.onComplete()
                    }
                })
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }
        })
    }
}


fun subscribe(): Subscriber<Int> {
    return object : Subscriber<Int> {

        override fun onSubscribe(subscription: Subscription) {
            println("onSubscribe")
            subscription.request(Long.MAX_VALUE)
        }

        override fun onNext(item: Int?) {
            println("onNext ${item}")
        }

        override fun onError(throwable: Throwable?) {
            println("onError")
        }

        override fun onComplete() {
            println("onComplete")
        }

    }
}

/**
 * Publisher -> (Data) -> Subscribe
 * - 결국 Publisher 와 Subscribe 사이에 Subscription 이라는 중재자/브로커 를 두어 중간 제어를 진행한다.
 *
 * Operator ( 연산자 )
 *  일종에 데이터 스트림에 변화/영향을 주는 연산자로 함수 형태로서 체이닝 가능하게 만들어져 있다. 다양한 Operator 를 통해
 *  데이터가 전송되는 과정에 변경된다. 대표적으로 Stream API를 생각하면 된다.
 *
 *  Publisher -> (Data1) -> Operator1 < map > -> (final Data) -> Subscribe
 */
fun main() {
    val iter = iterate(1) { i -> i + 1 }.limit(10).toList()

    val publisher = iterPub(iter)
    val mapPub: Publisher<Int> = mapPub(publisher) { i: Int -> i * 10 }
    mapPub.subscribe(subscribe())
}

