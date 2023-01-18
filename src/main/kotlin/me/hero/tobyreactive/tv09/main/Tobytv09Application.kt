package me.hero.tobyreactive.tv09.main

import io.netty.channel.nio.NioEventLoopGroup
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.http.client.Netty4ClientHttpRequestFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.AsyncRestTemplate
import org.springframework.web.context.request.async.DeferredResult
import java.util.concurrent.CompletableFuture


/**
 * Thread Pool Hell
 * - Thread Pool 이 존재하는데, 요청이 몰려들 때 Pool 이 모두 소모되어 요청처리가 불가능한 상태이다. 때문에 많은 지연시간이 생긴다.
 *
 * Thread Pool Hell 해결하기
 *
 */

class Completion {
    companion object {
        @Suppress("DEPRECATION")
        @JvmStatic
        fun from( lf: ListenableFuture<ResponseEntity<String>>?): Completion {
            val completion = Completion()
            lf?.addCallback(
                {
                  completion.complete(it)
                },
                {
                    completion.error(it)
                }
            )
            return completion
        }
    }

    private fun error(it: Throwable) {
        TODO("Not yet implemented")
    }

    private fun complete(it: ResponseEntity<String>?) {
        TODO("Not yet implemented")
    }
}

@RestController
class MyController(
    private val myService: MyService,
) {
    val rt: AsyncRestTemplate =
        AsyncRestTemplate(Netty4ClientHttpRequestFactory(NioEventLoopGroup(1)))

    @GetMapping("/rest")
    fun rest(idx: Int): Any {
        val dr = DeferredResult<String>()

        toCF(rt.getForEntity(URI1, String::class.java, "hello $idx"))
            .thenCompose { s -> toCF(rt.getForEntity(URI2, String::class.java, s.body)) }
            .thenCompose { s2 -> toCF(myService.work(s2.body))}
            .thenAccept { s3 -> dr.setResult(s3)}
            .exceptionally  { e ->
                dr.setErrorResult(e.message)
                return@exceptionally null
            }
        return dr
    }

    fun <T> toCF(lf: ListenableFuture<T>): CompletableFuture<T> {
        val cf: CompletableFuture<T> = CompletableFuture()
        lf.addCallback({s -> cf.complete(s)}, {e -> cf.completeExceptionally(e)})
        return cf
    }



    companion object {
        const val URI1: String = "http://localhost:8081/service?request={request}"
        const val URI2 = "http://localhost:8081/service2?request={request}"
    }
}

@Service
class MyService {
    @Async
    fun work(request: String?): ListenableFuture<String> {
        return AsyncResult("$request/asyncwork")
    }
}

@SpringBootApplication
class Tobytv09Application {
    @Bean
    fun myThreadPool(): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        return with(taskExecutor) {
            corePoolSize = 1
            maxPoolSize = 1
            initialize()
            taskExecutor
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Tobytv09Application>(*args)
}


