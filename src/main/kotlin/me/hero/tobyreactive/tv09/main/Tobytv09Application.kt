package me.hero.tobyreactive.tv09.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.AsyncRestTemplate
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


/**
 * Thread Pool Hell
 * - Thread Pool 이 존재하는데, 요청이 몰려들 때 Pool 이 모두 소모되어 요청처리가 불가능한 상태이다. 때문에 많은 지연시간이 생긴다.
 *
 * Thread Pool Hell 해결하기
 *
 */

//class Completion {
//    companion object {
//        @Suppress("DEPRECATION")
//        @JvmStatic
//        fun from( lf: ListenableFuture<ResponseEntity<String>>?): Completion {
//            val completion = Completion()
//            lf?.addCallback(
//                {
//                  completion.complete(it)
//                },
//                {
//                    completion.error(it)
//                }
//            )
//            return completion
//        }
//    }
//
//    private fun error(it: Throwable) {
//        TODO("Not yet implemented")
//    }
//
//    private fun complete(it: ResponseEntity<String>?) {
//        TODO("Not yet implemented")
//    }
//}
//
//@RestController
//class MyController {
//    @GetMapping("/rest")
//    fun rest(idx: Int): String {
//        val rt = RestTemplate()
//        val res =
//            rt.getForObject<String>("http://localhost:8081/service?request={request}", "hello $idx")
//        return res
//    }
//}
//
//@SpringBootApplication
//class Tobytv09Application()
//
//fun main(args: Array<String>) {
//    runApplication<Tobytv09Application>(*args)
//}
//
//
