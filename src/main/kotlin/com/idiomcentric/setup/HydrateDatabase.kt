package com.idiomcentric.setup

import com.idiomcentric.daos.Example
import com.idiomcentric.daos.ExamplesTable
import com.idiomcentric.daos.PostgresConnectionProvider
import com.idiomcentric.logger
import com.idiomcentric.services.ConfigService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

fun amain() {
    val configService = ConfigService()
    val connection = PostgresConnectionProvider(configService)
    val examplesTable = ExamplesTable(connection)

    transaction(db = connection.database) {
        logger.info("Creating Examples Table")
        SchemaUtils.create(examplesTable)
    }
}


suspend fun main() {
    val configService = ConfigService()
    val connection = PostgresConnectionProvider(configService)
    val examplesTable = ExamplesTable(connection)


    withContext(Dispatchers.IO) {
        examplesTable.create(Example(UUID.randomUUID(), Instant.now()))
    }

}