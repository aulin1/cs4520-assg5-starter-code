package com.cs4520.assignment5

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity(){
    //TODO: Workmanager
    //TODO: Readme

    private lateinit var viewModel: ProductViewModel
    private lateinit var viewModelFactory: ProductViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Testing", "start")

        ProductDatabase.setContext(this)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyNavHost()
                }
            }
        }
    }

    @Composable
    fun MyNavHost() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                Login(onNavigateToProductsList = { navController.navigate("productList") })
            }
            composable("productList") {
                ProductList()
            }
        }
    }

    @Composable
    fun Login(onNavigateToProductsList: () -> Unit){
        var username_text by remember{mutableStateOf("")}
        var password_text by remember{ mutableStateOf("") }
        Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            val context = LocalContext.current
            TextField(
                value = username_text,
                onValueChange = { username_text = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Username") }
            )
            TextField(
                value = password_text,
                onValueChange = { password_text = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(onClick = {
                if(username_text == "admin" && password_text == "admin"){
                    onNavigateToProductsList()
                } else {
                    Toast.makeText(context, "Username or password is incorrect.", Toast.LENGTH_SHORT).show()
                }}) {
                Text("LOGIN")
            }
        }
    }
    @Composable
    fun ProductList(){
        Log.d("Testing", "Product List")
        viewModelFactory = ProductViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]

        val productList by viewModel.ResponseData.observeAsState(listOf())
        val progress by viewModel.progress.observeAsState()

        if(progress == 0){
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
            }
        } else if (progress == 1) {
            if(productList.isEmpty()){
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No products available")
                }
            } else {
                LazyColumn {
                    items(items = productList) { product ->
                        ProductView(product)
                    }
                }
            }
        } else { //error message
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("An error occurred")
            }
        }
    }

    @Composable
    fun ProductView(p : ProductData){
        if(p.type == "Food"){
            Row(Modifier.background(Color(0xFFFFD965)).fillMaxWidth()){
                Image(
                    painter = painterResource(id = R.drawable.foodphoto),
                    null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp).padding(10.dp))
                Column(modifier = Modifier.height(150.dp), verticalArrangement = Arrangement.Center){
                    Text(p.name)
                    p.expiryDate?.let { Text(it) }
                    Text("$" + p.price)
                }
            }
        } else {
            Row(Modifier.background(Color(0xFFE06666)).fillMaxWidth()) {
                Image(painter = painterResource(id = R.drawable.equipmentphoto),
                    null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp).padding(10.dp))
                Column(modifier = Modifier.height(150.dp), verticalArrangement = Arrangement.Center){
                    Text(p.name)
                    Text("$" + p.price)
                }
            }
        }
    }
}