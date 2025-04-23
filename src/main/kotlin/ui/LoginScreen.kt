package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import controller.LoginController
import org.koin.compose.koinInject

@Composable
fun LogoIcon() {
    // Logo représentant un cariste
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icône stylisée représentant un chariot élévateur/cariste
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFF39C12)), // Orange pour représenter les engins de manutention
            contentAlignment = Alignment.Center
        ) {
            // Logo stylisé de chariot élévateur (représenté par une forme simple)
            Canvas(modifier = Modifier.size(60.dp)) {
                // Base du chariot
                drawRect(
                    color = Color.White,
                    topLeft = Offset(size.width * 0.2f, size.height * 0.6f),
                    size = Size(size.width * 0.6f, size.height * 0.25f)
                )

                // Mât du chariot
                drawRect(
                    color = Color.White,
                    topLeft = Offset(size.width * 0.15f, size.height * 0.2f),
                    size = Size(size.width * 0.15f, size.height * 0.6f)
                )

                // Fourches
                drawRect(
                    color = Color.White,
                    topLeft = Offset(0f, size.height * 0.4f),
                    size = Size(size.width * 0.15f, size.height * 0.05f)
                )
                drawRect(
                    color = Color.White,
                    topLeft = Offset(0f, size.height * 0.55f),
                    size = Size(size.width * 0.15f, size.height * 0.05f)
                )

                // Roues
                drawCircle(
                    color = Color.White,
                    radius = size.width * 0.08f,
                    center = Offset(size.width * 0.25f, size.height * 0.9f)
                )
                drawCircle(
                    color = Color.White,
                    radius = size.width * 0.08f,
                    center = Offset(size.width * 0.75f, size.height * 0.9f)
                )
            }
        }

        Text(
            "Carist-si",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun StyledTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        singleLine = true,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = label, tint = Color(0xFF3498DB)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() },
            onNext = { onImeAction() },
            onGo = { onImeAction() }
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF3498DB),
            focusedLabelColor = Color(0xFF3498DB),
            cursorColor = Color(0xFF3498DB)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
@Preview
fun LoginScreen() {
    var identifiant by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val loginController: LoginController = koinInject()
    val focusManager = LocalFocusManager.current

    // Fonction de login à utiliser pour les deux méthodes (bouton et Enter)
    val attemptLogin = {
        if (identifiant.text.isNotEmpty() && password.text.isNotEmpty()) {
            loginController.loginUser(identifiant.text, password.text)
        } else {
            errorMessage = "Veuillez remplir tous les champs"
        }
    }

    // Modifier avec le gestionnaire de clavier pour le formulaire entier
    val keyboardModifier = Modifier.onPreviewKeyEvent { keyEvent ->
        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) {
            attemptLogin()
            focusManager.clearFocus()
            true
        } else {
            false
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .fillMaxWidth()
            .then(keyboardModifier),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        LogoIcon()

        Spacer(modifier = Modifier.height(8.dp))

        // Titre
        Text(
            "Connexion",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Champs de connexion
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StyledTextField(
                    value = identifiant,
                    onValueChange = { identifiant = it },
                    label = "Identifiant",
                    icon = Icons.Default.AccountCircle,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )

                StyledTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Mot de passe",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        attemptLogin()
                        focusManager.clearFocus()
                    }
                )

                errorMessage?.let {
                    Text(
                        it,
                        color = MaterialTheme.colors.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = attemptLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3498DB)),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        "Se connecter",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}