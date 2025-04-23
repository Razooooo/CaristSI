import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import controller.LoginController
import ktorm.Allees
import ktorm.Cariste
import ktorm.allees
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.dsl.*
import org.ktorm.entity.toList
import repository.*
import routing.Router
import routing.Routes
import services.*
import ui.HomeScreen
import ui.InventoryTrackingScreen
import ui.LoginScreen
import ui.PackagesScreen
import ui.WarehouseScreen

@Composable
@Preview
fun App() {
    val router: Router = koinInject()
    MaterialTheme {
        Surface {
            when (router.currentRoute) {
                Routes.LOGIN -> LoginScreen()

                Routes.HOME -> HomeScreen { route ->
                    router.navigateTo(route)
                }

                Routes.WAREHOUSE -> {
                    WarehouseScreen()
                }

                Routes.CARIST -> {
                    // TODO: Implémenter l'écran des caristes
                }

                Routes.PACKAGES -> {
                    PackagesScreen()
                }

                Routes.INVENTORY -> {
                    InventoryTrackingScreen()
                }

                else -> {
                    // TODO: Gestion par défaut
                }
            }
        }
    }
}

fun main() = application {
    // Définition d'un module Koin
    val appModule = module {
        // Base de données
        single {
            Database.connect(
                url = "jdbc:mysql://localhost:3306/carist-si",
                user = "root",
                password = null
            )
        }

        // Repositories
        single { WarehouseRepository() }
        single<ColisRepository> { ColisRepositoryImpl(get()) }
        single<AlleeRepository> { AlleeRepositoryImpl(get()) }
        single<ColonneRepository> { ColonneRepositoryImpl(get()) }
        single<EmplacementRepository> { EmplacementRepositoryImpl(get()) }
        single<PlaceRepository> { PlaceRepositoryImpl(get()) }

        // Services
        single { ColisService(get()) }
        single { AlleeService() }
        single { ColonneService() }
        single { EmplacementService() }
        single { PlaceService() }

        // Autres
        single { LoginController() }
        single { Router() }
    }

    // Démarrage de Koin
    startKoin() {
        modules(appModule)
    }

    val database: Database = org.koin.java.KoinJavaComponent.get(Database::class.java)
    println("Connexion à la base de données réussie : ${database.name}");

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}