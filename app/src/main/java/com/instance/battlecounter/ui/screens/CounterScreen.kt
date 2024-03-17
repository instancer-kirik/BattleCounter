package com.instance.battlecounter.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.instance.battlecounter.data.Counter
import com.instance.battlecounter.util.PrefUtil
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun CounterScreen() {
    val context = LocalContext.current
    var counters by remember { mutableStateOf(PrefUtil.getCounters(context)) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(0.dp)) {
            Button(onClick = {
                val newId = (counters.maxByOrNull { it.id.toInt() }?.id?.toInt() ?: 0) + 1
                val newCounter = Counter(
                    id = newId.toString(),
                    label = "Counter $newId",
                    value = 0,
                    incrementUp = 1,
                    incrementDown = 1
                )
                counters = counters + newCounter
                PrefUtil.saveCounter(context, newCounter)
            }) {
                Text("Add Counter")
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(0.dp)) {
                items(counters) { counter ->
                    CounterRow(counter = counter, onCounterChange = { updatedCounter ->
                        counters =
                            counters.map { if (it.id == updatedCounter.id) updatedCounter else it }
                        PrefUtil.saveCounter(context, updatedCounter)
                    }, onDelete = { id ->
                        counters = counters.filter { it.id != id }
                        PrefUtil.deleteCounter(context, id)
                    })
                    HorizontalDivider(modifier = Modifier.padding(0.dp))
                }
            }
        }
    }
}

@Composable
fun CounterRow(counter: Counter, onCounterChange: (Counter) -> Unit, onDelete: (String) -> Unit) {
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showValueInputDialog by remember { mutableStateOf(false) }

    val shapeSize =18.dp
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
//            .border(1.dp,Color.Red)
         ,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(0.dp)
            ) {
                Button(onClick = {
                onCounterChange(counter.copy(value = counter.value - counter.incrementDown)) },
                    modifier = Modifier.padding(0.dp).fillMaxHeight().weight(.5f),//border(1.dp,Color.Blue),
                    shape = LeftPointyButtonShape(cutCornerSize = shapeSize, flatWidth = shapeSize/3),
                 )
                {
                    Text("-")
                }
                Box(
                    modifier = Modifier
                        .weight(3f) // This allows the text and settings to share the available space
                        .padding(horizontal = 0.dp)//8
                        .fillMaxHeight()
                    ,
                    contentAlignment = Alignment.Center // Aligns content to the start of the box
                ) {
                    Text(counter.label + ": " + counter.value.toString(),
                        modifier = Modifier
                            .padding(horizontal = 0.dp)//8
                            .wrapContentHeight(Alignment.CenterVertically)
                            .clickable { showValueInputDialog = true }
                         )
                }
                Button(onClick = {
                onCounterChange(counter.copy(value = counter.value + counter.incrementUp)) },
                    shape = RightPointyButtonShape(cutCornerSize = shapeSize, flatWidth = shapeSize/3),
                    modifier = Modifier.padding(0.dp).fillMaxHeight().weight(.5f))//border(1.dp,Color.Blue))
                {
                    Text("+")
                }

                IconButton(onClick = { showSettingsDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurface)
                }

    }}


    // Value Input Dialog
    if (showValueInputDialog) {
        CounterValueInputDialog(initialValue = counter.value, onDismiss = { showValueInputDialog = false }) { newValue ->
            counter.value = newValue
            onCounterChange(counter)
        }
    }
    if (showSettingsDialog) {
        CounterSettingsDialog(counter = counter, onSave = { updatedCounter ->
            onCounterChange(updatedCounter)
        }, onDelete = {
            onDelete(it.id)
        }, onDismiss = { showSettingsDialog = false })
    }
}
@Composable
fun CounterSettingsDialog(
    counter: Counter,
    onSave: (Counter) -> Unit,
    onDelete: (Counter) -> Unit,
    onDismiss: () -> Unit
) {
    var label by remember { mutableStateOf(counter.label) }
    var incrementUp by remember { mutableStateOf(counter.incrementUp.toString()) }
    var incrementDown by remember { mutableStateOf(counter.incrementDown.toString()) }
    var labelError by remember { mutableStateOf(false) }
    var incrementUpError by remember { mutableStateOf(false) }
    var incrementDownError by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        labelError = label.isBlank()
        incrementUpError = incrementUp.toIntOrNull() == null
        incrementDownError = incrementDown.toIntOrNull() == null
        return !(labelError || incrementUpError || incrementDownError)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Counter Settings") },
        text = {
            Column {
                TextField(
                    value = label,
                    onValueChange = { label = it; labelError = false },
                    label = { Text("Label") },
                    isError = labelError,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                )
                if (labelError) Text("Label cannot be empty", color = MaterialTheme.colorScheme.error)

                TextField(
                    value = incrementUp,
                    onValueChange = { incrementUp = it; incrementUpError = false },
                    label = { Text("Increment Up") },
                    isError = incrementUpError,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (incrementUpError) Text("Invalid increment value", color = MaterialTheme.colorScheme.error)

                TextField(
                    value = incrementDown,
                    onValueChange = { incrementDown = it; incrementDownError = false },
                    label = { Text("Increment Down") },
                    isError = incrementDownError,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (incrementDownError) Text("Invalid decrement value", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validate()) {
                        val updatedCounter = counter.copy(label = label, incrementUp = incrementUp.toInt(), incrementDown = incrementDown.toInt())
                        onSave(updatedCounter)
                        onDismiss()
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onDelete(counter); onDismiss() }) {
                Text("Delete")
            }
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
@Composable
fun CounterValueInputDialog(
    initialValue: Int,
    onDismiss: () -> Unit,
    onSave: (newValue: Int) -> Unit
) {
    var text by remember { mutableStateOf(initialValue.toString()) }
    var inputError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Set Counter Value") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it; inputError = false },
                isError = inputError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (inputError) Text("Invalid number", color = MaterialTheme.colorScheme.error)
        },
        confirmButton = {
            Button(
                onClick = {
                    val newValue = text.toIntOrNull()
                    if (newValue != null) {
                        onSave(newValue)
                        onDismiss()
                    } else {
                        inputError = true
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


class LeftPointyButtonShape(private val cutCornerSize: Dp, private val flatWidth: Dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val cutSize = with(density) { cutCornerSize.toPx() }
            val flatWidthPx = with(density) { flatWidth.toPx() }
            moveTo(cutSize, 0f)
            lineTo(size.width - flatWidthPx / 2, 0f)
            lineTo(size.width - cutSize, size.height / 4)
            //this is the middle CLOCKWISE
            lineTo(size.width - cutSize, 3 * size.height / 4)
            lineTo(size.width - flatWidthPx / 2, size.height)
            lineTo(cutSize, size.height)
            lineTo(0f, size.height / 2)
            close()
        }
        return Outline.Generic(path)
    }
}


class RightPointyButtonShape(private val cutCornerSize: Dp,
     private val flatWidth: Dp
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val cutSize = with(density) { cutCornerSize.toPx() }
            val flatWidthPx = with(density) { flatWidth.toPx() }
            moveTo(size.width - cutSize, 0f)
            lineTo(flatWidthPx / 2, 0f)
            lineTo(cutSize, size.height / 4)
            // Mirror the shape for the right side, moving counter-clockwise this time
            lineTo(cutSize, 3 * size.height / 4)
            lineTo(flatWidthPx / 2, size.height)
            lineTo(size.width - cutSize, size.height)
            lineTo(size.width, size.height / 2)
            close()
        }
        return Outline.Generic(path)
    }
}