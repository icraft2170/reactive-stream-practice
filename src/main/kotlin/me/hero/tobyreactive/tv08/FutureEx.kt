package me.hero.tobyreactive.tv08

import me.hero.tobyreactive.tvcommon.Logger.log
import java.lang.RuntimeException
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

/**
 * Future
 * - javascript 에서는 Promise로 비동기적으로 수행후의 값을 즉, 미래의 값을 의미함.
 * - Future 미래의 값을 가지고 있다는 의미로 지어진 이름, Promise 약속된 값을 가지고 있다는 의미로 지어진 이름
 *
 *
 */


//fun main() {
//    val es: ExecutorService = Executors.newCachedThreadPool()
//
//    // execute 는 Runnable 을 실행만 해준다.
//    es.execute {
//        Thread.sleep(2000)
//        log.info { "Async" }
//    }
//}

//fun main() {
//    val es: ExecutorService = Executors.newCachedThreadPool()
//
//    // submit 은 Callable을 실행해주기 떄문에 콜백이 존재한다.
//    var f = es.submit<String> {
//        Thread.sleep(2000)
//        log.info { "Async" }
//        "Hello"
//    }
//
//    println(f.get())
//    log.info { "Exit" }
//}

//fun main() {
//    val es: ExecutorService = Executors.newCachedThreadPool()
//
//    /**
//     * FutureTask는 Future 와 Callable 의 역할을 합쳐둔 것을 의미한다.
//     */
//    val ft: FutureTask<String> = object: FutureTask<String>(
//      Callable {
//          Thread.sleep(2000)
//          log.info { "Async" }
//          "Hello"
//      }
//    ) {
//
//        // Future의 작업이 끝날 때 호출되는 Hooking Function 에서 get을 호
//        override fun done() {
//            println(get())
//        }
//    }
//
//    es.execute(ft)
//    log.info { "Exit" }
//    es.shutdown()
//}


/**
 * CallbackFutureTask ( FutureTask + 성공시 실행되는 Callable + 실패시 실행되는 Callable )
 */

fun interface SuccessCallback {
    fun onSuccess(result: String)
}

fun interface ExceptionCallback {
    fun onError(throwable: Throwable)
}

class CallbackFutureTask(
    private val callable: Callable<String>,
    private val successCallback: SuccessCallback,
    private val exceptionCallback: ExceptionCallback,
) : FutureTask<String>(callable) {

    override fun done() {
        try {
            successCallback.onSuccess(get())
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        } catch (e: ExecutionException) {
            exceptionCallback.onError(e.cause!!)
        }

    }
}

fun main() {
    val es: ExecutorService = Executors.newCachedThreadPool()

    val f: CallbackFutureTask = CallbackFutureTask(
        {
            Thread.sleep(2000)
            //if (1 == 1) throw RuntimeException("Async Error!")
            log.info { "Async" }
            "Hello"
                },
        {
            log.info { it }
        },
        {
            log.error { it.message }
        }
    )

    es.execute(f)
    log.info { "Exit" }
    es.shutdown()
}