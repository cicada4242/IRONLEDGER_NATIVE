package com.ironledger.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironledger.app.ui.theme.*
import kotlinx.coroutines.delay

// ============================================================
// REUSABLE UI COMPONENTS
// Every new screen should use these instead of building from scratch.
// See native-conversion/06_UX_STANDARDS.md for full documentation.
// ============================================================

/**
 * Wraps content with a staggered fade+slide entry animation.
 * Use for each section/card on a screen.
 *
 * Example:
 *   StaggeredItem(index = 0) { MyCard() }
 *   StaggeredItem(index = 1) { AnotherCard() }
 */
@Composable
fun StaggeredItem(
    index: Int,
    delayPerItem: Long = 80L,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * delayPerItem)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + slideInVertically(
            tween(300, easing = EaseOutCubic),
            initialOffsetY = { it / 4 }
        )
    ) {
        content()
    }
}

/**
 * A card that scales down slightly when pressed. Use for any tappable card.
 * Automatically uses the app's dark card styling.
 */
@Composable
fun PressableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White.copy(alpha = 0.05f),
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "cardScale"
    )

    Surface(
        onClick = onClick,
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        color = Dark800,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

/**
 * A non-clickable card with the standard dark surface styling.
 */
@Composable
fun IronLedgerCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White.copy(alpha = 0.05f),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Dark800,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

/**
 * A gradient-accented card for premium/featured sections.
 */
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    startColor: Color = PrimaryVibrant.copy(alpha = 0.15f),
    endColor: Color = TertiaryVibrant.copy(alpha = 0.05f),
    borderColor: Color = PrimaryVibrant.copy(alpha = 0.2f),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(colors = listOf(startColor, endColor)),
                shape = RoundedCornerShape(20.dp)
            )
            .then(
                Modifier.drawBehind {
                    drawRoundRect(
                        color = borderColor,
                        style = Stroke(width = 1.dp.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx())
                    )
                }
            )
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

/**
 * Animated number counter. Numbers count up from 0 to targetValue.
 */
@Composable
fun AnimatedCounter(
    targetValue: Int,
    suffix: String = "",
    prefix: String = "",
    color: Color = Color.White,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium
) {
    var animatedValue by remember { mutableIntStateOf(0) }
    LaunchedEffect(targetValue) {
        if (targetValue == 0) { animatedValue = 0; return@LaunchedEffect }
        val steps = 30
        val delayMs = 800L / steps
        for (i in 1..steps) {
            animatedValue = (targetValue * i / steps)
            delay(delayMs)
        }
        animatedValue = targetValue
    }
    Text(
        "$prefix$animatedValue$suffix",
        style = style,
        fontWeight = FontWeight.Black,
        color = color
    )
}

/**
 * Circular progress indicator with smooth animation. Use for calorie rings, goals, etc.
 */
@Composable
fun AnimatedCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = TealNeon,
    trackColor: Color = Dark700,
    strokeWidth: Dp = 12.dp,
    size: Dp = 120.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "circularProgress"
    )
    androidx.compose.foundation.Canvas(modifier = modifier.size(size)) {
        val stroke = strokeWidth.toPx()
        drawArc(
            color = trackColor, startAngle = -90f, sweepAngle = 360f,
            useCenter = false, style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            color = color, startAngle = -90f, sweepAngle = 360f * animatedProgress,
            useCenter = false, style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

/**
 * Styled empty state for screens with no data.
 */
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon, contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = PrimaryVibrant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            title, style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            subtitle, style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.4f), textAlign = TextAlign.Center
        )
    }
}

/**
 * Section label in the IronLedger style (uppercase, spaced, accent color).
 */
@Composable
fun SectionLabel(text: String, color: Color = PrimaryVibrant) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = color,
        letterSpacing = 3.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/**
 * Shimmer loading placeholder.
 */
@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
    Box(
        modifier = modifier
            .background(Color.White.copy(alpha = alpha), RoundedCornerShape(12.dp))
    )
}

/**
 * Animated FAB that scales in with a delay.
 */
@Composable
fun AnimatedFAB(
    onClick: () -> Unit,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Add",
    containerColor: Color = PrimaryVibrant,
    delayMs: Long = 400L
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(delayMs); visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(tween(300, easing = EaseOutBack)) + fadeIn(tween(300))
    ) {
        val haptic = LocalHapticFeedback.current
        FloatingActionButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
            containerColor = containerColor,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(icon, contentDescription = contentDescription)
        }
    }
}

/**
 * Standard bottom sheet styling for all "Add" dialogs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IronLedgerBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Dark800,
        contentColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
            )
        },
        content = content
    )
}

/**
 * Stat card used in Weight Log and similar screens.
 */
@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String = "",
    valueColor: Color = Color.White
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Dark800,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = valueColor)
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(unit, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 4.dp))
                }
            }
        }
    }
}
