package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.add_edit_todo.AddEditTodoScreen
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.ui.todo_list.TodoListScreen
import com.example.todolist.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.TODO_LIST
                ){
                    composable(
                        route = Routes.TODO_LIST
                    ){
                        TodoListScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            }
                        )
                    }

                    composable(
                        route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                        arguments = listOf(
                            navArgument(name = "todoId"){
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ){
                        AddEditTodoScreen(
                            onPopBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
//                }
//                var name by remember {
//                    mutableStateOf("")
//                }
//                var names by remember {
//                    mutableStateOf(listOf<String>())
//                }
//                Column (
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(10.dp),
//                    //verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Row(
//
//                    ){
//                        OutlinedTextField(
//                            value = name,
//                            onValueChange = {
//                                input -> name = input
//                            },
//                            modifier = Modifier.weight(1f)
//                        )
//                        Spacer(modifier = Modifier.width(16.dp))
//                        Button(
//                            //modifier = Modifier.width(100.dp),
//                            onClick = {
//                                if(name.isNotBlank()){
//                                    names = names + name
//                                    name = ""
//                                }
//                                val intent = Intent(this@MainActivity, TestActivity::class.java)
//                                startActivity(intent)
//                        }) {
//                            Text(
//                                text = "Add&Navigate"
//                            )
//                        }
//                    }
//
//                    LazyColumn{
//                        items(names){
//                                currentName -> Text(
//                                                text = currentName,
//                                                modifier = Modifier
//                                                    .fillMaxWidth()
//                                                    .padding(6.dp))
//                                                Divider()
//                        }
//                    }
//                }
            }
        }
    }

//    fun magicClick(view: View) {
//        Toast.makeText(this, "Kliknuto na tlacitko",
//            Toast.LENGTH_LONG).show()
//        Log.i("info", "Uzivatel kliknul na tlacitko")
//    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Column (
//        modifier = Modifier
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ){
//        for(i in 1..3){
//            Icon(imageVector = Icons.Rounded.ArrowForward,
//                contentDescription = "",
//                modifier = Modifier
//                    .height(50.dp)
//                    .width(50.dp)
//                    .background(Color.Magenta)
//                    .border(border = BorderStroke(2.dp, Color.Red)))
//        }
//        Text(
//            text = "Hello $name!",
//            color = Color.Cyan,
//            fontSize = 20.sp,
//            modifier = Modifier
//                .background(Color(100, 100, 10))
//                .padding(20.dp)
//        )
//        Text(
//            text = "Ok I pull up!",
//            color = Color.Cyan,
//            fontSize = 20.sp,
//            modifier = Modifier
//                //.background(Color(100, 100, 10))
//                .padding(20.dp)
//        )
//        Row (
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.Bottom
//        ){
//            Text(
//                text = "Hello $name! In a row",
//                color = Color.Cyan,
//                fontSize = 20.sp,
//                modifier = Modifier
//                    .background(Color(100, 100, 10))
//                    .padding(20.dp)
//            )
//            Text(
//                text = "Ok I pull up in a row!",
//                color = Color.Cyan,
//                fontSize = 20.sp,
//                modifier = Modifier
//                    //.background(Color(100, 100, 10))
//                    .padding(20.dp)
//            )
//        }
//        Box(
//            modifier = Modifier
//                .height(300.dp)
//                .width(300.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "Hello $name! In a box",
//                color = Color.Cyan,
//                fontSize = 20.sp,
//                modifier = Modifier
//                    .background(Color(100, 100, 10))
//                    .padding(20.dp)
//                    .align(Alignment.BottomEnd)
//            )
//            Text(
//                text = "Ok I pull up in a box!",
//                color = Color.Cyan,
//                fontSize = 20.sp,
//                modifier = Modifier
//                    //.background(Color(100, 100, 10))
//                    .align(Alignment.TopStart)
//                    .padding(20.dp)
//            )
//        }
//    }
//}
//
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoListTheme {
        //Greeting("Android")
    }
}
