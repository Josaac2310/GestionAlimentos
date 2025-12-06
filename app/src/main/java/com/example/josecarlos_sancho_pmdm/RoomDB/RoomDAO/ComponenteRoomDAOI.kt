package com.example.josecarlos_sancho_pmdm.RoomDB.RoomDAO

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente
import com.example.josecarlos_sancho_pmdm.Modelo.TipoComponente

@Dao
interface ComponenteRoomDAOI {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertComponente(componente: ComponenteDieta):Long

    @Query("SELECT * FROM ComponenteDieta")
    suspend fun selectComponentes(): MutableList<ComponenteDieta>

    @Query("SELECT * FROM ComponenteDieta WHERE tipo=:tipo")
    suspend fun selectComponentesByTipo(tipo: TipoComponente): MutableList<ComponenteDieta>

    @Query("SELECT * FROM ComponenteDieta WHERE id=:id")
    suspend fun selectComponente(id:Int): ComponenteDieta?

    @Query("SELECT * \n" +
            "FROM ComponenteDieta\n" +
            "WHERE id IN (SELECT componenteId \n" +
            "             FROM Ingrediente\n" +
            "             WHERE nombre=:nombreIngrediente)")
    suspend fun selectComponenteByIngrediente(nombreIngrediente: String): MutableList<ComponenteDieta>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateComponente(componenteOld: ComponenteDieta, componenteNew: ComponenteDieta)

    @Query("DELETE FROM ComponenteDieta WHERE id=:id")
    suspend fun deleteComponente(id: Int):Int
}