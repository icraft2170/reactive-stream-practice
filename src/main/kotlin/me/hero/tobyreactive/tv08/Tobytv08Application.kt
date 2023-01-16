//package me.hero.tobyreactive.tv08
//
//import me.hero.tobyreactive.tvcommon.Logger.log
//import org.springframework.boot.ApplicationRunner
//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.boot.runApplication
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.scheduling.annotation.Async
//import org.springframework.scheduling.annotation.AsyncResult
//import org.springframework.scheduling.annotation.EnableAsync
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
//import org.springframework.stereotype.Component
//import org.springframework.util.concurrent.ListenableFuture
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RestController
//import org.springframework.web.context.request.async.DeferredResult
//import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter
//import java.util.Queue
//import java.util.concurrent.Callable
//import java.util.concurrent.ConcurrentLinkedQueue
//import java.util.concurrent.Executors
//
///**
// * - Callable 을 사용한 Servlet Thread Async
// * - @EnableAsync @Async
// * - DeferredResult 큐
// *
// */
//@RestController
//class MyController(
//    private val myService: MyService,
//    private val results: Queue<DeferredResult<String>> = ConcurrentLinkedQueue()
//) {
//    // Callable 로 반환하면 Servlet Thread가 즉시 Pool로 반환된다.
//    @GetMapping("/callable")
//    fun callable(): Callable<String> {
//        log.info { "callable" }
//        return Callable<String> {
//            log.info { "async" }
//            Thread.sleep(2000)
//            "hello"
//        }
//    }
//
//    @GetMapping("/async")
//    fun async(): String  {
//        log.info { "async" }
//        Thread.sleep(2000)
//        return "hello"
//    }
//
//    @GetMapping("/dr")
//    fun dr(): DeferredResult<String>  {
//        log.info { "dr" }
//        val dr = DeferredResult<String>(600000)
//        results.add(dr)
//        return dr
//    }
//
//    @GetMapping("/dr/count")
//    fun drCount(): String {
//        return results.size.toString()
//    }
//
//    @GetMapping("/dr/event")
//    fun drEvent(): String {
//        for (dr in results) {
//            dr.setResult("Hello ")
//            results.remove(dr)
//        }
//        return "OK"
//    }
//
//    @GetMapping("/emitter")
//    fun emitter(): ResponseBodyEmitter {
//        val emitter = ResponseBodyEmitter()
//        Executors.newSingleThreadExecutor().submit {
//            for (i in 0..50) {
//                emitter.send("<p>Stream $i </p>")
//                Thread.sleep(100)
//            }
//        }
//        return emitter
//    }
//}
//
//@Component
//class MyService{
//
//    // ListenableFuture<String> 역시 Spring 6부터 지원종료 CompletableFuture로 대신한다.
//    // Future, CompletableFuture 등 으로 대신 할 수 있다.
//    @Async
//    fun hello(): ListenableFuture<String>//Future<String>
//    {
//        log.info { "hello()" }
//        Thread.sleep(2000)
//        // Spring 6 부터 지원종료 CompletableFuture 로 대신하라
//        return AsyncResult<String>("Hello")
//    }
//}
//
//@SpringBootApplication
//@EnableAsync // @Async 어노테이션 사용에 필요하다.
//class Tobytv08Application(
//    val myService: MyService,
//) {
//
//    @Bean
//    fun run(): ApplicationRunner {
//        return ApplicationRunner {
//            log.info { "run()" }
//            val f = myService.hello()
//            log.info { "exit : ${f.isDone}" }
//            log.info { "result : ${f.get()}" }
//        }
//    }
//}
//
//@Configuration
//class WebMvcConfig : WebMvcConfigurer {
//    @Bean
//    fun taskExecutor(): ThreadPoolTaskExecutor {
//        val pool = ThreadPoolTaskExecutor()
//        with(pool) {
//            corePoolSize = 10
//            maxPoolSize= 100
//            queueCapacity = 50
//            return this
//        }
//    }
//
//    override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
//        configurer.setTaskExecutor(taskExecutor())
//    }
//}
//
//fun main(args: Array<String>) {
//    runApplication<Tobytv08Application>(*args)
//}
//
//
