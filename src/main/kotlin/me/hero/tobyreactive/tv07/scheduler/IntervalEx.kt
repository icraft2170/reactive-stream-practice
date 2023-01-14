package me.hero.tobyreactive.tv07.scheduler

import java.util.concurrent.Executors
import java.util.concurrent.Flow.Publisher
import java.util.concurrent.Flow.Subscriber
import java.util.concurrent.Flow.Subscription
import java.util.concurrent.TimeUnit

fun main() {
    val pub :Publisher<Int> = Publisher<Int> { subscriber ->
        subscriber.onSubscribe(object: Subscription {
            var no: Int = 0
            var cancelled = false
            override fun request(n: Long) {
                val exec = Executors.newSingleThreadScheduledExecutor()
                exec.scheduleAtFixedRate(Runnable {
                    if (cancelled) {
                        exec.shutdown()
                        return@Runnable
                    }
                    subscriber.onNext(no++) }, 1, 300, TimeUnit.MILLISECONDS)
            }

            override fun cancel() {
                cancelled = true
            }

        })
    }
    
    val takePub :Publisher<Int> = Publisher<Int> {subscriber ->  
        pub.subscribe(object: Subscriber<Int> {
            var count: Int = 0
            lateinit var subscription: Subscription
            override fun onSubscribe(subscription: Subscription) {
                this.subscription = subscription
                subscriber.onSubscribe(subscription)
            }

            override fun onError(throwable: Throwable?) {
                subscriber.onError(throwable)
            }

            override fun onComplete() {
                subscriber.onComplete()
            }

            override fun onNext(item: Int?) {
                subscriber.onNext(item)
                if (++count > 4) {
                    subscription.cancel()
                }
            }

        })
    }

    val sub = object: Subscriber<Int> {
        override fun onSubscribe(subscription: Subscription?) {
            println("[${Thread.currentThread().name}] - onSubscribe")
            subscription?.request(Long.MAX_VALUE)
        }

        override fun onError(throwable: Throwable?) {
            println("[${Thread.currentThread().name}] - OnError ${throwable?.message}")
        }

        override fun onComplete() {
            println("[${Thread.currentThread().name}] - OnComplete")
        }

        override fun onNext(item: Int?) {
            println("[${Thread.currentThread().name}] - OnNext : $item")
        }
    }

    takePub.subscribe(sub)
}