import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import controller.LoginController
import ktorm.Allees
import ktorm.Caristes
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
import repository.WarehouseRepository
import routing.Router
import routing.Routes
import ui.HomeScreen
import ui.LoginScreen
import ui.WarehouseScreen

@Composable
@Preview
fun App() {
    val router:Router = koinInject()
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
//                Routes.CARIST -> {
//
//                }
//                Routes.PACKAGES -> {
//
//                }
                else -> {
                    // TODO replace with commented code
                }
            }


        }
    }
}

fun main() = application {
// Définition d'un module Koin
    val appModule = module {
        single {
            Database.connect(
                url = "jdbc:mysql://localhost:3306/carist-si",
                user = "root",
                password = null
            )
        }
        single { WarehouseRepository() }
        single {
            LoginController()
        }
        single {
            Router()
        }
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
