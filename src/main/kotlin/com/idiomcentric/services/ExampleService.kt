package com.idiomcentric.services

import com.google.inject.Inject
import com.idiomcentric.daos.Example
import com.idiomcentric.daos.ExamplesTable
import java.util.UUID

class ExampleService @Inject constructor(
    private val examplesTable: ExamplesTable,
) {
    suspend fun retrieve(exampleId: UUID): Example? {
        return examplesTable.retrieveById(exampleId)
    }
}
