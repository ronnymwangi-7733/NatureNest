package com.ronny.naturenest.ui.screens.auth


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ronny.naturenest.navigation.ROUT_HOME
import com.ronny.naturenest.navigation.ROUT_LOGIN
import com.ronny.naturenest.navigation.ROUT_REGISTER
import com.ronny.naturenest.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val firebaseAuth = remember { FirebaseAuth.getInstance() }

    // Google Sign-In client
    val googleSignInClient = remember {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("313433710979-1km6gi1smnbr1dn0hi19ggvi524m8qbh.apps.googleusercontent.com") // replace from Firebase
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, options)
    }

    // Google launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {

                    val name = firebaseAuth.currentUser?.displayName ?: "Mama"

                    navController.navigate("home/$name") {
                        popUpTo(ROUT_LOGIN) { inclusive = true }
                    }
                }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
            .verticalScroll(rememberScrollState())
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BlushLight, SurfaceCream)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌿", fontSize = 52.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "NatureNest",
                    style = MaterialTheme.typography.displaySmall,
                    color = WarmBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Welcome back, Mama 💕",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        }

        // FORM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
        ) {

            Text(
                "Sign In",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Outlined.Email, null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible)
                                Icons.Outlined.VisibilityOff
                            else Icons.Outlined.Visibility,
                            null
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // EMAIL LOGIN
            Button(
                onClick = {
                    navController.navigate(ROUT_HOME) {
                        popUpTo(ROUT_LOGIN) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blush)
            ) {
                Text("Sign In", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))

            // GOOGLE SIGN IN
            OutlinedButton(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Continue with Google 🔵")
            }

            Spacer(Modifier.height(20.dp))

            // REGISTER NAV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ")
                TextButton(
                    onClick = { navController.navigate(ROUT_REGISTER) }
                ) {
                    Text("Sign Up")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreen() {
    LoginScreen(navController = NavController(LocalContext.current))
}