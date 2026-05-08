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

// ─── Validation helpers ────────────────────────────────────────────────────────

private fun isValidEmail(email: String): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

private fun isValidPassword(password: String): Boolean =
    password.length >= 6

private fun isValidDueDate(date: String): Boolean {
    // Expects MM/DD/YYYY
    val regex = Regex("""^(0[1-9]|1[0-2])/(0[1-9]|[12]\d|3[01])/\d{4}$""")
    return regex.matches(date.trim())
}

// ─── RegisterScreen ────────────────────────────────────────────────────────────

@Composable
fun RegisterScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedStage by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    // Track whether user has touched each field (so errors only show after interaction)
    var nameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var dueDateTouched by remember { mutableStateOf(false) }
    var stageTouched by remember { mutableStateOf(false) }

    // Error messages — empty string means no error
    val nameError = if (nameTouched && fullName.isBlank()) "Full name is required" else ""
    val emailError = when {
        emailTouched && email.isBlank() -> "Email is required"
        emailTouched && !isValidEmail(email) -> "Enter a valid email address"
        else -> ""
    }
    val passwordError = when {
        passwordTouched && password.isBlank() -> "Password is required"
        passwordTouched && !isValidPassword(password) -> "Password must be at least 6 characters"
        else -> ""
    }
    val stageError = if (stageTouched && selectedStage.isBlank()) "Please select your current stage" else ""
    val dueDateError = when {
        selectedStage == "Pregnant" && dueDateTouched && dueDate.isBlank() -> "Due date is required"
        selectedStage == "Pregnant" && dueDateTouched && !isValidDueDate(dueDate) -> "Enter date as MM/DD/YYYY"
        else -> ""
    }

    // Overall form validity gate
    val isFormValid = fullName.isNotBlank()
            && isValidEmail(email)
            && isValidPassword(password)
            && selectedStage.isNotBlank()
            && (selectedStage != "Pregnant" || isValidDueDate(dueDate))

    val stages = listOf("Trying to Conceive", "Pregnant", "New Mom (0-12 months)", "Experienced Mom")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
            .verticalScroll(rememberScrollState())
    ) {
        // Top header — unchanged
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
                onValueChange = {
                    fullName = it
                    nameTouched = true
                },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Outlined.Person, null, tint = Sage) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                isError = nameError.isNotEmpty(),
                supportingText = {
                    if (nameError.isNotEmpty()) Text(nameError, color = ErrorRed)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    focusedLabelColor = Sage,
                    unfocusedBorderColor = CreamDark,
                    errorBorderColor = ErrorRed,
                    errorLabelColor = ErrorRed
                )
            )
            Spacer(Modifier.height(14.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true
                },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Outlined.Email, null, tint = Sage) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError.isNotEmpty(),
                supportingText = {
                    if (emailError.isNotEmpty()) Text(emailError, color = ErrorRed)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    focusedLabelColor = Sage,
                    unfocusedBorderColor = CreamDark,
                    errorBorderColor = ErrorRed,
                    errorLabelColor = ErrorRed
                )
            )
            Spacer(Modifier.height(14.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordTouched = true
                },
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
                isError = passwordError.isNotEmpty(),
                supportingText = {
                    if (passwordError.isNotEmpty()) Text(passwordError, color = ErrorRed)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Sage,
                    focusedLabelColor = Sage,
                    unfocusedBorderColor = CreamDark,
                    errorBorderColor = ErrorRed,
                    errorLabelColor = ErrorRed
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
                            onClick = {
                                selectedStage = stage
                                stageTouched = true
                                // Reset due date touch when switching away from Pregnant
                                if (stage != "Pregnant") dueDateTouched = false
                            },
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

            // Stage error shown below the grid
            if (stageError.isNotEmpty()) {
                Text(
                    stageError,
                    color = ErrorRed,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(Modifier.height(4.dp))
            }

            // Due date (only for pregnant)
            if (selectedStage == "Pregnant") {
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = {
                        dueDate = it
                        dueDateTouched = true
                    },
                    label = { Text("Due Date (MM/DD/YYYY)") },
                    leadingIcon = { Icon(Icons.Outlined.CalendarToday, null, tint = Sage) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = dueDateError.isNotEmpty(),
                    supportingText = {
                        if (dueDateError.isNotEmpty()) Text(dueDateError, color = ErrorRed)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Sage,
                        focusedLabelColor = Sage,
                        unfocusedBorderColor = CreamDark,
                        errorBorderColor = ErrorRed,
                        errorLabelColor = ErrorRed
                    )
                )
                Spacer(Modifier.height(14.dp))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    // Touch all fields to reveal any remaining errors
                    nameTouched = true
                    emailTouched = true
                    passwordTouched = true
                    stageTouched = true
                    if (selectedStage == "Pregnant") dueDateTouched = true

                    if (isFormValid) {
                        navController.navigate(ROUT_HOME) {
                            popUpTo(ROUT_REGISTER) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blush,
                    disabledContainerColor = Blush.copy(alpha = 0.5f)
                )
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