package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import repository.WarehouseRepository

@Composable
@Preview
fun WarehouseScreen() {
    val warehouseRepository: WarehouseRepository = koinInject()
    val allees = warehouseRepository.getWarehouseData();
    var expandedAllee by remember { mutableStateOf<Int?>(null) }
    Column {
        Text("Gestion des emplacements", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("ID", modifier = Modifier.weight(1f), style = MaterialTheme.typography.subtitle1)
                        Text("Number", modifier = Modifier.weight(1f), style = MaterialTheme.typography.subtitle1)
                    }
                    Divider()
                }

                items(allees) { allee ->
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
                        expandedAllee = if (expandedAllee == allee.id) null else allee.id
                    }) {
                        Text(allee.id.toString(), modifier = Modifier.weight(1f))
                        Text(allee.number.toString(), modifier = Modifier.weight(1f))
                    }
                    if (expandedAllee == allee.id) {
                        Text("Colonne de l'allée ${allee.number} : ")
                        LazyColumn(modifier = Modifier.padding(8.dp).heightIn(max = 150.dp)) {
                            item {
                                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                    Text("Numéro de colonne")
                                }
                            }
                        }
                    }

                    Divider()
                }
            }
        }
    }
}