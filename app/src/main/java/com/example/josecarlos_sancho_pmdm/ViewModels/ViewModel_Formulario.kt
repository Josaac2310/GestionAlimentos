package com.example.josecarlos_sancho_pmdm.ViewModels

import androidx.lifecycle.ViewModel
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class state_newComponente(
    var nombre: String="",
    var tipo: Tipo_Componente=Tipo_Componente.RECETA,
    var grHC_ini: Double=0.0,
    var grLip_ini:Double=0.0,
    var grPro_ini:Double=0.0,
    var ingredientes:ArrayList<Ingrediente> = arrayListOf()
)

data class  state_newIngrediente(
    var nombre: String ="",
    var grHC_ini: Double=0.0,
    var grLip_ini:Double=0.0,
    var grPro_ini:Double=0.0,
    var cantidad:Double=100.0,
)

data class state_UIFormulario(
    var nombre: String=""
)

enum class Tipo_Componente {
    MENU,RECETA,DIETA
}

class ViewModel_Formulario:ViewModel() {

    private val _stateUiformulario = MutableStateFlow(state_UIFormulario())
    val stateUiformulario: StateFlow<state_UIFormulario> get() = _stateUiformulario

    private val _stateNewingrediente = MutableStateFlow(state_newIngrediente())
    val stateNewingrediente: StateFlow<state_newIngrediente> get() = _stateNewingrediente

    private val _stateNewcomponente = MutableStateFlow(state_newComponente())
    val stateNewComponente: StateFlow<state_newComponente> get() = _stateNewcomponente

    fun updateNombreComponente(nombre:String){
        _stateNewcomponente.value = _stateNewcomponente.value.copy(nombre=nombre)
    }

    fun updateTipoComponente(tipo: Tipo_Componente){
        _stateNewcomponente.value = _stateNewcomponente.value.copy(tipo = tipo)
    }
    fun updategr_HcComponente(grHC_ini: Double){
        _stateNewcomponente.value = _stateNewcomponente.value.copy(grHC_ini = grHC_ini)
    }
    fun updategr_LipComponente(grLip_ini: Double){
        _stateNewcomponente.value = _stateNewcomponente.value.copy(grLip_ini = grLip_ini)
    }

    fun updategrProComponente(grPro_ini: Double){
        _stateNewcomponente.value = _stateNewcomponente.value.copy(grPro_ini = grPro_ini)
    }
    fun addIngredienteComponente(ingrediente: Ingrediente){
        _stateNewcomponente.value = _stateNewcomponente.value.copy(ingredientes = ArrayList(_stateNewcomponente.value.ingredientes+ingrediente))
    }

    fun removeIngredienteComponente(ingrediente: Ingrediente){
        _stateNewcomponente.value = _stateNewcomponente.value.copy(ingredientes = ArrayList(_stateNewcomponente.value.ingredientes-ingrediente))
    }


    //Ingredientes
    fun updateNombreIngrediente(nombre:String){
        _stateNewingrediente.value = _stateNewingrediente.value.copy(nombre = nombre)
    }
    fun updategr_HcIngrediente(grHC_ini: Double){
        _stateNewingrediente.value = _stateNewingrediente.value.copy(grHC_ini = grHC_ini)
    }
    fun updategr_LipIngrediente(grLip_ini: Double){
        _stateNewingrediente.value = _stateNewingrediente.value.copy(grLip_ini = grLip_ini)
    }
    fun updategrProIngrediente(grPro_ini: Double){
        _stateNewingrediente.value = _stateNewingrediente.value.copy(grPro_ini = grPro_ini)
    }
    fun updategrCantidadIngrediente(cantidad: Double){
        _stateNewingrediente.value = _stateNewingrediente.value.copy(cantidad = cantidad)
    }

    fun clearIngrediente(){
        _stateNewingrediente.value=_stateNewingrediente.value.copy(nombre = "", grHC_ini = 0.0, grLip_ini = 0.0, grPro_ini = 0.0, cantidad = 0.0)
    }
}