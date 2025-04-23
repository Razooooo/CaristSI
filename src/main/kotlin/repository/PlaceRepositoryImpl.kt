package repository

import ktorm.Places
import ktorm.Emplacements
import ktorm.Colonnes
import ktorm.Allees
import ktorm.Colis
import models.Place
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.time.LocalDate

class PlaceRepositoryImpl(private val database: Database) : PlaceRepository {

    override fun getAllPlaces(): List<Place> {
        return database.from(Places).select()
            .map { row ->
                Place(
                    idCariste = row[Places.idCariste]!!,
                    idColis = row[Places.idColis]!!,
                    idEmplacement = row[Places.idEmplacement]!!,
                    dateDepot = row[Places.dateDepot]
                )
            }
    }

    override fun getPlaceByKeys(caristeId: Int, colisId: Int, emplacementId: Int): Place? {
        return database.from(Places)
            .select()
            .where {
                (Places.idCariste eq caristeId) and
                        (Places.idColis eq colisId) and
                        (Places.idEmplacement eq emplacementId)
            }
            .map { row ->
                Place(
                    idCariste = row[Places.idCariste]!!,
                    idColis = row[Places.idColis]!!,
                    idEmplacement = row[Places.idEmplacement]!!,
                    dateDepot = row[Places.dateDepot]
                )
            }
            .firstOrNull()
    }

    override fun getPlacesByColisId(colisId: Int): List<Place> {
        return database.from(Places)
            .innerJoin(Colis, on = Places.idColis eq Colis.id) // Jointure interne pour s'assurer que le colis existe
            .select()
            .where { Places.idColis eq colisId }
            .map { row ->
                Place(
                    idCariste = row[Places.idCariste]!!,
                    idColis = row[Places.idColis]!!,
                    idEmplacement = row[Places.idEmplacement]!!,
                    dateDepot = row[Places.dateDepot]
                )
            }
    }

    override fun getPlacesByEmplacementId(emplacementId: Int): List<Place> {
        return database.from(Places)
            .select()
            .where { Places.idEmplacement eq emplacementId }
            .map { row ->
                Place(
                    idCariste = row[Places.idCariste]!!,
                    idColis = row[Places.idColis]!!,
                    idEmplacement = row[Places.idEmplacement]!!,
                    dateDepot = row[Places.dateDepot]
                )
            }
    }

    override fun getPlacesWithDetails(): List<Place> {
        return database.from(Places)
            .innerJoin(Colis, on = Places.idColis eq Colis.id) // Utilisation de innerJoin au lieu de leftJoin
            .leftJoin(Emplacements, on = Places.idEmplacement eq Emplacements.id)
            .leftJoin(Colonnes, on = Emplacements.idColonne eq Colonnes.id)
            .leftJoin(Allees, on = Colonnes.idAllee eq Allees.id)
            .select(
                Places.idCariste,
                Places.idColis,
                Places.idEmplacement,
                Places.dateDepot,
                Emplacements.niveau,
                Colonnes.numeroColonne,
                Allees.numeroAllee,
                Colis.longueur,
                Colis.largeur,
                Colis.hauteur,
                Colis.poids
            )
            .map { row ->
                Place(
                    idCariste = row[Places.idCariste]!!,
                    idColis = row[Places.idColis]!!,
                    idEmplacement = row[Places.idEmplacement]!!,
                    dateDepot = row[Places.dateDepot],
                    niveauEmplacement = row[Emplacements.niveau],
                    numeroColonne = row[Colonnes.numeroColonne],
                    numeroAllee = row[Allees.numeroAllee],
                    longueurColis = row[Colis.longueur],
                    largeurColis = row[Colis.largeur],
                    hauteurColis = row[Colis.hauteur],
                    poidsColis = row[Colis.poids]
                )
            }
    }

    override fun createPlace(place: Place): Boolean {
        val result = database.insert(Places) {
            set(it.idCariste, place.idCariste)
            set(it.idColis, place.idColis)
            set(it.idEmplacement, place.idEmplacement)
            set(it.dateDepot, place.dateDepot)
        }
        return result > 0
    }

    override fun updatePlace(place: Place): Boolean {
        val result = database.update(Places) {
            set(it.dateDepot, place.dateDepot)
            where {
                (it.idCariste eq place.idCariste) and
                        (it.idColis eq place.idColis) and
                        (it.idEmplacement eq place.idEmplacement)
            }
        }
        return result > 0
    }

    override fun deletePlace(caristeId: Int, colisId: Int, emplacementId: Int): Boolean {
        val result = database.delete(Places) {
            (it.idCariste eq caristeId) and
                    (it.idColis eq colisId) and
                    (it.idEmplacement eq emplacementId)
        }
        return result > 0
    }

    override fun createPlace(caristeId: Int, colisId: Int, emplacementId: Int): Boolean {
        // Vérifier si le colis existe toujours
        val colisExists = database.from(Colis)
            .select(Colis.id)
            .where { Colis.id eq colisId }
            .totalRecords > 0

        if (!colisExists) {
            return false
        }

        // Vérifier si le colis est déjà placé quelque part
        val existingPlace = getPlaceByColis(colisId)

        if (existingPlace != null) {
            // Si le colis est déjà dans cet emplacement, considérer l'opération comme réussie
            if (existingPlace.idEmplacement == emplacementId) {
                return true
            }

            // Sinon, supprimer l'ancienne place
            deletePlace(existingPlace.idCariste, existingPlace.idColis, existingPlace.idEmplacement)
        }

        // Créer la nouvelle place
        val place = Place(
            idCariste = caristeId,
            idColis = colisId,
            idEmplacement = emplacementId,
            dateDepot = LocalDate.now()
        )

        return createPlace(place)
    }

    override fun getPlaceByColis(colisId: Int): Place? {
        return database.from(Places)
            .innerJoin(Colis, on = Places.idColis eq Colis.id) // Utilisation de innerJoin
            .select()
            .where { Places.idColis eq colisId }
            .orderBy(Places.dateDepot.desc())
            .map { row ->
                Place(
                    idCariste = row[Places.idCariste]!!,
                    idColis = row[Places.idColis]!!,
                    idEmplacement = row[Places.idEmplacement]!!,
                    dateDepot = row[Places.dateDepot]
                )
            }
            .firstOrNull()
    }

    override fun getHistoriquePlaces(colisId: Int): List<Place> {
        return database.from(Places)
            .innerJoin(Colis, on = Places.idColis eq Colis.id) // Utilisation de innerJoin
            .select()
            .where { Places.idColis eq colisId }
            .orderBy(Places.dateDepot.desc())
            .map { row ->
                Place(
                    idCariste = row[Places.idCariste]!!,
                    idColis = row[Places.idColis]!!,
                    idEmplacement = row[Places.idEmplacement]!!,
                    dateDepot = row[Places.dateDepot]
                )
            }
    }
}