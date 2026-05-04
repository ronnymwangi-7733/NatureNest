package com.ronny.naturenest.ui.screens.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.navigation.ROUT_HOME
import com.ronny.naturenest.navigation.ROUT_LOGIN
import com.ronny.naturenest.navigation.ROUT_REGISTER
import com.ronny.naturenest.ui.theme.*

@Composable
fun RegisterScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedStage by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    val stages = listOf("Trying to Conceive", "Pregnant", "New Mom (0-12 months)", "Experienced Mom")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
            .verticalScroll(rememberScrollState())
    ) {
        // Top header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(colors = listOf(SageLight, SurfaceCream))
                )
                .padding(28.dp)
        ) {
            Column {
                Text("🌸", fontSize = 40.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Create Account",
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Join thousands of mamas on NatureNest",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 8.dp)
        ) {

            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Outlined.Person, null, tint = Sage) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    focusedLabelColor = Sage,
                    unfocusedBorderColor = CreamDark
                )
            )
            Spacer(Modifier.height(14.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Outlined.Email, null, tint = Sage) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    focusedLabelColor = Sage,
                    unfocusedBorderColor = CreamDark
                )
            )
            Spacer(Modifier.height(14.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = Sage) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            null, tint = TextHint
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    focusedLabelColor = Sage,
                    unfocusedBorderColor = CreamDark
                )
            )
            Spacer(Modifier.height(20.dp))

            // Stage selector
            Text(
                "What's your current stage?",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(10.dp))

            stages.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { stage ->
                        val isSelected = selectedStage == stage
                        Surface(
                            onClick = { selectedStage = stage },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SageLight else SurfaceWhite,
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) Sage else CreamDark
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    when (stage) {
                                        "Trying to Conceive" -> "🌱"
                                        "Pregnant" -> "🤰"
                                        "New Mom (0-12 months)" -> "👶"
                                        else -> "💕"
                                    },
                                    fontSize = 24.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    stage,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) SageDark else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            // Due date (only for pregnant)
            if (selectedStage == "Pregnant") {
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date (MM/DD/YYYY)") },
                    leadingIcon = { Icon(Icons.Outlined.CalendarToday, null, tint = Sage) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Sage,
                        focusedLabelColor = Sage,
                        unfocusedBorderColor = CreamDark
                    )
                )
                Spacer(Modifier.height(14.dp))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(ROUT_HOME) {
                        popUpTo(ROUT_REGISTER) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Sage)
            ) {
                Text(
                    "Join NatureNest 🌿",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account? ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                TextButton(
                    onClick = { navController.navigate(ROUT_LOGIN) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Sign In", color = Sage, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = rememberNavController())
}