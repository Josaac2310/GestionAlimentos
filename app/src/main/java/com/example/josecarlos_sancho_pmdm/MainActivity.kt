package com.example.josecarlos_sancho_pmdm



import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.josecarlos_sancho_pmdm.Componentes.DrawerContent
import com.example.josecarlos_sancho_pmdm.Componentes.NavigationBottom
import com.example.josecarlos_sancho_pmdm.Composable.screenFormulario
import com.example.josecarlos_sancho_pmdm.Composable.screenListado
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.AlimentosViewModel
import com.example.josecarlos_sancho_pmdm.Pantallas.Ruta
import com.example.josecarlos_sancho_pmdm.RoomDB.BDController
import com.example.josecarlos_sancho_pmdm.ViewModels.ViewModel_Formulario
import com.example.josecarlos_sancho_pmdm.ViewModels.ViewModel_Listado
import com.example.josecarlos_sancho_pmdm.ViewModels.stateListado
import com.example.josecarlos_sancho_pmdm.ViewModels.state_UIFormulario
import com.example.josecarlos_sancho_pmdm.ViewModels.state_newComponente
import com.example.josecarlos_sancho_pmdm.ViewModels.state_newIngrediente
import com.example.josecarlos_sancho_pmdm.ui.theme.JoseCarlos_Sancho_PMDMTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            BDController.init(applicationContext)
        }

        val viewModel = ViewModel_Formulario()
        val viewModelListado = ViewModel_Listado()
        enableEdgeToEdge()
        setContent {
            val estadoComponente by viewModel.stateNewComponente.collectAsState()
            val estadoIngrediente by viewModel.stateNewingrediente.collectAsState()
            val estadoUI by viewModel.stateUiformulario.collectAsState()
            val estadoListado by viewModelListado.stateListado.collectAsState()

            JoseCarlos_Sancho_PMDMTheme {
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .navigationBarsPadding()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "main"
                    ) {
                        composable("main") {
                            screenMain(
                                context = this@MainActivity,
                                viewModel = viewModel,
                                viewmodelListado = viewModelListado,
                                stateNewcomponente = estadoComponente,
                                stateNewingrediente = estadoIngrediente,
                                stateListado = estadoListado,
                                stateUiformulario = estadoUI,
                                navController = navController
                            )
                        }
                        composable("formulario") {
                            screenFormulario(
                                viewModel = viewModel,
                                stateComponente = estadoComponente,
                                stateIngrediente = estadoIngrediente,
                                stateUi = estadoUI
                            )
                        }
                        composable("listado") {
                            screenListado(
                                contexto = this@MainActivity,
                                viewmodelListado = viewModelListado
                            )
                        }
                    }
                }
            }
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
    var alimentos = remember { mutableStateListOf<ComponenteDieta>() }
    val navigationController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().background(Color.Magenta).padding(15.dp).systemBarsPadding().navigationBarsPadding()) {
        Row(Modifier.fillMaxSize().weight(0.5f).background(Color.Gray), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "GESTOR DE ALIMENTOS CON ROOM", fontSize = 30.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }
        Row(Modifier.fillMaxSize().weight(0.1f).background(Color.Red), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            ElevatedButton(onClick = { navController.navigate("listado") }) {
                Text(text = "Ver listado", fontSize = 20.sp)
            }
        }
        Row(Modifier.fillMaxSize().weight(0.1f).background(Color.Green), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            ElevatedButton(onClick = { navController.navigate("formulario") }) {
                Text(text = "Nuevo elemento", fontSize = 20.sp)
            }
        }
        Row(Modifier.fillMaxSize().weight(0.3f).background(Color.Blue), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
            Text(text = "Propiedad reservada Jose Carlos Sancho Acosta", fontSize = 15.sp)
        }
    }
    Column(modifier.padding(20.dp)) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent {
                    route -> scope.launch { drawerState.close() }
                    navigationController.navigate(route)
                }
            }
        ) {
            Scaffold(
                topBar = {
                    NavigationBottom(navigationController)
                }
            ) { paddingValues ->
                NavHost(
                    navController = navigationController,
                    startDestination = Ruta.Formulario.ruta,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(Ruta.Formulario.ruta) { screenFormulario(viewModel, stateNewcomponente, stateNewingrediente, stateUiformulario) }
                    composable(Ruta.ListadoDetalle.ruta) { screenListado(context, viewmodelListado) }
                }
            }
        }
    }
}

@Composable
fun Main(viewModel: ViewModel_Formulario, viewmodelListado: ViewModel_Listado,stateNewcomponente: state_newComponente, stateNewingrediente: state_newIngrediente, stateListado: stateListado, stateUiformulario: state_UIFormulario, context: Context, modifier: Modifier = Modifier, name: String){

    var alimentos = remember { mutableStateListOf<ComponenteDieta>() }
    val navigationController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


}
