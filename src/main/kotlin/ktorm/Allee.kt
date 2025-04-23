package ktorm

import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int

data class Allee(val id: Int, val numeroAllee: Int)

object Allees : BaseTable<Allee>("allee") {
    val id = int("IdAllee").primaryKey()
    val numeroAllee = int("NumeroAllee")  // Nom exact de la colonne dans la BDD

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Allee(row[id] ?: 0, row[numeroAllee] ?: 0)
}

val Database.allees get() = this.sequenceOf(Allees)