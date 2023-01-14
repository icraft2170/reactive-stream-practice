package me.hero.tobyreactive.tv05.pubsub

import java.util.concurrent.Flow.*


/**
 * 1. Publisher == Observable
 *  - 한계없이 순서를 가진 연속된 요소를 제공하는 공급자
 * 2. Subscriber == Observer
 *
 *  Reactive Streams 의 Protocol(WorkFlow)
 *  ```
 *  onSubscribe(필수) -> onNext (Optional로 N번 호출 가능) -> onError or onComplete ( 둘 중 하나 호출 )
 *  ```
 */
fun main() {
    val itr = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val publisher = object: Publisher<Int> {
        override fun subscribe(subscriber: Subscriber<in Int>?) {
            val it :Iterator<Int> = itr.iterator()
            subscriber?.onSubscribe(object: Subscription {
                // Back Pressure ( 역압 ) : 일종의 Rate Limit 설정 기능으로 Pub / Sub 간의 처리속도 차이로
                // 중간에 차이로 생기는 데이터들을 저장해둘 메모리공간 낭비를 막을 수 있다.
                override fun request(n: Long) {
                    var num = n
                    try {
                        while (num-- > 0)  {
                            if (it.hasNext()) {
                                subscriber.onNext(it.next())
                            } else {
                                subscriber.onComplete()
                                break
                            }
                        }
                    } catch (e: Exception) {
                        subscriber.onError(e)
                    }
                }

                override fun cancel() {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    val subscriber = object : Subscriber<Int> {
        lateinit var subscription: Subscription
        var isOver = false

        override fun onSubscribe(subscription: Subscription) {
            println("onSubscribe")
            this.subscription = subscription

            while (!isOver) {
                this.subscription.request(2)
                println("----------------------")
                Thread.sleep(1000)
            }
        }

        override fun onNext(item: Int?) {
            println("onNext ${item}")
        }

        override fun onError(throwable: Throwable?) {
            isOver = true
            println("onError")
        }

        override fun onComplete() {
            isOver = true
            println("onComplete")
        }

    }

    publisher.subscribe(subscriber)

}

