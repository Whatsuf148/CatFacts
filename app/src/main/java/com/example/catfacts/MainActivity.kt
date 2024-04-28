package com.example.catfacts

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catfacts.model.CatFacts
import com.example.catfacts.ui.theme.CatFactsTheme
import com.example.catfacts.utils.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val fact = mutableStateOf(CatFacts())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatFactsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            sendRequest()
                        },
                    color = MaterialTheme.colorScheme.background
                ) {
                    sendRequest()


                    MyUi(fact = fact)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getRandomFacts()
            }
            catch (e:HttpException){
                Toast.makeText(applicationContext, "http request error {${e.message}}", Toast.LENGTH_SHORT).show()
                return@launch
            }
            catch (e:IOException){
                Toast.makeText(applicationContext, "IO exception {${e.message}} ", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if(response.isSuccessful && response.body()!=null){
                withContext(Dispatchers.Main){
                    fact.value = response.body()!!
                }
            }
        }
    }
}

@Composable
fun MyUi(fact: MutableState<CatFacts>, modifier: Modifier = Modifier) {
    Column(modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Cat Fact:", modifier.padding(bottom = 25.dp), fontSize = 26.sp)
        Text(
            text = fact.value.fact, fontSize = 26.sp,
            lineHeight = 40.sp,
            textAlign = TextAlign.Center
        )
    }
}

