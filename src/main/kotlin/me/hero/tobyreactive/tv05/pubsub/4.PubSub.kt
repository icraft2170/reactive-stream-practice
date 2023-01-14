package me.hero.tobyreactive.tv05.pubsub

import java.util.concurrent.Executors
import java.util.concurrent.Flow
import java.util.concurrent.TimeUnit


fun main() {
    val itr = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val es = Executors.newCachedThreadPool()

    val publisher = object: Flow.Publisher<Int> {
        override fun subscribe(subscriber: Flow.Subscriber<in Int>?) {
            val it :Iterator<Int> = itr.iterator()
            subscriber?.onSubscribe(object: Flow.Subscription {
                override fun request(n: Long) {
                    var num = n
                    es.submit {
                        try {
                            while (num-- > 0) {
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
                }

                override fun cancel() {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    val subscriber = object : Flow.Subscriber<Int> {
        lateinit var subscription: Flow.Subscription

        override fun onSubscribe(subscription: Flow.Subscription) {
            println("${Thread.currentThread().name} - onSubscribe")
            this.subscription = subscription
            this.subscription.request(1)
        }

        override fun onNext(item: Int?) {
            println("${Thread.currentThread().name} - onNext ${item}")
            this.subscription.request(1)
        }

        override fun onError(throwable: Throwable?) {
            println("onError ${throwable?.message}")
        }

        override fun onComplete() {
            println("onComplete")
        }

    }

    publisher.subscribe(subscriber)

    es.awaitTermination(1, TimeUnit.SECONDS)
    es.shutdown()
}

