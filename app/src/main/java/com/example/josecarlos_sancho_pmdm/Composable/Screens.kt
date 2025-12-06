package com.example.josecarlos_sancho_pmdm.Composable

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDietaData
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente
import com.example.josecarlos_sancho_pmdm.Modelo.IngredienteData
import com.example.josecarlos_sancho_pmdm.RoomDB.BDController
import com.example.josecarlos_sancho_pmdm.ViewModels.ViewModel_Formulario
import com.example.josecarlos_sancho_pmdm.ViewModels.ViewModel_Listado
import com.example.josecarlos_sancho_pmdm.ViewModels.state_UIFormulario
import com.example.josecarlos_sancho_pmdm.ViewModels.state_newComponente
import com.example.josecarlos_sancho_pmdm.ViewModels.state_newIngrediente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.josecarlos_sancho_pmdm.ViewModels.stateListado

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun prev() {
    Scaffold(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .navigationBarsPadding()) {
        screenListado()
    }
}

@Composable
fun screenFormulario(
    viewModel: ViewModel_Formulario,
    stateComponente: state_newComponente,
    stateIngrediente: state_newIngrediente,
    stateUi: state_UIFormulario
) {
    var expandedMenu by remember { mutableStateOf(false) }
    val opciones = listOf("Menu", "Receta", "Dieta", "Ingrediente")
    var selectedOption by remember { mutableStateOf("Seleccionar tipo") }

    // Variables para los campos del ingrediente
    var proteinas by remember { mutableStateOf("0.0") }
    var lipidos by remember { mutableStateOf("0.0") }
    var hidratos by remember { mutableStateOf("0.0") }
    var cantidad by remember { mutableStateOf("1.0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = when(selectedOption) {
                "Ingrediente" -> "Nuevo Ingrediente"
                "Menu" -> "Nuevo Menú"
                "Receta" -> "Nueva Receta"
                "Dieta" -> "Nueva Dieta"
                else -> "Nuevo Componente"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Campo nombre
        OutlinedTextField(
            value = stateComponente.nombre,
            onValueChange = { viewModel.updateNombreComponente(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de tipo
        Box(Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expandedMenu = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = selectedOption)
                Icon(
                    if (expandedMenu) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            selectedOption = opcion
                            expandedMenu = false
                        }
                    )
                }
            }
        }

        // Campos condicionales basados en el tipo seleccionado
        if (selectedOption == "Ingrediente") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Valores Nutricionales",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = proteinas,
                        onValueChange = { proteinas = it },
                        label = { Text("Proteínas (g)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = lipidos,
                        onValueChange = { lipidos = it },
                        label = { Text("Lípidos (g)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = hidratos,
                        onValueChange = { hidratos = it },
                        label = { Text("Hidratos (g)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        label = { Text("Cantidad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Botón guardar
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    if (selectedOption == "Ingrediente") {
                        // Primero creamos el componente tipo Ingrediente
                        val caloriasBase = (proteinas.toDoubleOrNull() ?: 0.0) * 4 +
                                         (hidratos.toDoubleOrNull() ?: 0.0) * 4 +
                                         (lipidos.toDoubleOrNull() ?: 0.0) * 9
                        val cantidad = cantidad.toDoubleOrNull() ?: 100.0
                        val caloriasTotales = caloriasBase * (cantidad / 100.0)

                        val componente = ComponenteDieta(
                            nombre = stateComponente.nombre,
                            tipo = selectedOption,
                            grHC_ini = hidratos.toDoubleOrNull() ?: 0.0,
                            grLip_ini = lipidos.toDoubleOrNull() ?: 0.0,
                            grPro_ini = proteinas.toDoubleOrNull() ?: 0.0
                        )
                        val idComponente = BDController.insertComponente(componente)

                        // Crear el ingrediente asociado al componente
                        val ingrediente = Ingrediente(
                            nombre = stateComponente.nombre,
                            grPro_ini = proteinas.toDoubleOrNull() ?: 0.0,
                            grLip_ini = lipidos.toDoubleOrNull() ?: 0.0,
                            grHC_ini = hidratos.toDoubleOrNull() ?: 0.0,
                            cantidad = cantidad,
                            componenteId = idComponente.toInt()
                        )
                        BDController.insertIngrediente(ingrediente)
                    } else {
                        val componente = ComponenteDieta(
                            nombre = stateComponente.nombre,
                            tipo = selectedOption,
                            grHC_ini = 0.0,
                            grLip_ini = 0.0,
                            grPro_ini = 0.0
                        )
                        BDController.insertComponente(componente)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Guardar")
        }
    }
}

@Composable
fun screenMain(
    context: Context?,
    viewModel: ViewModel_Formulario,
    viewmodelListado: ViewModel_Listado,
    stateNewcomponente: state_newComponente,
    stateNewingrediente: state_newIngrediente,
    stateListado: stateListado,
    stateUiformulario: state_UIFormulario,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Gestión de Alimentos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        Button(
            onClick = { navController.navigate("listado") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)
        ) {
            Text("Ver Listado de Alimentos", fontSize = 16.sp)
        }

        Button(
            onClick = { navController.navigate("formulario") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp)

        ) {
            Text("Añadir Nuevo Alimento", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Desarrollado por Jose Carlos Sancho Acosta",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}


@Composable
fun screenListado(contexto: Context? = null, viewmodelListado: ViewModel_Listado? = null) {
    viewmodelListado?.let { safeViewModel ->
        val stateListado by safeViewModel.stateListado.collectAsState()
        LaunchedEffect(Unit) {
            // Primero limpiamos la lista actual
            safeViewModel.clearComponentes()

            // Luego cargamos los componentes de la base de datos
            val componentes = BDController.selectAllComponentes()

            componentes.forEach { componente ->
                val componenteConverted = ComponenteDietaData(
                    id = componente.id,
                    nombre = componente.nombre,
                    grLip_ini = componente.grLip_ini,
                    grHC_ini = componente.grHC_ini,
                    grPro_ini = componente.grPro_ini,
                    tipo = componente.tipo,
                    ingredientes = arrayListOf()
                )

                val lista = BDController.selectIngredientesByComponente(componenteConverted.id)

                lista.forEach { ing ->
                    val ingConverted = IngredienteData(
                        id = ing.id,
                        nombre = ing.nombre,
                        grPro_ini = ing.grPro_ini,
                        grHC_ini = ing.grHC_ini,
                        grLip_ini = ing.grLip_ini,
                        cantidad = ing.cantidad,
                        componenteId = componenteConverted.id
                    )
                    componenteConverted.ingredientes.add(ingConverted)
                }

                safeViewModel.addComponenteDieta(componenteConverted)
            }
            
            // Solo seleccionar un componente si la lista no está vacía
            if (safeViewModel.stateListado.value.listado.isNotEmpty()) {
                safeViewModel.setComponenteSeleccionado(safeViewModel.stateListado.value.listado[0])
            }
            safeViewModel.setReady(true)
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (stateListado.listReady) {
                if (stateListado.listado.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay componentes disponibles",
                            fontSize = 18.sp
                        )
                    }
                } else {
                    Text(
                        text = "Componentes Disponibles",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(stateListado.listado.size) { index ->
                            cardReceta(contexto, stateListado.listado[index], viewmodelListado)
                        }

                    }

                    if (stateListado.componenteSeleccionado != null) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Detalles del Componente",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Botón para borrar componente
                                    IconButton(
                                        onClick = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                safeViewModel.setReady(false)
                                                // Primero borramos todos los ingredientes

                                                BDController.deleteIngredientesByComponente(
                                                    stateListado.componenteSeleccionado!!.id
                                                )

                                                // Luego borramos el componente
                                                BDController.deleteComponente(stateListado.componenteSeleccionado!!.id)
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    Toast.makeText(
                                                        contexto,
                                                        "Componente eliminado",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                // Actualizamos la lista

                                                safeViewModel.removeComponenteDieta(stateListado.componenteSeleccionado!!)
                                                if (safeViewModel.stateListado.value.listado.isNotEmpty()) {

                                                    safeViewModel.setComponenteSeleccionado(
                                                        safeViewModel.stateListado.value.listado[0]
                                                    )
                                                } else {
                                                    safeViewModel.setComponenteSeleccionado(null)
                                                }
                                                safeViewModel.setReady(true)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Eliminar componente",
                                            tint = Color.Red
                                        )
                                    }
                                }

                                Text(
                                    text = stateListado.componenteSeleccionado!!.nombre,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                var ingredienteAEditar by remember { mutableStateOf<IngredienteData?>(null) }

                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(stateListado.componenteSeleccionado!!.ingredientes.size) { index ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    ingredienteAEditar = stateListado.componenteSeleccionado!!.ingredientes[index]
                                                },
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = stateListado.componenteSeleccionado!!.ingredientes[index].nombre,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = "HC: ${stateListado.componenteSeleccionado!!.ingredientes[index].grHC_ini}g " +
                                                                "Lip: ${stateListado.componenteSeleccionado!!.ingredientes[index].grLip_ini}g " +
                                                                "Pro: ${stateListado.componenteSeleccionado!!.ingredientes[index].grPro_ini}g",
                                                        fontSize = 12.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                                IconButton(

                                                    onClick = {
                                                        CoroutineScope(Dispatchers.IO).launch {
                                                            safeViewModel.setReady(false)

                                                            // Borrar el ingrediente de la base de datos

                                                            val deleted =
                                                                BDController.deleteIngredienteByComponente(

                                                                    stateListado.componenteSeleccionado!!.id,

                                                                    stateListado.componenteSeleccionado!!.ingredientes[index].id

                                                                )


                                                            // Mostrar mensaje de confirmación

                                                            CoroutineScope(Dispatchers.Main).launch {
                                                                Toast.makeText(
                                                                    contexto,
                                                                    "Ingrediente eliminado",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()

                                                            }


                                                            // Actualizar la lista de ingredientes

                                                            val ingredientesUpdated =
                                                                BDController.selectIngredientesByComponente(
                                                                    stateListado.componenteSeleccionado!!.id
                                                                )

                                                            val comp =
                                                                stateListado.componenteSeleccionado!!.copy()

                                                            comp.ingredientes.clear()


                                                            ingredientesUpdated.forEach { ing ->
                                                                val ingConverted = IngredienteData(

                                                                    id = ing.id,
                                                                    nombre = ing.nombre,
                                                                    grPro_ini = ing.grPro_ini,
                                                                    grHC_ini = ing.grHC_ini,
                                                                    grLip_ini = ing.grLip_ini,
                                                                    cantidad = ing.cantidad,
                                                                    componenteId = comp.id
                                                                )
                                                                comp.ingredientes.add(ingConverted)
                                                            }


                                                            safeViewModel.addComponenteDieta(comp)
                                                            safeViewModel.setComponenteSeleccionado(comp
                                                            )
                                                            safeViewModel.setReady(true)
                                                        }
                                                    }
                                                ) {
                                                    Icon(
                                                        Icons.Filled.Delete,
                                                        contentDescription = "Eliminar ingrediente",
                                                        tint = Color.Red
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }


                                var mostrarDialogo by remember { mutableStateOf(false) }
                                var mostrarDialogoExistente by remember { mutableStateOf(false) }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { mostrarDialogo = true },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Nuevo Ingrediente")
                                    }

                                    Button(
                                        onClick = { mostrarDialogoExistente = true },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Añadir Existente")
                                    }
                                }

                                if (mostrarDialogoExistente) {
                                    DialogoSeleccionarIngrediente(
                                        onDismiss = { mostrarDialogoExistente = false },
                                        onConfirm = { ingredienteSeleccionado ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                safeViewModel.setReady(false)
                                                
                                                Log.d("MenuDebug", "=== INICIO PROCESO DE AÑADIR INGREDIENTE ===")
                                                Log.d("MenuDebug", "Estado actual del menú: ${stateListado.componenteSeleccionado?.nombre}")
                                                Log.d("MenuDebug", "Número de ingredientes actual: ${stateListado.componenteSeleccionado?.ingredientes?.size}")
                                                
                                                // Crear una copia del ingrediente seleccionado manteniendo los valores nutricionales originales
                                                val nuevoIngrediente = Ingrediente(
                                                    id = 0,
                                                    nombre = ingredienteSeleccionado.nombre,
                                                    grPro_ini = ingredienteSeleccionado.grPro_ini,
                                                    grLip_ini = ingredienteSeleccionado.grLip_ini,
                                                    grHC_ini = ingredienteSeleccionado.grHC_ini,
                                                    cantidad = ingredienteSeleccionado.cantidad,
                                                    componenteId = stateListado.componenteSeleccionado!!.id
                                                )

                                                Log.d("MenuDebug", "Insertando nuevo ingrediente: ${nuevoIngrediente.nombre} con cantidad ${nuevoIngrediente.cantidad}")
                                                BDController.insertIngrediente(nuevoIngrediente)

                                                Log.d("MenuDebug", "Obteniendo lista actualizada de ingredientes")
                                                val ingredientesUpdated = BDController.selectIngredientesByComponente(
                                                    stateListado.componenteSeleccionado!!.id
                                                )
                                                Log.d("MenuDebug", "Número de ingredientes después de insertar: ${ingredientesUpdated.size}")

                                                Log.d("MenuDebug", "Eliminando componente actual de la lista")
                                                val componenteActualId = stateListado.componenteSeleccionado!!.id
                                                val componenteActualNombre = stateListado.componenteSeleccionado!!.nombre
                                                safeViewModel.removeComponenteDieta(stateListado.componenteSeleccionado!!)

                                                Log.d("MenuDebug", "Creando copia del componente")
                                                val comp = stateListado.componenteSeleccionado!!.copy()
                                                comp.ingredientes.clear()

                                                // Calcular las calorías totales del componente
                                                var totalKcal = 0.0
                                                Log.d("MenuDebug", "Procesando ingredientes actualizados:")
                                                ingredientesUpdated.forEach { ing ->
                                                    val caloriasBase = (ing.grPro_ini * 4) + 
                                                                      (ing.grHC_ini * 4) + 
                                                                      (ing.grLip_ini * 9)
                                                    val caloriasIngrediente = caloriasBase * ing.cantidad
                                                    totalKcal += caloriasIngrediente
                                                    
                                                    Log.d("MenuDebug", "- Ingrediente: ${ing.nombre}")
                                                    Log.d("MenuDebug", "  Cantidad: ${ing.cantidad}")
                                                    Log.d("MenuDebug", "  Calorías: $caloriasIngrediente")
                                                    
                                                    val ingConverted = IngredienteData(
                                                        id = ing.id,
                                                        nombre = ing.nombre,
                                                        grPro_ini = ing.grPro_ini,
                                                        grHC_ini = ing.grHC_ini,
                                                        grLip_ini = ing.grLip_ini,
                                                        cantidad = ing.cantidad,
                                                        componenteId = comp.id
                                                    )
                                                    comp.ingredientes.add(ingConverted)
                                                }

                                                Log.d("MenuDebug", "Total calorías calculadas: $totalKcal")

                                                // Actualizar el componente en la base de datos
                                                val componenteActualizado = ComponenteDieta(
                                                    id = comp.id,
                                                    nombre = comp.nombre,
                                                    tipo = comp.tipo,
                                                    grPro_ini = totalKcal / 4.0,
                                                    grHC_ini = totalKcal / 4.0,
                                                    grLip_ini = totalKcal / 9.0
                                                )
                                                
                                                Log.d("MenuDebug", "Actualizando componente en la base de datos")
                                                BDController.updateComponente(componenteActualizado, componenteActualizado)

                                                Log.d("MenuDebug", "Verificando estado de la lista antes de añadir")
                                                Log.d("MenuDebug", "Número de componentes en la lista: ${safeViewModel.stateListado.value.listado.size}")
                                                
                                                // Añadir el componente actualizado y seleccionarlo
                                                safeViewModel.addComponenteDieta(comp)
                                                safeViewModel.setComponenteSeleccionado(comp)
                                                
                                                Log.d("MenuDebug", "Estado final:")
                                                Log.d("MenuDebug", "Número de componentes en la lista: ${safeViewModel.stateListado.value.listado.size}")
                                                Log.d("MenuDebug", "=== FIN PROCESO DE AÑADIR INGREDIENTE ===")
                                                
                                                safeViewModel.setReady(true)

                                                // Mostrar mensaje de confirmación
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    Toast.makeText(
                                                        contexto,
                                                        "Ingrediente añadido",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    )
                                }

                                if (mostrarDialogo) {
                                    DialogoNuevoIngrediente(
                                        onDismiss = { mostrarDialogo = false },
                                        onConfirm = { nuevoIngrediente ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                safeViewModel.setReady(false)
                                                safeViewModel.removeComponenteDieta(stateListado.componenteSeleccionado!!)

                                                // Convertir IngredienteData a Ingrediente para la base de datos
                                                val ingredienteDB = Ingrediente(
                                                    id = 0, // La base de datos asignará el ID
                                                    nombre = nuevoIngrediente.nombre,
                                                    grPro_ini = nuevoIngrediente.grPro_ini,
                                                    grLip_ini = nuevoIngrediente.grLip_ini,
                                                    grHC_ini = nuevoIngrediente.grHC_ini,
                                                    cantidad = nuevoIngrediente.cantidad,
                                                    componenteId = stateListado.componenteSeleccionado!!.id
                                                )

                                                // Insertar el nuevo ingrediente
                                                BDController.insertIngrediente(ingredienteDB)

                                                // Actualizar la lista de ingredientes
                                                val ingredientesUpdated = BDController.selectIngredientesByComponente(
                                                    stateListado.componenteSeleccionado!!.id
                                                )

                                                val comp = stateListado.componenteSeleccionado!!.copy()
                                                comp.ingredientes.clear()

                                                // Calcular las calorías totales del componente
                                                var totalKcal = 0.0
                                                ingredientesUpdated.forEach { ing ->
                                                    val caloriasBase = (ing.grPro_ini * 4) + 
                                                                      (ing.grHC_ini * 4) + 
                                                                      (ing.grLip_ini * 9)
                                                    totalKcal += caloriasBase * ing.cantidad
                                                    
                                                    val ingConverted = IngredienteData(
                                                        id = ing.id,
                                                        nombre = ing.nombre,
                                                        grPro_ini = ing.grPro_ini,
                                                        grHC_ini = ing.grHC_ini,
                                                        grLip_ini = ing.grLip_ini,
                                                        cantidad = ing.cantidad,
                                                        componenteId = comp.id
                                                    )
                                                    comp.ingredientes.add(ingConverted)
                                                }

                                                // Actualizar las calorías del componente
                                                comp.grPro_ini = totalKcal / 4.0 // Ajustamos proporcionalmente los macronutrientes
                                                comp.grHC_ini = totalKcal / 4.0
                                                comp.grLip_ini = totalKcal / 9.0

                                                // Actualizar las calorías del componente en la base de datos
                                                val componenteActualizado = ComponenteDieta(
                                                    id = comp.id,
                                                    nombre = comp.nombre,
                                                    tipo = comp.tipo,
                                                    grPro_ini = totalKcal / 4.0,
                                                    grHC_ini = totalKcal / 4.0,
                                                    grLip_ini = totalKcal / 9.0
                                                )
                                                BDController.updateComponente(componenteActualizado, componenteActualizado)

                                                safeViewModel.addComponenteDieta(comp)
                                                safeViewModel.setComponenteSeleccionado(comp)
                                                safeViewModel.setReady(true)
                                            }
                                        }
                                    )
                                }
                                // Añadir el diálogo de edición aquí
                                ingredienteAEditar?.let { ingrediente ->
                                    DialogoEditarIngrediente(
                                        ingrediente = ingrediente,
                                        onDismiss = { ingredienteAEditar = null },
                                        onConfirm = { ingredienteActualizado ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                safeViewModel.setReady(false)
                                                
                                                // Convertir a Ingrediente para la base de datos
                                                val ingredienteDB = Ingrediente(
                                                    id = ingredienteActualizado.id,
                                                    nombre = ingredienteActualizado.nombre,
                                                    grPro_ini = ingredienteActualizado.grPro_ini,
                                                    grLip_ini = ingredienteActualizado.grLip_ini,
                                                    grHC_ini = ingredienteActualizado.grHC_ini,
                                                    cantidad = ingredienteActualizado.cantidad,
                                                    componenteId = ingredienteActualizado.componenteId
                                                )
                                                
                                                // Actualizar en la base de datos
                                                BDController.updateIngrediente(ingredienteDB)
                                                
                                                // Actualizar la UI
                                                val ingredientesUpdated = BDController.selectIngredientesByComponente(stateListado.componenteSeleccionado!!.id)
                                                val comp = stateListado.componenteSeleccionado!!.copy()
                                                
                                                // Eliminar el componente actual de la lista antes de añadir el actualizado
                                                safeViewModel.removeComponenteDieta(stateListado.componenteSeleccionado!!)
                                                
                                                comp.ingredientes.clear()
                                                ingredientesUpdated.forEach { ing ->
                                                    val ingConverted = IngredienteData(
                                                        id = ing.id,
                                                        nombre = ing.nombre,
                                                        grPro_ini = ing.grPro_ini,
                                                        grHC_ini = ing.grHC_ini,
                                                        grLip_ini = ing.grLip_ini,
                                                        cantidad = ing.cantidad,
                                                        componenteId = comp.id
                                                    )
                                                    comp.ingredientes.add(ingConverted)
                                                }
                                                
                                                // Calcular las calorías totales del componente
                                                var totalKcal = 0.0
                                                ingredientesUpdated.forEach { ing ->
                                                    val caloriasBase = (ing.grPro_ini * 4) + 
                                                                      (ing.grHC_ini * 4) + 
                                                                      (ing.grLip_ini * 9)
                                                    totalKcal += caloriasBase * ing.cantidad
                                                }

                                                // Actualizar las calorías del componente
                                                comp.grPro_ini = totalKcal / 4.0 // Ajustamos proporcionalmente los macronutrientes
                                                comp.grHC_ini = totalKcal / 4.0
                                                comp.grLip_ini = totalKcal / 9.0

                                                // Actualizar las calorías del componente en la base de datos
                                                val componenteActualizado = ComponenteDieta(
                                                    id = comp.id,
                                                    nombre = comp.nombre,
                                                    tipo = comp.tipo,
                                                    grPro_ini = totalKcal / 4.0,
                                                    grHC_ini = totalKcal / 4.0,
                                                    grLip_ini = totalKcal / 9.0
                                                )
                                                BDController.updateComponente(componenteActualizado, componenteActualizado)

                                                safeViewModel.addComponenteDieta(comp)
                                                safeViewModel.setComponenteSeleccionado(comp)
                                                safeViewModel.setReady(true)

                                                // Mostrar mensaje de confirmación
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    Toast.makeText(contexto, "Ingrediente actualizado", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            // Cerrar el diálogo
                                            ingredienteAEditar = null
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun cardReceta(
    context: Context?,
    componenteDieta: ComponenteDietaData,
    viewmodelListado: ViewModel_Listado?
) {
    // Calcular kilocalorías totales teniendo en cuenta la cantidad
    val totalKcal = if (componenteDieta.tipo == "Ingrediente") {
        // Si es un ingrediente, calculamos las calorías directamente de sus macronutrientes
        val caloriasBase = (componenteDieta.grPro_ini * 4) +
                (componenteDieta.grHC_ini * 4) +
                (componenteDieta.grLip_ini * 9)
        Log.d("CaloriasDebug", "Componente: ${componenteDieta.nombre}")
        Log.d("CaloriasDebug", "Proteínas: ${componenteDieta.grPro_ini}g, Hidratos: ${componenteDieta.grHC_ini}g, Lípidos: ${componenteDieta.grLip_ini}g")
        Log.d("CaloriasDebug", "Calorías base: $caloriasBase")
        caloriasBase
    } else {
        // Si es un menú o componente compuesto, sumamos las calorías de sus ingredientes
        var total = 0.0
        Log.d("CaloriasDebug", "Componente compuesto (${componenteDieta.nombre}):")
        
        if (componenteDieta.ingredientes.isEmpty()) {
            Log.d("CaloriasDebug", "No hay ingredientes en el menú")
        } else {
            componenteDieta.ingredientes.forEach { ingrediente ->
                // Calculamos las calorías base del ingrediente
                val caloriasBase = (ingrediente.grPro_ini * 4) +
                        (ingrediente.grHC_ini * 4) +
                        (ingrediente.grLip_ini * 9)
                Log.d("CaloriasDebug", "Ingrediente: ${ingrediente.nombre}")
                Log.d("CaloriasDebug", "Proteínas: ${ingrediente.grPro_ini}g, Hidratos: ${ingrediente.grHC_ini}g, Lípidos: ${ingrediente.grLip_ini}g")
                Log.d("CaloriasDebug", "Cantidad: ${ingrediente.cantidad} unidades")
                Log.d("CaloriasDebug", "Calorías base: $caloriasBase")

                // Multiplicamos las calorías base por la cantidad
                val caloriasIngrediente = caloriasBase * ingrediente.cantidad
                Log.d("CaloriasDebug", "Calorías totales por ingrediente: $caloriasIngrediente")

                total += caloriasIngrediente
            }
        }
        Log.d("CaloriasDebug", "Total de calorías del componente compuesto: $total")
        total
    }
    Card(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .clickable { viewmodelListado?.setComponenteSeleccionado(componenteDieta) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = componenteDieta.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = componenteDieta.tipo,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Divider()

            Text(
                text = "Kilocalorías totales: ${String.format("%.1f", totalKcal)} kcal",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
fun DialogoNuevoIngrediente(
    onDismiss: () -> Unit,
    onConfirm: (IngredienteData) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var proteinas by remember { mutableStateOf("0.0") }
    var lipidos by remember { mutableStateOf("0.0") }
    var hidratos by remember { mutableStateOf("0.0") }
    var cantidad by remember { mutableStateOf("0.0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Ingrediente") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = proteinas,
                    onValueChange = { proteinas = it },
                    label = { Text("Proteínas (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = lipidos,
                    onValueChange = { lipidos = it },
                    label = { Text("Lípidos (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hidratos,
                    onValueChange = { hidratos = it },
                    label = { Text("Hidratos (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

        },

        confirmButton = {
            Button(

                onClick = {
                    val ingrediente = IngredienteData(
                        id = 0, // La base de datos asignará el ID
                        nombre = nombre,
                        grPro_ini = proteinas.toDoubleOrNull() ?: 0.0,
                        grLip_ini = lipidos.toDoubleOrNull() ?: 0.0,
                        grHC_ini = hidratos.toDoubleOrNull() ?: 0.0,
                        cantidad = cantidad.toDoubleOrNull() ?: 0.0,
                        componenteId = 0 // Se asignará después
                    )
                    onConfirm(ingrediente)
                    onDismiss()
                }
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoEditarIngrediente(
    ingrediente: IngredienteData,
    onDismiss: () -> Unit,
    onConfirm: (IngredienteData) -> Unit
) {
    var nombre by remember { mutableStateOf(ingrediente.nombre) }
    var proteinas by remember { mutableStateOf(ingrediente.grPro_ini.toString()) }
    var lipidos by remember { mutableStateOf(ingrediente.grLip_ini.toString()) }
    var hidratos by remember { mutableStateOf(ingrediente.grHC_ini.toString()) }
    var cantidad by remember { mutableStateOf(ingrediente.cantidad.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Ingrediente") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = proteinas,
                    onValueChange = { proteinas = it },
                    label = { Text("Proteínas (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = lipidos,
                    onValueChange = { lipidos = it },
                    label = { Text("Lípidos (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hidratos,
                    onValueChange = { hidratos = it },
                    label = { Text("Hidratos (g)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ingredienteActualizado = ingrediente.copy(
                        nombre = nombre,
                        grPro_ini = proteinas.toDoubleOrNull() ?: ingrediente.grPro_ini,
                        grLip_ini = lipidos.toDoubleOrNull() ?: ingrediente.grLip_ini,
                        grHC_ini = hidratos.toDoubleOrNull() ?: ingrediente.grHC_ini,
                        cantidad = cantidad.toDoubleOrNull() ?: ingrediente.cantidad
                    )
                    onConfirm(ingredienteActualizado)
                    onDismiss()
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoSeleccionarIngrediente(
    onDismiss: () -> Unit,
    onConfirm: (Ingrediente) -> Unit
) {
    var ingredientesExistentes by remember { mutableStateOf<List<Ingrediente>>(emptyList()) }
    var selectedIngrediente by remember { mutableStateOf<Ingrediente?>(null) }
    var cantidad by remember { mutableStateOf("1.0") }

    // Cargar ingredientes existentes
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val componentes = BDController.selectAllComponentes()
                .filter { it.tipo == "Ingrediente" }
            
            val ingredientes = componentes.mapNotNull { componente ->
                // Obtener el ingrediente original con sus valores nutricionales
                val ingredienteOriginal = BDController.selectIngredientesByComponente(componente.id).firstOrNull()
                
                ingredienteOriginal?.let {
                    Log.d("CaloriasDebug", "Cargando ingrediente ${it.nombre}")
                    Log.d("CaloriasDebug", "Valores originales - Pro: ${it.grPro_ini}, HC: ${it.grHC_ini}, Lip: ${it.grLip_ini}")
                    
                    Ingrediente(
                        id = componente.id,
                        nombre = it.nombre,
                        grPro_ini = it.grPro_ini,
                        grHC_ini = it.grHC_ini,
                        grLip_ini = it.grLip_ini,
                        cantidad = 1.0,
                        componenteId = 0
                    )
                }
            }
            ingredientesExistentes = ingredientes
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Ingrediente") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(ingredientesExistentes.size) { index ->
                        val ingrediente = ingredientesExistentes[index]
                        val caloriasBase = (ingrediente.grPro_ini * 4) +
                                (ingrediente.grHC_ini * 4) +
                                (ingrediente.grLip_ini * 9)
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    selectedIngrediente = ingrediente
                                    Log.d("CaloriasDebug", "Ingrediente seleccionado: ${ingrediente.nombre}")
                                    Log.d("CaloriasDebug", "Valores - Pro: ${ingrediente.grPro_ini}, HC: ${ingrediente.grHC_ini}, Lip: ${ingrediente.grLip_ini}")
                                },
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (selectedIngrediente?.id == ingrediente.id) 4.dp else 1.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = ingrediente.nombre,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "HC: ${ingrediente.grHC_ini}g Lip: ${ingrediente.grLip_ini}g Pro: ${ingrediente.grPro_ini}g",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Calorías base: ${String.format("%.1f", caloriasBase)} kcal",
                                    fontSize = 12.sp,
                                    color = Color(0xFF2196F3)
                                )
                            }
                        }
                    }
                }

                if (selectedIngrediente != null) {
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        label = { Text("Cantidad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedIngrediente?.let { ingrediente ->
                        val cantidadNum = cantidad.toDoubleOrNull() ?: 1.0
                        Log.d("CaloriasDebug", "Añadiendo ingrediente: ${ingrediente.nombre}")
                        Log.d("CaloriasDebug", "Valores finales - Pro: ${ingrediente.grPro_ini}, HC: ${ingrediente.grHC_ini}, Lip: ${ingrediente.grLip_ini}")
                        Log.d("CaloriasDebug", "Cantidad: $cantidadNum")
                        
                        val ingredienteConCantidad = ingrediente.copy(
                            cantidad = cantidadNum
                        )
                        onConfirm(ingredienteConCantidad)
                        onDismiss()
                    }
                },
                enabled = selectedIngrediente != null
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}