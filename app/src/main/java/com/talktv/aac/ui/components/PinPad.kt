package com.talktv.aac.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.aac.ui.theme.FocusGlowYellow

@Composable
fun PinPad(
    expectedPin: String,
    onPinSuccess: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var enteredPin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .width(420.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF131C2E))
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Caregiver Access",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Enter 4-digit PIN (Default: 1234)",
            fontSize = 15.sp,
            color = Color(0xFF8B9CB5),
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // PIN Dot indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            for (i in 0 until 4) {
                val filled = i < enteredPin.length
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(
                            when {
                                showError -> Color(0xFFEF5350)
                                filled -> FocusGlowYellow
                                else -> Color(0xFF23324C)
                            }
                        )
                )
            }
        }

        if (showError) {
            Text(
                text = "Incorrect PIN, please try again",
                color = Color(0xFFEF5350),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Keypad Grid 1-9, 0, Del, Cancel
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("Cancel", "0", "DEL")
        )

        keys.forEach { rowKeys ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                rowKeys.forEach { key ->
                    PinKeyButton(
                        text = key,
                        onClick = {
                            showError = false
                            when (key) {
                                "Cancel" -> onCancel()
                                "DEL" -> {
                                    if (enteredPin.isNotEmpty()) {
                                        enteredPin = enteredPin.dropLast(1)
                                    }
                                }
                                else -> {
                                    if (enteredPin.length < 4) {
                                        enteredPin += key
                                        if (enteredPin.length == 4) {
                                            if (enteredPin == expectedPin) {
                                                onPinSuccess()
                                            } else {
                                                showError = true
                                                enteredPin = ""
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PinKeyButton(
    text: String,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(width = 100.dp, height = 56.dp)
            .tvFocusGlow(
                isFocused = isFocused,
                glowColor = FocusGlowYellow,
                cornerRadius = 14.dp,
                focusedScale = 1.08f
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isFocused) Color(0xFF2A3A54) else Color(0xFF1B263B)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = if (text.length > 2) 16.sp else 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (text == "Cancel") Color(0xFFEF5350) else Color.White,
            textAlign = TextAlign.Center
        )
    }
}
