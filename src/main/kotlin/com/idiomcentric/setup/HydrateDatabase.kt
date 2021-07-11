package com.idiomcentric.setup

import com.idiomcentric.daos.ExamplesTable
import com.idiomcentric.daos.PostgresConnectionProvider
import com.idiomcentric.logger
import com.idiomcentric.services.ConfigService
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val configService = ConfigService()
    val connection = PostgresConnectionProvider(configService)
    val examplesTable = ExamplesTable(connection)

    transaction(db = connection.database) {
        logger.info("Creating Examples Table")
        SchemaUtils.create(examplesTable)
    }
}
