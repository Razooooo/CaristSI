package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object Cariste : Table<Nothing>("cariste") {
    val id = int("IdCariste").primaryKey()
    val firstName = varchar("Prenom")
    val lastName = varchar("Nom")
    val mdp = varchar("MDP")
    val naissance = date("Naissance")
    val embauche = date("Embauche")
    val login = varchar("Login")
}