package com.idiomcentric.daos

import com.google.inject.Inject
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.time.Instant
import java.util.UUID

class ExamplesTable @Inject constructor(
    private val connection: PostgresConnectionProvider
) : Table(name = "EXAMPLES") {
    val id = uuid("ID")
    private val createdAt = timestamp("CREATED_AT")
    override val primaryKey = PrimaryKey(id)

    suspend fun retrieveAll(): List<Example> = connection.dbQuery {
        this.selectAll().map(::toHierarchy)
    }

    suspend fun retrieveById(exampleId: UUID): Example? = connection.dbQuery {
        this.select { id eq exampleId }.map(::toHierarchy).firstOrNull()
    }

    suspend fun create(example: Example): Example = connection.dbQuery {
        this.insert { toRow(example, it) }
        example
    }

    private fun toRow(example: Example, insert: InsertStatement<Number>) {
        insert[this.id] = example.id
        insert[this.createdAt] = example.createdAt
    }

    private fun toHierarchy(row: ResultRow): Example {
        val id = row[this.id]
        val createdAt = row[this.createdAt]
        return Example(id, createdAt)
    }
}

data class Example(val id: UUID, val createdAt: Instant)
