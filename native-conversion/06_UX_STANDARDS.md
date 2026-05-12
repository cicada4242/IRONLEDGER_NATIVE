# IronLedger Native — UX Standards & Animation Specs

> **This is NOT optional.** Every screen MUST follow these standards.
> The app should feel like Samsung Health / Nike Run Club tier — polished, fluid, alive.

---

## 1. Screen Entry Animations

Every screen's content must animate in when first displayed. Use `AnimatedVisibility` with staggered delays.

### Pattern: Staggered Fade-Slide

```kotlin
@Composable
fun AnimatedScreenContent(content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(400)) +
                slideInVertically(
                    animationSpec = tween(400, easing = EaseOutCubic),
                    initialOffsetY = { it / 8 }  // subtle 12.5% slide up
                )
    ) {
        content()
    }
}
```

### Pattern: Staggered Items in a List

Each card/item in a column should appear with a slight delay after the previous one:

```kotlin
@Composable
fun StaggeredItem(index: Int, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 80L)  // 80ms stagger per item
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
```

**Usage:** Wrap each card in a Column/LazyColumn with `StaggeredItem(index = i)`.

---

## 2. Card Press Feedback

Every tappable card must have a scale-down animation on press.

```kotlin
@Composable
fun PressableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f)
    )

    Surface(
        onClick = onClick,
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        color = Dark800,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        content()
    }
}
```

---

## 3. Number Counter Animations

When displaying stats (calories, weight, streak days), numbers should animate from 0 to their target value:

```kotlin
@Composable
fun AnimatedCounter(targetValue: Int, suffix: String = "", color: Color = Color.White) {
    var animatedValue by remember { mutableIntStateOf(0) }
    LaunchedEffect(targetValue) {
        if (targetValue == 0) { animatedValue = 0; return@LaunchedEffect }
        val duration = 800  // ms
        val steps = 30
        val delay = duration / steps
        for (i in 1..steps) {
            animatedValue = (targetValue * i / steps)
            delay(delay.toLong())
        }
        animatedValue = targetValue
    }
    Text(
        "$animatedValue$suffix",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Black,
        color = color
    )
}
```

---

## 4. Circular Progress Indicators

For calorie rings and progress circles, animate the sweep angle:

```kotlin
@Composable
fun AnimatedCircularProgress(
    progress: Float,  // 0f to 1f
    color: Color = TealNeon,
    trackColor: Color = Dark700,
    strokeWidth: Float = 12f,
    size: Dp = 120.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = EaseOutCubic)
    )
    Canvas(modifier = Modifier.size(size)) {
        // Track
        drawArc(
            color = trackColor, startAngle = -90f, sweepAngle = 360f,
            useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        // Progress
        drawArc(
            color = color, startAngle = -90f, sweepAngle = 360f * animatedProgress,
            useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}
```

---

## 5. Bottom Sheet Animations

All "Add" dialogs (food, workout, measurement, etc.) should use `ModalBottomSheet`:

```kotlin
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
```

---

## 6. Page Transitions

The HorizontalPager already provides swipe animations. For navigating to secondary screens (Settings, Photos, etc.), use:

```kotlin
navController.navigate(route) {
    popUpTo(Destinations.DASHBOARD) { saveState = true }
    launchSingleTop = true
    restoreState = true
}
```

The `NavHost` should have enter/exit transitions:

```kotlin
NavHost(
    navController = navController,
    startDestination = startDestination,
    enterTransition = { slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)) },
    exitTransition = { slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(300)) },
    popEnterTransition = { slideInHorizontally(tween(300)) { -it } + fadeIn(tween(300)) },
    popExitTransition = { slideOutHorizontally(tween(300)) { it } + fadeOut(tween(300)) }
) { /* composable blocks */ }
```

---

## 7. Shimmer Loading States

When data is loading (e.g., first frame before Room emits), show shimmer placeholders instead of blank screens:

```kotlin
@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = modifier
            .background(Color.White.copy(alpha = alpha), RoundedCornerShape(12.dp))
    )
}

// Usage: ShimmerBox(Modifier.fillMaxWidth().height(80.dp))
```

---

## 8. FAB Animation

FABs should scale in on screen entry:

```kotlin
var fabVisible by remember { mutableStateOf(false) }
LaunchedEffect(Unit) { delay(400); fabVisible = true }

AnimatedVisibility(
    visible = fabVisible,
    enter = scaleIn(tween(300, easing = EaseOutBack)) + fadeIn(tween(300))
) {
    FloatingActionButton(
        onClick = { /* show bottom sheet */ },
        containerColor = PrimaryVibrant,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) { Icon(Icons.Default.Add, contentDescription = "Add") }
}
```

---

## 9. Empty State Illustrations

When a screen has no data yet, don't just show plain text. Use styled empty states:

```kotlin
@Composable
fun EmptyState(icon: ImageVector, title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon, contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = PrimaryVibrant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(8.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.4f), textAlign = TextAlign.Center)
    }
}
```

---

## 10. Gradient Backgrounds

Section headers and featured cards should use gradient backgrounds, not flat colors:

```kotlin
// Gradient card
Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    PrimaryVibrant.copy(alpha = 0.15f),
                    TertiaryVibrant.copy(alpha = 0.05f)
                )
            ),
            shape = RoundedCornerShape(20.dp)
        )
        .border(1.dp, PrimaryVibrant.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
) { /* content */ }
```

```kotlin
// Glowing border effect for premium cards
Box(
    modifier = Modifier
        .shadow(
            elevation = 0.dp,
            shape = RoundedCornerShape(20.dp),
            ambientColor = PrimaryVibrant.copy(alpha = 0.3f),
            spotColor = PrimaryVibrant.copy(alpha = 0.3f)
        )
)
```

---

## 11. Haptic Feedback

Add haptic feedback on important actions (saving data, resetting streak, etc.):

```kotlin
val haptic = LocalHapticFeedback.current
// On button click:
haptic.performHapticFeedback(HapticFeedbackType.LongPress)
```

---

## 12. Snackbar Confirmations

After saving data, show a styled snackbar instead of doing nothing:

```kotlin
val snackbarHostState = remember { SnackbarHostState() }

// In Scaffold:
Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { ... }

// After save:
scope.launch {
    snackbarHostState.showSnackbar(
        message = "Weight saved ✓",
        duration = SnackbarDuration.Short
    )
}
```

---

## Summary Checklist for Every New Screen

Before marking a screen as "done", verify ALL of these:

- [ ] Content fades/slides in on first display (staggered if multiple cards)
- [ ] Cards have press scale feedback if tappable
- [ ] Stats use animated counters (numbers count up from 0)
- [ ] Circular progress indicators animate from 0 to target
- [ ] Empty states use icon + title + subtitle (not just plain text)
- [ ] FAB scales in with delay after content loads
- [ ] "Add" dialogs use `ModalBottomSheet` with dark styling
- [ ] Data saves show snackbar confirmation
- [ ] Important actions trigger haptic feedback
- [ ] Loading state shows shimmer placeholders
- [ ] Background uses `MaterialTheme.colorScheme.background` (never white)
- [ ] All text colors use `Color.White` with appropriate alpha (never pure black text)
