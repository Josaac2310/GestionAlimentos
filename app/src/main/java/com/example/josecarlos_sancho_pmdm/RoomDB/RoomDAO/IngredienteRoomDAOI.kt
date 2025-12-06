package com.example.josecarlos_sancho_pmdm.RoomDB.RoomDAO

import android.content.Context
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente
import com.example.josecarlos_sancho_pmdm.Modelo.IngredienteData

@Dao
interface IngredienteRoomDAOI {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIngrediente(ingrediente: Ingrediente):Unit

    @Query("SELECT * FROM Ingrediente WHERE componenteId=:idComponente AND id=:idIngrediente")
    suspend fun readIngredienteByComponente(idIngrediente: Int,idComponente:Int): Ingrediente

    @Query("SELECT * FROM Ingrediente WHERE componenteId=:idComponente")
    suspend fun readIngredientesByComponente(idComponente:Int):List<Ingrediente>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateIngrediente (ingrediente: Ingrediente):Unit

    @Query("DELETE FROM Ingrediente WHERE componenteId=:idComponente AND id=:idIngrediente")
    suspend fun deleteIngredientebyComponente(idComponente: Int, idIngrediente: Int):Int

    @Query("DELETE FROM Ingrediente WHERE componenteId = :componenteId")
    suspend fun deleteByComponente(componenteId: Int)
}