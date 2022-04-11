package top.imyzt.learning

import spark.kotlin.ignite

fun main(args: Array<String>) {
    val http = ignite()
    http.get("/hello") {
        "Hello Spark Kotlin!"
    }
    http.post("/hello") {
        "Hello Spark Kotlin!"
    }
}