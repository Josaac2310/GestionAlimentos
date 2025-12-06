package com.example.josecarlos_sancho_pmdm.Pantallas


/*
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNodeLifecycleCallback
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.josecarlos_sancho_pmdm.BD_Fichero
import com.example.josecarlos_sancho_pmdm.DAO.ComponenteDAO
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.AlimentosViewModel
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente
import com.example.josecarlos_sancho_pmdm.Modelo.TipoComponente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.text.toDoubleOrNull


@Composable
fun Formulario(viewModel: AlimentosViewModel, context: Context, lista: SnapshotStateList<ComponenteDieta>) {

    var alimento by remember { mutableStateOf(ComponenteDieta()) }
    var nombre = remember { mutableStateOf("")}
    var proteinas = remember { mutableStateOf("")}
    var carbohidratos = remember { mutableStateOf("")}
    var lipidos = remember { mutableStateOf("")}
    var selectedTipo by remember { mutableStateOf(TipoComponente.SIMPLE) }

    var ingredientesList by remember { mutableStateOf(ArrayList<Ingrediente>()) }
    var rechargeUi by remember { mutableIntStateOf(0) }


    var menuNombre by remember { mutableStateOf("")}
    var menuDuracion by remember { mutableStateOf("")}


    val context = LocalContext.current

    if (rechargeUi != null){

        Row(modifier = Modifier.fillMaxSize().padding(25.dp, 15.dp)) {
            Column(Modifier.fillMaxSize()) {

                Row(Modifier.fillMaxWidth().weight(0.7f)) {
                    Column {
                        if (selectedTipo == TipoComponente.SIMPLE || selectedTipo == TipoComponente.PROCESADO) {
                            Text(text = "" + alimento.Kcal)
                            txtField(nombre.value, { valor ->
                                alimento.nombre = valor
                                nombre.value = valor
                            }, "Escribe nombre", "Introduce nombre aqui")
                            txtField(proteinas.value, { valor ->
                                alimento.grPro_ini = valor.toDoubleOrNull() ?: 0.0
                                proteinas.value = valor
                            }, "Cantidad de proteínas por 100 grs", "Introduce cantidad aquí")
                            txtField(carbohidratos.value, { valor ->
                                alimento.grHC_ini = valor.toDoubleOrNull() ?: 0.0
                                carbohidratos.value = valor
                            }, "Cantidad de Hidratos de carbono por 100 grs", "Introduce cantidad aquí")
                            txtField(lipidos.value, { valor ->
                                alimento.grLip_ini = valor.toDoubleOrNull() ?: 0.0
                                lipidos.value = valor
                            }, "Cantidad de Lípidos por 100 grs", "Introduce cantidad aquí")

                        } else {
                            Text(text = "" + alimento.Kcal)
                            txtField(nombre.value, { valor ->
                                alimento.nombre = valor
                                nombre.value = valor
                            }, "Escribe nombre", "Introduce nombre aqui")
                            txtField(proteinas.value, { valor ->
                                alimento.grPro_ini = valor.toDoubleOrNull() ?: 0.0
                                proteinas.value = valor
                            }, "Cantidad de proteínas por 100 grs", "Introduce cantidad aquí")
                            //

                            Column(Modifier.fillMaxWidth()) {
                                var showIngredienteFormulario by remember { mutableStateOf(false) }
                                var cantidadIng = remember { mutableStateOf("") }
                                var nombreIng = remember { mutableStateOf("") }
                                var proteinasIng = remember { mutableStateOf("") }
                                var carbohidratosIng = remember { mutableStateOf("") }
                                var lipidosIng = remember { mutableStateOf("") }
                                var alimentoIng by remember { mutableStateOf(Ingrediente(cd=ComponenteDieta())) }


                                Row(
                                    Modifier.fillMaxWidth().border(1.dp, Color.Black.copy(alpha = 0.5f))
                                ) {
                                    LazyColumn(Modifier.fillMaxHeight(0.4f).fillMaxWidth()) {
                                        items(ingredientesList.size) { index ->
                                            cardIngrediente(context, ingredientesList, index,
                                                { aux -> ingredientesList = aux }, { rechargeUi += 1 })
                                        }
                                    }
                                }
                                Row(Modifier.fillMaxWidth()) {
                                    ElevatedButton(
                                        onClick = {
                                            showIngredienteFormulario = !showIngredienteFormulario
                                        }
                                    ) {
                                        Text("Añadir Ingrediente")
                                    }
                                }
                                if (showIngredienteFormulario) {
                                    Dialog({ showIngredienteFormulario = !showIngredienteFormulario }) {
                                        Column {

                                            txtField(nombreIng.value, { valor ->
                                                alimento.nombre = valor
                                                nombreIng.value = valor
                                            }, "Escribe nombre", "Introduce nombre aqui")
                                            txtField(
                                                proteinasIng.value,
                                                { valor ->
                                                    alimento.grPro_ini = valor.toDoubleOrNull() ?: 0.0
                                                    proteinasIng.value = valor
                                                },
                                                "Cantidad de proteínas por 100 grs",
                                                "Introduce cantidad aquí"
                                            )
                                            txtField(
                                                carbohidratosIng.value,
                                                { valor ->
                                                    alimento.grHC_ini = valor.toDoubleOrNull() ?: 0.0
                                                    carbohidratosIng.value = valor
                                                },
                                                "Cantidad de Hidratos de carbono por 100 grs",
                                                "Introduce cantidad aquí"
                                            )
                                            txtField(
                                                lipidosIng.value,
                                                { valor ->
                                                    alimento.grLip_ini = valor.toDoubleOrNull() ?: 0.0
                                                    lipidosIng.value = valor
                                                },
                                                "Cantidad de Lípidos por 100 grs",
                                                "Introduce cantidad aquí"
                                            )
                                            txtField(
                                                cantidadIng.value.toString(),
                                                { valor ->
                                                    alimento.cantidadTotal =
                                                        valor.toDoubleOrNull() ?: 0.0
                                                    cantidadIng.value = valor
                                                },
                                                "Cantidad de Lípidos por 100 grs",
                                                "Introduce cantidad aquí"
                                            )

                                            ElevatedButton(
                                                onClick = {

                                                    val i = Ingrediente(
                                                        cd=ComponenteDieta(
                                                            nombre=nombreIng.value,
                                                            tipo = TipoComponente.SIMPLE,
                                                            grPro_ini = proteinasIng.value.toDoubleOrNull()!!,
                                                            grHC_ini =  carbohidratosIng.value.toDoubleOrNull()!!,
                                                             grLip_ini = lipidosIng.value.toDoubleOrNull()!!
                                                        ),
                                                        cantidad = cantidadIng.value.toDoubleOrNull()!!
                                                    )
                                                    ingredientesList.add(i)



                                                    showIngredienteFormulario =
                                                        !showIngredienteFormulario
                                                    limpiarCampos(
                                                        nombreIng,
                                                        proteinasIng,
                                                        carbohidratosIng,
                                                        lipidosIng,
                                                        cantidadIng
                                                    )
                                                }
                                            ) {
                                                Text("Crear Ingrediente")
                                            }

                                            ElevatedButton(
                                                onClick = {
                                                    showIngredienteFormulario =
                                                        !showIngredienteFormulario
                                                    limpiarCampos(
                                                        nombreIng,
                                                        proteinasIng,
                                                        carbohidratosIng,
                                                        lipidosIng,
                                                        cantidadIng
                                                    )
                                                }
                                            ) {
                                                Text("Cancelar Ingrediente")
                                            }
                                        }
                                    }
                                }
                            }
                    }


                    }
                }

                Row(Modifier.fillMaxWidth().weight(0.1f)) {
                    var expanded by remember { mutableStateOf(false) }
                    //comtentarios
                    Column {
                        Button(
                            onClick = {
                                expanded=true
                            }, Modifier.background(Color.Transparent).border(1.dp, Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp)),
                            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Transparent)
                        ) {
                            Text(selectedTipo.toString(), color = Color.Black)
                            if(expanded) Icon(Icons.Filled.KeyboardArrowDown, "", tint = Color.Black)
                            else Icon(Icons.Filled.KeyboardArrowUp, "", tint = Color.Black)

                        }
                        DropdownMenu(expanded, {  expanded= !expanded }) {
                            DropdownMenuItem( {
                                Text("Simple")
                            },
                            {
                                selectedTipo=TipoComponente.SIMPLE
                                expanded=false
                            })
                            DropdownMenuItem( {

                                Text("Procesado")
                            },
                                {
                                    selectedTipo=TipoComponente.PROCESADO
                                    expanded=false

                                })
                            DropdownMenuItem( {
                                Text("Menu")

                            },
                                {
                                    selectedTipo=TipoComponente.MENU
                                    expanded=false

                                })
                            DropdownMenuItem( {
                                Text("Receta")

                            },
                                {
                                    selectedTipo=TipoComponente.RECETA
                                    expanded=false

                                })
                            DropdownMenuItem( {
                                Text("Dieta")

                            },
                                {
                                    selectedTipo=TipoComponente.DIETA
                                    expanded=false

                                })

                        }
                    }
                }

                Row(Modifier.fillMaxWidth().weight(0.2f), horizontalArrangement = Arrangement.SpaceAround) {

                        Button(
                            onClick = {
                                lista.add(alimento) // Agregar a la lista
                                ComponenteDAO.addListaCD(context, lista) // Guardar en BD

                                alimento= ComponenteDieta()
                                Toast.makeText(context, "Alimento Guardado", Toast.LENGTH_SHORT).show()
                                limpiarCampos(nombre, proteinas, carbohidratos, lipidos)
                            }

                        ) { Text(text = "Guardar") }

                        Button(
                            onClick = {
                                alimento= ComponenteDieta()
                                limpiarCampos(nombre, proteinas, carbohidratos, lipidos)
                            }
                        ) { Text(text = "Cancelar") }

                    }
            }
        }
    }


}




@Composable
fun txtField(nombre: String, func: (String) -> Unit, label: String, placeHolder: String){
    TextField(
        value = nombre,
        //onValueChange = { newText -> viewModel.updateNombre(newText) },
        onValueChange = {newText ->
           func(newText)
        },
        label = { Text(label) },
        placeholder = { Text(placeHolder) },
        modifier = Modifier.fillMaxWidth()
    )
}


fun limpiarCampos(vararg campos: MutableState<String>){
    for(i in campos){
        i.value=""
    }
}

@Composable
fun cardIngrediente(context: Context ,ingredientesList:ArrayList<Ingrediente>, index: Int,
                    callback: (ArrayList<Ingrediente>) -> Unit,
                    callbackRechargeUi: () -> Unit
                    ){
    Card(Modifier.fillMaxWidth()) {
        Text(ingredientesList.get(index).cd.nombre)
        ElevatedButton(
            onClick = {
                val listaAux=ingredientesList
                listaAux.removeAt(index)

                callback(listaAux)

                callbackRechargeUi()

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Ingrediente Borrado", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Borrar")
        }
    }
}
*/