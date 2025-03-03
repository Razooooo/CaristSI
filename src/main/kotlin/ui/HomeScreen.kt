package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import routing.Routes

@Composable
@Preview
fun HomeScreen(onNavigate : (route:Routes) -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Menu Principal", fontSize = 24.sp)
        Button(
            onClick = {
                onNavigate(Routes.CARIST)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestion des caristes")
        }

        Button(
            onClick = {
                onNavigate(Routes.PACKAGES)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestion des colis")
        }
        Button(
            onClick = {
                onNavigate(Routes.WAREHOUSE)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestion des emplacements")
        }
    }
}