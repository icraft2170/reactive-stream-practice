package me.hero.tobyreactive.tv08

import me.hero.tobyreactive.tvcommon.Logger.log
import org.springframework.util.StopWatch
import org.springframework.web.client.RestTemplate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun main() {
    val es = Executors.newFixedThreadPool(100)

    val rt = RestTemplate()
    val url = "http://localhost:8080/dr"
//    val url = "http://localhost:8080/callable"
//    val url = "http://localhost:8080/async"

    val main = StopWatch()
    main.start()

    for (idx in 1..100) {
        es.execute {
            log.info { "Thread $idx " }

            val sw = StopWatch()
            sw.start()
            rt.getForObject(url, String::class.java)
            sw.stop()
            log.info { "Elapsed : $idx -> ${sw.totalTimeSeconds}s" }
        }
    }

    es.shutdown()
    es.awaitTermination(100, TimeUnit.SECONDS)

    main.stop()
    log.info { "Total :  ${main.totalTimeSeconds}s" }
}