package com.ironledger.app.ui.screens

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ironledger.app.ui.components.IronLedgerCard
import com.ironledger.app.ui.components.SectionLabel
import com.ironledger.app.ui.components.StaggeredItem
import com.ironledger.app.ui.theme.Dark900
import com.ironledger.app.ui.theme.PrimaryVibrant
import com.ironledger.app.ui.theme.TealNeon
import com.ironledger.app.ui.viewmodel.AppLockViewModel

@Composable
fun AppLockScreen(
    onUnlocked: () -> Unit,
    viewModel: AppLockViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var pinInput by remember { mutableStateOf("") }
    var confirmPinInput by remember { mutableStateOf("") }
    var isConfirmStage by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isUnlocked) {
        if (uiState.isUnlocked) {
            onUnlocked()
        }
    }

    // Auto-submit PIN when 4 digits are entered in Unlock mode
    LaunchedEffect(pinInput) {
        if (uiState.isPinSet && !isConfirmStage && pinInput.length == 4) {
            viewModel.validatePin(pinInput)
            pinInput = ""
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Dark900
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StaggeredItem(index = 0) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = PrimaryVibrant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            StaggeredItem(index = 1) {
                Text(
                    text = if (!uiState.isPinSet) {
                        if (isConfirmStage) "Confirm your PIN" else "Set up your PIN"
                    } else "Enter PIN",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            StaggeredItem(index = 2) {
                Text(
                    text = if (!uiState.isPinSet) "Secure your data locally" else "Private & Secure",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            StaggeredItem(index = 3) {
                IronLedgerCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = if (isConfirmStage) confirmPinInput else pinInput,
                        onValueChange = { 
                            if (it.length <= 4) {
                                if (isConfirmStage) confirmPinInput = it else pinInput = it
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = { Text("0000", color = Color.White.copy(alpha = 0.2f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealNeon,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            letterSpacing = 12.sp
                        )
                    )

                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            StaggeredItem(index = 4) {
                if (!uiState.isPinSet) {
                    Button(
                        onClick = {
                            if (isConfirmStage) {
                                if (pinInput == confirmPinInput) {
                                    viewModel.setupPin(pinInput)
                                } else {
                                    // Handle mismatch (optional: clear and reset)
                                    confirmPinInput = ""
                                    isConfirmStage = false
                                    pinInput = ""
                                }
                            } else if (pinInput.length == 4) {
                                isConfirmStage = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealNeon),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        enabled = if (isConfirmStage) confirmPinInput.length == 4 else pinInput.length == 4
                    ) {
                        Text(
                            text = if (isConfirmStage) "Confirm & Save" else "Next",
                            color = Dark900,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // Fingerprint button if biometric is available
                    val biometricManager = remember { BiometricManager.from(context) }
                    val canAuthenticate = remember {
                        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
                    }

                    if (canAuthenticate) {
                        IconButton(
                            onClick = {
                                showBiometricPrompt(context as FragmentActivity) {
                                    viewModel.onBiometricSuccess()
                                }
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "Biometric Unlock",
                                tint = TealNeon,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Unlock IronLedger")
        .setSubtitle("Use your biometric credential")
        .setNegativeButtonText("Use PIN")
        .build()

    biometricPrompt.authenticate(promptInfo)
}
