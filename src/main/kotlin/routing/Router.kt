package routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

enum class Routes(val route: String) {
    LOGIN("LOGIN"),
    HOME("HOME")
}

class Router {
    var currentRoute by mutableStateOf<Routes>(Routes.LOGIN)
        private set

    fun navigateTo(route: Routes) {
        currentRoute = route
    }
}