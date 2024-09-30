package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.lab08.Room.Task
import com.example.lab08.Room.TaskDatabase
import kotlinx.coroutines.launch
import com.example.lab08.ui.theme.Lab08Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab08Theme {
                val db = Room.databaseBuilder(
                    applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                ).build()

                val taskDao = db.taskDao()
                val viewModel = TaskViewModel(taskDao)

                TaskScreen(viewModel)
            }
        }
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var newTaskDescription by remember { mutableStateOf("") }
    var editTaskDescription by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var editingTask: Task? by remember { mutableStateOf(null) }  // Para almacenar la tarea que se está editando.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de texto para buscar tareas
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar tareas") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { viewModel.searchTasks(searchQuery) }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para nueva tarea
        TextField(
            value = newTaskDescription,
            onValueChange = { newTaskDescription = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (newTaskDescription.isNotEmpty()) {
                    viewModel.addTask(newTaskDescription)
                    newTaskDescription = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Agregar tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros y ordenamientos
        Row {
            Button(onClick = { viewModel.getCompletedTasks() }) {
                Text("Ver completadas")
            }
            Button(onClick = { viewModel.getPendingTasks() }) {
                Text("Ver pendientes")
            }
        }

        Row {
            Button(onClick = { viewModel.sortTasksByName() }) {
                Text("Ordenar por nombre")
            }
            Button(onClick = { viewModel.sortTasksByDate() }) {
                Text("Ordenar por fecha")
            }
            Button(onClick = { viewModel.sortTasksByStatus() }) {
                Text("Ordenar por estado")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar y editar tareas
        tasks.forEach { task ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (editingTask == task) {
                    TextField(
                        value = editTaskDescription,
                        onValueChange = { editTaskDescription = it },
                        label = { Text("Editar tarea") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (editTaskDescription.isNotEmpty()) {
                                viewModel.editTask(task, editTaskDescription)
                                editingTask = null
                                editTaskDescription = ""
                            }
                        }
                    ) {
                        Text("Guardar")
                    }
                } else {
                    Text(text = task.description)
                    Button(onClick = { viewModel.toggleTaskCompletion(task) }) {
                        Text(if (task.isCompleted) "Completada" else "Pendiente")
                    }
                    Button(onClick = { editingTask = task; editTaskDescription = task.description }) {
                        Text("Editar")
                    }
                    Button(onClick = { viewModel.deleteTask(task) }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
