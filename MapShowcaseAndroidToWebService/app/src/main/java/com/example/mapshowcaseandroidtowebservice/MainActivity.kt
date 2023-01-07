package com.example.mapshowcaseandroidtowebservice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mapshowcaseandroidtowebservice.ui.theme.MapShowcaseAndroidToWebServiceTheme
import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MapShowcaseAndroidToWebServiceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MapDisplay()
                }
            }
        }
    }
}

const val minLat = 54.04270752753084
const val maxLat = 54.11773308594
const val minLong = 21.314587417775144
const val maxLong = 21.442646800979137
var currentMinLat = minLat
var currentMinLong = minLong
var currentMaxLat = maxLat
var currentMaxLong = maxLong

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MapShowcaseAndroidToWebServiceTheme {
        MapDisplay()
    }
}
@Composable
fun MapDisplay()
{
    var inputImageBitMap by remember { mutableStateOf("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Column{
            FetchedImage(inputImageBitMap)

            Column(modifier = Modifier.padding(10.dp)){
                CoordinatesInput("Minimum", 0)
                Spacer(modifier = Modifier.height(10.dp))
                CoordinatesInput("Maximum", 1)
            }

            Button(onClick = {
                println(currentMinLat);
                println(currentMinLong);
                println(currentMaxLat);
                println(currentMaxLong);
                var testRequest = TestRequest()
                var listener = IMapProviderListener { inputImageBitMap = it };

                if(abs(currentMinLat) <= 0.1) currentMinLong = minLat;
                if(abs(currentMaxLat) <= 0.1) currentMaxLat = maxLat;
                if(abs(currentMinLong) <= 0.1) currentMinLong = minLong;
                if(abs(currentMaxLong) <= 0.1) currentMaxLong = maxLong;

                if(currentMinLong >= currentMaxLong) currentMinLong = currentMaxLong - 0.01;
                if(currentMinLat >= currentMaxLat) currentMinLat = currentMaxLat - 0.01;

                currentMinLat = Math.min(currentMinLat, maxLat);
                currentMinLat = Math.max(currentMinLat, minLat);

                currentMaxLat = Math.min(currentMaxLat, maxLat);
                currentMaxLat = Math.max(currentMaxLat, minLat);

                currentMinLong = Math.min(currentMinLong, maxLong);
                currentMinLong = Math.max(currentMinLong, minLong);

                currentMaxLong = Math.min(currentMaxLong, maxLong);
                currentMaxLong = Math.max(currentMaxLong, minLong);

                testRequest.startWebAccess(listener, currentMinLong, currentMinLat, currentMaxLong, currentMaxLat)

            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)){
                Text("Run")
            }
        }
    }
}

@Composable
fun FetchedImage(input:String){

    val decodedString = Base64.decode(input, Base64.DEFAULT)
    val inputStream: InputStream = ByteArrayInputStream(decodedString)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .width(200.dp)
            .height(200.dp),
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "contentDescription",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

    }
}

@Composable
fun CoordinatesInput(title: String, id:Int) {
    var longText by remember { mutableStateOf( "") }
    var latText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally))
    {
        Row()
        {
            Column()
            {
                Text("$title long", Modifier.wrapContentHeight(Alignment.CenterVertically))
                if(id == 0){
                    Text("min: ${minLong.toString().subSequence(0, 12)}")
                } else {
                    Text("max: ${maxLong.toString().subSequence(0, 12)}")
                }
            }
            TextField(
                value = longText,
                onValueChange = {
                    longText = it
                    if(it != null && it != ""){
                        if(id==0){ currentMinLong = longText.toDouble(); }
                        else{ currentMaxLong = longText.toDouble(); }
                    }
                },
                modifier = Modifier.width(100.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Row()
        {
            Column()
            {
                Text("$title lat", Modifier.wrapContentHeight(Alignment.CenterVertically))
                if(id == 0){
                    Text("min: ${minLat.toString().subSequence(0, 12)}")
                } else {
                    Text("max: ${maxLat.toString().subSequence(0, 12)}")
                }
            }
            TextField(
                value = latText,
                onValueChange = { latText = it
                    if(it != null && it != "") {
                        if (id == 0) { currentMinLat = latText.toDouble() }
                        else { currentMaxLat = latText.toDouble() }
                    }
                },
                modifier = Modifier.width(100.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}