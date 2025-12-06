package com.example.josecarlos_sancho_pmdm.RoomDB

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente
import com.example.josecarlos_sancho_pmdm.Modelo.TipoComponente

object BDController {

    lateinit var db: AppDataBase

    fun init (context: Context) {
        db= Room.databaseBuilder(context, AppDataBase::class.java, "appDB").build()
    }

    //COMPONENTES DIETA
    suspend fun insertComponente(componente:ComponenteDieta):Long{
        val dao=db.componenteDietaDAO()
        return dao.insertComponente(componente)

    }

    suspend fun selectAllComponentes(): MutableList<ComponenteDieta>{
        val dao=db.componenteDietaDAO()
        return dao.selectComponentes()
    }

    suspend fun selectComponenteByTipo(tipo: TipoComponente):MutableList<ComponenteDieta>{
        val dao=db.componenteDietaDAO()
        val res=dao.selectComponentesByTipo(tipo)
        return res;
    }

    suspend fun selectComponente(id:Int): ComponenteDieta?{
        val dao=db.componenteDietaDAO()
        val res=dao.selectComponente(id)
        return res
    }

    suspend fun selectComponenteByIngrediente(nombreIngrediente: String): MutableList<ComponenteDieta>{
        val dao=db.componenteDietaDAO()
        val res= dao.selectComponenteByIngrediente(nombreIngrediente)
        return res
    }

    suspend fun updateComponente(componenteOld: ComponenteDieta, componenteNew: ComponenteDieta){
        val dao=db.componenteDietaDAO()
        dao.updateComponente(componenteOld, componenteNew)
    }

    suspend fun deleteComponente(id: Int):Boolean{
        val dao=db.componenteDietaDAO()
        val result= dao.deleteComponente(id)
        if (result>0) return true
        else return false
    }



    //INGREDIENTES
    suspend fun insertIngrediente(ingrediente: Ingrediente){
        val dao=db.ingredienteDAO()
        dao.insertIngrediente(ingrediente)
    }

    suspend fun selectIngredienteByComponente(idIngrediente: Int,idComponente:Int):Ingrediente{
        val dao=db.ingredienteDAO()
        val res= dao.readIngredienteByComponente(idIngrediente, idComponente)
        return res
    }

    suspend fun selectIngredientesByComponente(idComponente:Int):List<Ingrediente>{
        val dao=db.ingredienteDAO()
        return dao.readIngredientesByComponente(idComponente)
    }

    suspend fun updateIngrediente(ingrediente: Ingrediente){
        val dao=db.ingredienteDAO()
        dao.updateIngrediente(ingrediente)
    }

    suspend fun deleteIngredienteByComponente(idComponente: Int, idIngrediente: Int):Boolean{
        val dao=db.ingredienteDAO()
        val result=dao.deleteIngredientebyComponente(idComponente, idIngrediente)
        if(result>0) return true
        else return false
    }

    suspend fun deleteIngredientesByComponente(componenteId: Int) {
        val dao = db.ingredienteDAO()
        dao.deleteByComponente(componenteId)
    }

}