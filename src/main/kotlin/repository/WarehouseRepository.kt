package repository

import ktorm.Allee
import ktorm.allees
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.database.Database
import org.ktorm.entity.toList

class WarehouseRepository: KoinComponent {
    private val database:Database by inject()

    fun getWarehouseData(): List<Allee> {
        return database.allees.toList()
    }
}