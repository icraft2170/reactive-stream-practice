package me.hero.tobyreactive.tv07.scheduler

import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import java.util.concurrent.Executors
import java.util.concurrent.Flow.Publisher
import java.util.concurrent.Flow.Subscriber
import java.util.concurrent.Flow.Subscription
import java.util.concurrent.ThreadFactory


/**
 * Reactive Stream
 * subscribeOn(Scheduler) - Operator
 *  - Publisher(데이터 생성)가 느리고 Subscriber(데이터 처리)는 빠른 경우에 사용된다.
 *  - Publisher 스레드를 분리해서 사용하도록 함.
 * publishOn(Scheduler) - Operator
 * - Publisher(데이터 생성)가 빠르고 Subscriber(데이터 처리)는 느린 경우에 사용된다.
 */


fun main() {
    println("[${Thread.currentThread().name}] - 시작")
    val pub = Publisher<Int> { subscriber ->
         subscriber.onSubscribe(object: Subscription {
             override fun request(n: Long) {
                println("[${Thread.currentThread().name}] - request")
                subscriber.onNext(1)
                subscriber.onNext(2)
                subscriber.onNext(3)
                subscriber.onNext(4)
                subscriber.onNext(5)
                subscriber.onComplete()
             }

             override fun cancel() {
                 TODO("Not yet implemented")
             }

         })
    }

    val publishOnPub = publishOn(pub)
    val subOnPub = subscribeOn(publishOnPub)

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

//    publishOnPub.subscribe(sub)
    subOnPub.subscribe(sub)
    println("[${Thread.currentThread().name}] - 종료")
}

private fun subscribeOn(pub: Publisher<Int>) =
    Publisher<Int> { subscriber ->
        val es = Executors.newSingleThreadExecutor(CustomizableThreadFactory("subOn-"))
        es.execute { pub.subscribe(subscriber) }
    }

private fun publishOn(pub: Publisher<Int>): Publisher<Int> {
    val es = Executors.newSingleThreadExecutor(CustomizableThreadFactory("publishOn-"))

    return Publisher<Int> { subscriber ->
        pub.subscribe(object : Subscriber<Int> {
            override fun onSubscribe(subscription: Subscription?) {
                println("[${Thread.currentThread().name}] - onSubscribe")
                subscription?.request(Long.MAX_VALUE)
            }

            override fun onNext(item: Int?) {
                es.execute {
                    println("[${Thread.currentThread().name}] - OnNext : $item")
                }
            }

            override fun onError(throwable: Throwable?) {
                es.execute {
                    println("[${Thread.currentThread().name}] - OnError ${throwable?.message}")
                }
            }

            override fun onComplete() {
                es.execute {
                    println("[${Thread.currentThread().name}] - OnComplete")
                }
            }
        })
    }
}
