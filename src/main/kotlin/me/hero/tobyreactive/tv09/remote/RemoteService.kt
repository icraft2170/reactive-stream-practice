package me.hero.tobyreactive.tv09.remote

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RemoteController{
    @GetMapping("/service")
    fun service1(request: String): String {
        Thread.sleep(1000)
        return "$request/service"
    }

    @GetMapping("/service2")
    fun service2(request: String): String {
        Thread.sleep(1000)
        return "$request/service2"
    }
}

@SpringBootApplication
class RemoteService()

fun main(args: Array<String>) {
    runApplication<RemoteService>(*args) {
        this.setDefaultProperties(
            mapOf("server.port " to "8081", "server.tomcat.threads.max" to "1000")
        )
    }
}