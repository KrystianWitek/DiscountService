package com.witek.config

import com.witek.config.MySqlTestContainerRunner.mysqlContainer
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

interface WithMySqlTestContainer {

    companion object {
        @Suppress("unused")
        @DynamicPropertySource
        @JvmStatic
        fun overrideConfiguration(registry: DynamicPropertyRegistry) {
            MySqlTestContainerRunner.run()
            registry.run {
                add("spring.datasource.url") { mysqlContainer.jdbcUrl }
                add("spring.datasource.username") { mysqlContainer.username }
                add("spring.datasource.password") { mysqlContainer.password }
            }
        }
    }
}