package com.witek

import com.witek.config.DiscountProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@EnableCaching
@SpringBootApplication
@EnableConfigurationProperties(DiscountProperties::class)
@EnableJdbcRepositories
class DiscountServiceApplication

fun main(args: Array<String>) {
    runApplication<DiscountServiceApplication>(*args)
}
