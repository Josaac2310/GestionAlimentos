package com.example.josecarlos_sancho_pmdm.ViewModels

import androidx.lifecycle.ViewModel
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDietaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class stateListado(
    val listado: ArrayList<ComponenteDietaData> = arrayListOf(),
    val listReady:Boolean=false,
    val componenteSeleccionado:ComponenteDietaData?=null
)

class ViewModel_Listado:ViewModel() {
    private val _stateListado= MutableStateFlow(stateListado())
    val stateListado:StateFlow<stateListado> get() = _stateListado

    fun addComponenteDieta(componenteDieta: ComponenteDietaData){
        _stateListado.value =  _stateListado.value.copy(listado = ArrayList(_stateListado.value.listado+componenteDieta))
    }

    fun removeComponenteDieta(componenteDieta: ComponenteDietaData){
        _stateListado.value =  _stateListado.value.copy(listado = ArrayList(_stateListado.value.listado-componenteDieta))
    }

    fun setReady(boolean: Boolean){
        _stateListado.value = _stateListado.value.copy(listReady = boolean)
    }

    fun setComponentes(lista:ArrayList<ComponenteDietaData>){
        _stateListado.value=_stateListado.value.copy(listado = lista)
    }

    fun setComponenteSeleccionado(componente:ComponenteDietaData?){
        _stateListado.value=_stateListado.value.copy(componenteSeleccionado = componente)
    }

    fun clearComponentes() {
        _stateListado.value = _stateListado.value.copy(
            listado = arrayListOf()
        )
    }
}