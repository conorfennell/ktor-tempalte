package com.idiomcentric.services

import com.typesafe.config.ConfigFactory

class ConfigService {
    private val applicationConf = ConfigFactory.load()

    val config = Config(
        postgres = PostgresConfig(
            hostname = applicationConf.getString("db.postgres.hostname"),
            database = applicationConf.getString("db.postgres.database"),
            username = applicationConf.getString("db.postgres.username"),
            password = applicationConf.getString("db.postgres.password"),
            driverClassName = applicationConf.getString("db.postgres.driverClassName"),
        )
    )
}

data class Config(val postgres: PostgresConfig)
data class PostgresConfig(
    val hostname: String,
    val database: String,
    val username: String,
    val password: String,
    val driverClassName: String,
)
