package ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import models.Allee
import models.Colonne
import models.Emplacement
import org.koin.compose.koinInject
import routing.Router
import routing.Routes
import services.AlleeService
import services.ColonneService
import services.EmplacementService

@Composable
fun WarehouseScreen() {
    val router = koinInject<Router>()
    val alleeService = koinInject<AlleeService>()
    val colonneService = koinInject<ColonneService>()
    val emplacementService = koinInject<EmplacementService>()
    val scope = rememberCoroutineScope()

    // États pour les listes d'entités
    var allees by remember { mutableStateOf<List<Allee>>(emptyList()) }
    var colonnes by remember { mutableStateOf<List<Colonne>>(emptyList()) }
    var emplacements by remember { mutableStateOf<List<Emplacement>>(emptyList()) }

    // États pour le formulaire d'ajout d'allée
    var nouveauNumeroAllee by remember { mutableStateOf("") }

    // États pour le formulaire d'ajout de colonne
    var nouveauNumeroColonne by remember { mutableStateOf("") }
    var alleeSelectionneeId by remember { mutableStateOf<Int?>(null) }

    // États pour le formulaire d'ajout d'emplacement
    var volumeMax by remember { mutableStateOf("") }
    var poidsMax by remember { mutableStateOf("") }
    var colonneSelectionneeId by remember { mutableStateOf<Int?>(null) }
    var niveauSelectionne by remember { mutableStateOf("0") }

    // État pour suivre l'onglet actif
    var ongletActif by remember { mutableStateOf(0) }

    // État pour l'allée dépliée
    var expandedAllee by remember { mutableStateOf<Int?>(null) }

    // Message d'erreur
    var messageErreur by remember { mutableStateOf<String?>(null) }

    // Chargement des données
    LaunchedEffect(Unit) {
        allees = alleeService.getAllAllees()
        colonnes = colonneService.getAllColonnes()
        emplacements = emplacementService.getEmplacementsWithDetails()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Structure d'Entrepôt") },
                backgroundColor = Color(0xFF7C3AED),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { router.navigateTo(Routes.HOME) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Onglets
                TabRow(
                    selectedTabIndex = ongletActif,
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF7C3AED)
                ) {
                    Tab(
                        selected = ongletActif == 0,
                        onClick = { ongletActif = 0 },
                        text = { Text("Allées") }
                    )
                    Tab(
                        selected = ongletActif == 1,
                        onClick = { ongletActif = 1 },
                        text = { Text("Colonnes") }
                    )
                    Tab(
                        selected = ongletActif == 2,
                        onClick = { ongletActif = 2 },
                        text = { Text("Emplacements") }
                    )
                    Tab(
                        selected = ongletActif == 3,
                        onClick = { ongletActif = 3 },
                        text = { Text("Vue Hiérarchique") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Affichage du message d'erreur si présent
                messageErreur?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Contenu principal qui occupe l'espace disponible
                when (ongletActif) {
                    0 -> { // Gestion des allées
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Formulaire d'ajout d'allée
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Ajouter une nouvelle allée",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937)
                                    )
                                    OutlinedTextField(
                                        value = nouveauNumeroAllee,
                                        onValueChange = { nouveauNumeroAllee = it },
                                        label = { Text("Numéro d'allée") },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                    )
                                    Button(
                                        onClick = {
                                            val numero = nouveauNumeroAllee.toIntOrNull()
                                            if (numero != null && numero > 0) {
                                                scope.launch {
                                                    alleeService.creerAllee(numero)
                                                    allees = alleeService.getAllAllees()
                                                    nouveauNumeroAllee = ""
                                                    messageErreur = null
                                                }
                                            } else {
                                                messageErreur = "Le numéro d'allée doit être un nombre positif."
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7C3AED), contentColor = Color.White),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Ajouter")
                                    }
                                }
                            }

                            // Liste des allées existantes
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Allées existantes:",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    // Liste défilable
                                    LazyColumn {
                                        items(allees) { allee ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                elevation = 2.dp
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text("Allée n°${allee.numeroAllee}")
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                allee.id?.let {
                                                                    alleeService.supprimerAllee(it)
                                                                    allees = alleeService.getAllAllees()
                                                                }
                                                            }
                                                        },
                                                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                                                    ) {
                                                        Text("Supprimer")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 -> { // Gestion des colonnes
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Formulaire d'ajout de colonne
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Ajouter une nouvelle colonne",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937)
                                    )

                                    // Sélection de l'allée
                                    Text("Sélectionner une allée:", modifier = Modifier.padding(top = 8.dp))
                                    LazyColumn(
                                        modifier = Modifier
                                            .height(100.dp)
                                            .fillMaxWidth()
                                            .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                                    ) {
                                        items(allees) { allee ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { alleeSelectionneeId = allee.id }
                                                    .background(
                                                        if (alleeSelectionneeId == allee.id)
                                                            Color(0xFF7C3AED).copy(alpha = 0.12f)
                                                        else Color.Transparent
                                                    )
                                                    .padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Allée n°${allee.numeroAllee}")
                                            }
                                        }
                                    }

                                    OutlinedTextField(
                                        value = nouveauNumeroColonne,
                                        onValueChange = { nouveauNumeroColonne = it },
                                        label = { Text("Numéro de colonne") },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                    )

                                    Button(
                                        onClick = {
                                            val numero = nouveauNumeroColonne.toIntOrNull()
                                            if (numero != null && numero > 0 && alleeSelectionneeId != null) {
                                                scope.launch {
                                                    colonneService.creerColonne(numero, alleeSelectionneeId!!)
                                                    colonnes = colonneService.getAllColonnes()
                                                    nouveauNumeroColonne = ""
                                                    messageErreur = null
                                                }
                                            } else {
                                                messageErreur = "Veuillez fournir un numéro de colonne valide et sélectionner une allée."
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7C3AED), contentColor = Color.White),
                                        modifier = Modifier.align(Alignment.End),
                                        enabled = alleeSelectionneeId != null
                                    ) {
                                        Text("Ajouter")
                                    }
                                }
                            }

                            // Liste des colonnes existantes
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Colonnes existantes:",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    // Liste défilable
                                    LazyColumn {
                                        items(colonnes) { colonne ->
                                            val allee = allees.find { it.id == colonne.idAllee }
                                            Card(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                elevation = 2.dp
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Text("Colonne n°${colonne.numeroColonne}")
                                                        Text("Allée n°${allee?.numeroAllee ?: "?"}", fontSize = 12.sp)
                                                    }
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                colonne.id?.let {
                                                                    colonneService.supprimerColonne(it)
                                                                    colonnes = colonneService.getAllColonnes()
                                                                }
                                                            }
                                                        },
                                                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                                                    ) {
                                                        Text("Supprimer")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    2 -> { // Gestion des emplacements
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Formulaire d'ajout d'emplacement
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Ajouter un nouvel emplacement",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937)
                                    )

                                    // Sélection de la colonne
                                    Text("Sélectionner une colonne:", modifier = Modifier.padding(top = 8.dp))
                                    LazyColumn(
                                        modifier = Modifier
                                            .height(100.dp)
                                            .fillMaxWidth()
                                            .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                                    ) {
                                        items(colonnes) { colonne ->
                                            val allee = allees.find { it.id == colonne.idAllee }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { colonneSelectionneeId = colonne.id }
                                                    .background(
                                                        if (colonneSelectionneeId == colonne.id)
                                                            Color(0xFF7C3AED).copy(alpha = 0.12f)
                                                        else Color.Transparent
                                                    )
                                                    .padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Colonne n°${colonne.numeroColonne} (Allée n°${allee?.numeroAllee ?: "?"})")
                                            }
                                        }
                                    }

                                    // Sélection du niveau - NOUVELLE IMPLÉMENTATION
                                    Text("Niveau:", modifier = Modifier.padding(top = 8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    ) {
                                        // Ligne grise pour représenter la barre complète
                                        Box(
                                            modifier = Modifier
                                                .height(4.dp)
                                                .fillMaxWidth()
                                                .background(Color(0xFFE5E7EB))
                                                .align(Alignment.Center)
                                        )

                                        // Points de niveau avec espacement égal
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            for (i in 0..3) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(20.dp)
                                                            .clip(CircleShape)
                                                            .background(
                                                                if (niveauSelectionne.toIntOrNull() == i) Color(0xFF7C3AED)
                                                                else Color(0xFFD1D5DB)
                                                            )
                                                            .clickable { niveauSelectionne = i.toString() }
                                                    )
                                                    Text(
                                                        text = "$i",
                                                        fontSize = 12.sp,
                                                        modifier = Modifier.padding(top = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Niveau: $niveauSelectionne",
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )

                                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                                        OutlinedTextField(
                                            value = volumeMax,
                                            onValueChange = { volumeMax = it },
                                            label = { Text("Volume Max (cm³)") },
                                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                                        )
                                        OutlinedTextField(
                                            value = poidsMax,
                                            onValueChange = { poidsMax = it },
                                            label = { Text("Poids Max (kg)") },
                                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            val volume = volumeMax.toIntOrNull()
                                            val poids = poidsMax.toIntOrNull()
                                            val niveau = niveauSelectionne.toIntOrNull()

                                            if (volume != null && poids != null && niveau != null &&
                                                volume > 0 && poids > 0 && niveau in 0..3 &&
                                                colonneSelectionneeId != null) {

                                                // Vérifier si un emplacement existe déjà à cette position (même colonne et même niveau)
                                                val emplacementExistant = emplacements.any {
                                                    it.idColonne == colonneSelectionneeId && it.niveau == niveau
                                                }

                                                if (emplacementExistant) {
                                                    messageErreur = "Un emplacement existe déjà au niveau $niveau pour cette colonne."
                                                } else {
                                                    scope.launch {
                                                        emplacementService.creerEmplacement(volume, poids, colonneSelectionneeId!!, niveau)
                                                        emplacements = emplacementService.getEmplacementsWithDetails()
                                                        volumeMax = ""
                                                        poidsMax = ""
                                                        niveauSelectionne = "0"
                                                        messageErreur = null
                                                    }
                                                }
                                            } else {
                                                messageErreur = "Veuillez fournir des valeurs valides et sélectionner une colonne."
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7C3AED), contentColor = Color.White),
                                        modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                                        enabled = colonneSelectionneeId != null
                                    ) {
                                        Text("Ajouter")
                                    }
                                }
                            }

                            // Liste des emplacements existants
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Emplacements existants:",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    // Liste défilable
                                    LazyColumn {
                                        items(emplacements) { emplacement ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                elevation = 2.dp
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Text("Allée n°${emplacement.numeroAllee ?: "?"} - Colonne n°${emplacement.numeroColonne ?: "?"} - Niveau ${emplacement.niveau}")
                                                        Text("Volume: ${emplacement.volumeMax} cm³, Poids: ${emplacement.poidsMax} kg", fontSize = 12.sp)
                                                    }
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                emplacement.id?.let {
                                                                    emplacementService.supprimerEmplacement(it)
                                                                    emplacements = emplacementService.getEmplacementsWithDetails()
                                                                }
                                                            }
                                                        },
                                                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                                                    ) {
                                                        Text("Supprimer")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    3 -> { // Vue hiérarchique
                        Column(modifier = Modifier.fillMaxSize()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Vue hiérarchique de l'entrepôt",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1F2937),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    // Vue hiérarchique dans une LazyColumn
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        // Parcourir toutes les allées
                                        items(allees.sortedBy { it.numeroAllee }) { allee ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                elevation = 2.dp
                                            ) {
                                                Column(modifier = Modifier.fillMaxWidth()) {
                                                    // En-tête de l'allée (toujours visible)
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable { expandedAllee = if (expandedAllee == allee.id) null else allee.id }
                                                            .background(Color(0xFF7C3AED).copy(alpha = 0.1f))
                                                            .padding(8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            "Allée n°${allee.numeroAllee}",
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                    }

                                                    // Contenu déplié
                                                    if (expandedAllee == allee.id) {
                                                        val colonnesAllee = colonnes.filter { it.idAllee == allee.id }.sortedBy { it.numeroColonne }

                                                        // S'il n'y a pas de colonnes
                                                        if (colonnesAllee.isEmpty()) {
                                                            Text(
                                                                "Aucune colonne dans cette allée",
                                                                modifier = Modifier.padding(8.dp),
                                                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                                                fontSize = 14.sp
                                                            )
                                                        }

                                                        // Parcourir les colonnes de cette allée
                                                        colonnesAllee.forEach { colonne ->
                                                            // En-tête de la colonne
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 4.dp),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Text(
                                                                    "Colonne n°${colonne.numeroColonne}",
                                                                    fontWeight = FontWeight.Medium,
                                                                    modifier = Modifier.weight(1f)
                                                                )
                                                            }

                                                            // Liste des emplacements de cette colonne
                                                            val emplacementsColonne = emplacements
                                                                .filter { it.idColonne == colonne.id }
                                                                .sortedBy { it.niveau }

                                                            // S'il n'y a pas d'emplacements
                                                            if (emplacementsColonne.isEmpty()) {
                                                                Text(
                                                                    "Aucun emplacement dans cette colonne",
                                                                    modifier = Modifier.padding(start = 32.dp, bottom = 4.dp),
                                                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                                                    fontSize = 12.sp
                                                                )
                                                            }

                                                            // Afficher tous les emplacements
                                                            emplacementsColonne.forEach { emplacement ->
                                                                Row(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth()
                                                                        .padding(start = 32.dp, end = 8.dp, bottom = 4.dp),
                                                                    verticalAlignment = Alignment.CenterVertically
                                                                ) {
                                                                    Row(modifier = Modifier.weight(1f)) {
                                                                        Text(
                                                                            "Niveau ${emplacement.niveau}",
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight.Medium,
                                                                            modifier = Modifier.width(80.dp)
                                                                        )
                                                                        Text(
                                                                            "Volume: ${emplacement.volumeMax} cm³",
                                                                            fontSize = 12.sp,
                                                                            modifier = Modifier.width(120.dp)
                                                                        )
                                                                        Text(
                                                                            "Poids: ${emplacement.poidsMax} kg",
                                                                            fontSize = 12.sp
                                                                        )
                                                                    }
                                                                }
                                                            }

                                                            // Séparateur entre les colonnes
                                                            if (colonnesAllee.last() != colonne) {
                                                                Divider(
                                                                    modifier = Modifier.padding(start = 32.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                                                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}