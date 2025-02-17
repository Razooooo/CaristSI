import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import routing.Router
import routing.Routes
import ui.HomeScreen
import ui.LoginScreen

@Composable
@Preview
fun App(database: Database) {
    val router = remember { Router() }

    MaterialTheme {
        Surface {

            when (router.currentRoute) {
                Routes.LOGIN -> LoginScreen({ email, password ->
                    println("Tentative de connexion avec $email et un mot de passe ${password}")
//                    database.executeQuery("SELECT 1").asIterable().map {
//                        val received = it.getInt(1);
//                        if (received == 1) {
//                            router.navigateTo(route = Routes.HOME)
//                        }
//                    }
                })

                Routes.HOME -> HomeScreen()
            }


        }
    }
}

fun main() = application {

    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/carist-si",
        user = "root",
        password = null
    )

    database.useConnection { connection ->
        val sql = "SELECT 1"
        connection.prepareStatement(sql).use { statement ->
            statement.executeQuery().asIterable().map {
                println("it worked : " + it.getString(1))
            }
        }
    }

    Window(onCloseRequest = ::exitApplication) {
        App(database)
    }
}
