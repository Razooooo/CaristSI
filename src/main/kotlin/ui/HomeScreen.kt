package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import routing.Routes

@Composable
@Preview
fun HomeScreen(onNavigate: (route: Routes) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3F4F6),
                        Color(0xFFE5E7EB)
                    )
                )
            )
    ) {
        // Bouton de déconnexion en haut à droite
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            LogoutButton(
                onClick = { onNavigate(Routes.LOGIN) }
            )
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // En-tête avec titre et sous-titre
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    "Carist-Si",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    "Système de gestion d'entrepôt",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Section des cartes
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Carte pour la gestion des colis
                MenuButton(
                    title = "Gestion des colis",
                    description = "Ajouter, rechercher et gérer les colis dans l'entrepôt",
                    backgroundColor = Color(0xFF2563EB),
                    onClick = { onNavigate(Routes.PACKAGES) }
                )

                // Carte pour la gestion des emplacements
                MenuButton(
                    title = "Gestion des emplacements",
                    description = "Configurer et organiser les zones de stockage",
                    backgroundColor = Color(0xFF059669),
                    onClick = { onNavigate(Routes.WAREHOUSE) }
                )

                // Carte supplémentaire (optionnelle) pour le suivi d'inventaire
                MenuButton(
                    title = "Suivi d'inventaire",
                    description = "Consulter l'historique des placements de colis",
                    backgroundColor = Color(0xFF7C3AED),
                    onClick = { onNavigate(Routes.INVENTORY) }
                )
            }

            // Information de version
            Text(
                "v1.0.0",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFDC2626), // Rouge pour déconnexion
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Déconnexion",
                tint = Color.White
            )
            Text(
                "Déconnexion",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    description: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}