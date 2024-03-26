package com.cs4520.assignment5

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
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

class MainActivity : ComponentActivity(){

    //TODO: product list fragment functionality
    //TODO: Workmanager
    //TODO: Readme
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        var password_text by remember{ mutableStateOf("")
        }
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
        Text("Product List (Fix Later)")
    }


}