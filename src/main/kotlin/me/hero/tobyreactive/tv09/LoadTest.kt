package me.hero.tobyreactive.tv09

import me.hero.tobyreactive.tvcommon.Logger.log
import org.springframework.util.StopWatch
import org.springframework.web.client.RestTemplate
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun main() {
    val es = Executors.newFixedThreadPool(100)

    val rt = RestTemplate()
    val url = "http://localhost:8080/rest?idx={idx}"

    // 쓰레드 동기화 작업
    val barrier = CyclicBarrier(100)


    for (idx in 1..100) {
        es.execute {
            barrier.await() // Block
            log.info { "Thread $idx " }

            val sw = StopWatch()
            sw.start()
            val res = rt.getForObject(url, String::class.java, idx)
            sw.stop()
            log.info { "Elapsed : $idx -> ${sw.totalTimeSeconds}s / res: $res"  }
        }
    }

    barrier.await() // Block
    val main = StopWatch()
    main.start()

    es.shutdown()
    es.awaitTermination(100, TimeUnit.SECONDS)

    main.stop()
    log.info { "Total :  ${main.totalTimeSeconds}s" }
}