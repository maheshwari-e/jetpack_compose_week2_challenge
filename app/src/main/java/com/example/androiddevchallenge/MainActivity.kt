/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme


class MainActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val density  = resources.displayMetrics.density
        val width = resources.displayMetrics.widthPixels / density
        setContent {
            MyTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ToolBar(width)
                }
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp(width: Float,countDownViewModel: CountDownTimerViewModel = CountDownTimerViewModel()) {
    Column(modifier = Modifier.padding(16.dp) ) {
        val isTimerRunning by countDownViewModel.isTimerRunning.observeAsState()
        AnimatedVisibility(visible = isTimerRunning == false) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val hour: String by countDownViewModel.hours.observeAsState("00")
                InputTimer(
                    hour,
                    "HH",
                    ":",
                    onTextChange = countDownViewModel::onTimeHoursChanged
                )
                val minutes: String by countDownViewModel.minutes.observeAsState("00")
                InputTimer(
                    minutes,
                    "MM",
                    ":",
                    onTextChange = countDownViewModel::onTimeMintesChanged
                )
                val second: String by countDownViewModel.seconds.observeAsState("00")
                InputTimer(
                    second,
                    "SS",
                    "",
                    onTextChange = countDownViewModel::onTimeSecondsChanged
                )
            }
        }
        AnimatedVisibility(visible = isTimerRunning == true) {
            val time:String by countDownViewModel.tickTime.observeAsState("00:00:00")
            val second:Long by countDownViewModel.second.observeAsState(0)
                Box(modifier = Modifier.size(width.dp),
                    contentAlignment = Alignment.Center){
                    CircularProgress(countDownViewModel.maxSecond, second)
                    Text(
                        time, fontWeight = FontWeight.ExtraBold, fontSize = 48.sp,
                        textAlign = TextAlign.Center
                    )
            }
        }
        Row(modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { if(isTimerRunning == true) countDownViewModel.onCancelClick() },
                shape =  CircleShape,
                colors = if(isTimerRunning == false) ButtonDefaults.buttonColors(Color.LightGray)
                else ButtonDefaults.buttonColors(),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 32.dp)) {
                Text("Cancel", modifier = Modifier.padding(8.dp))
            }

            Button(onClick = { if(isTimerRunning == false) countDownViewModel.onStartClick() },
                shape =  CircleShape,
                colors = if(isTimerRunning == false) ButtonDefaults.buttonColors()
                else ButtonDefaults.buttonColors(Color.LightGray),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 32.dp)) {
                Text("Start", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun InputTimer(time: String, name: String, colon: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = time,
        onValueChange = { onTextChange(it)},
        label = { Text(text = name, textAlign = TextAlign.Center) },
        modifier = Modifier.width(60.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    if(colon.isNotEmpty()) Text(text = colon)
}

@ExperimentalAnimationApi
@Composable
fun ToolBar(width: Float) {
    Column {
        TopAppBar(title = { Text(text = "CountDown Timer") }, navigationIcon = null)
        MyApp(width)
    }
}

@Composable
fun CircularProgress(maxSecond: Long, second: Long) {
    val color =  MaterialTheme.colors
    Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
        drawArc(
            color.primary,
            270f,
            (second.toFloat() / maxSecond.toFloat())* 360f,
            useCenter = true,
            size = Size(size.width, size.width),
        )
        drawArc(
            color = color.background,
            270f,
            360f,
            topLeft = Offset(25f, 25f),
            useCenter = true,
            size = Size(size.width - 50f, size.width - 50f),
        )
    })
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(360f)
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(360f)
    }
}
