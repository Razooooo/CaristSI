package routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

enum class Routes(val route: String) {
    LOGIN("LOGIN"),
    HOME("HOME"),
    CARIST("CARIST"),
    WAREHOUSE("WAREHOUSE"),
    PACKAGES("PACKAGES"),
    INVENTORY("INVENTORY")  // Nouvelle route pour le suivi d'inventaire
}

class Router {
    var currentRoute by mutableStateOf<Routes>(Routes.LOGIN)
        private set

    fun navigateTo(route: Routes) {
        currentRoute = route
    }
}