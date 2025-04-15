package com.witek.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.time.Instant

object MySqlTestContainerRunner {

    val mysqlContainer: MySQLContainer<*> by lazy {
        MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("testdb")
            .withUsername("sa")
            .withPassword("sa")
            .withReuse(false)
    }

    fun run() {
        val start: Instant = Instant.now()
        if (!mysqlContainer.isRunning) {
            log.info("Starting container ${mysqlContainer.dockerImageName}")
            mysqlContainer.start()
        }
        log.info("🐳 MySqlTestContainer started in ${Duration.between(start, Instant.now())}")
    }

    private val log: Logger by lazy { LoggerFactory.getLogger(javaClass) }
}