package com.idiomcentric.daos

import com.idiomcentric.services.ConfigService
import com.idiomcentric.services.PostgresConfig
import com.google.inject.Inject
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresConnectionProvider @Inject constructor(configService: ConfigService) {
    val database: Database

    init { database = Database.connect(hikari(configService.config.postgres)) }

    suspend fun <T> dbQuery(block: (Transaction) -> T): T =
        withContext(Dispatchers.IO) {
            transaction(db = database) {
                block(this)
            }
        }

    private fun hikari(db: PostgresConfig): HikariDataSource {
        val hikariConfig = HikariConfig()

        with(hikariConfig) {
            jdbcUrl = "${db.hostname}/${db.database}"
            username = db.username
            password = db.password
            driverClassName = db.driverClassName

            validate()
        }

        return HikariDataSource(hikariConfig)
    }
}
