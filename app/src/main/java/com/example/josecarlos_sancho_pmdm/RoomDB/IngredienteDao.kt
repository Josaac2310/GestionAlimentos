package com.example.josecarlos_sancho_pmdm.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente

@Dao
interface IngredienteDao {
    @Insert
    suspend fun insertIngrediente(ingrediente: Ingrediente)

    @Query("SELECT * FROM ingrediente WHERE id = :idIngrediente AND componenteId = :idComponente")
    suspend fun readIngredienteByComponente(idIngrediente: Int, idComponente: Int): Ingrediente

    @Query("SELECT * FROM ingrediente WHERE componenteId = :idComponente")
    suspend fun readIngredientesByComponente(idComponente: Int): List<Ingrediente>

    @Update
    suspend fun updateIngrediente(ingrediente: Ingrediente)

    @Query("DELETE FROM ingrediente WHERE componenteId = :idComponente AND id = :idIngrediente")
    suspend fun deleteIngredientebyComponente(idComponente: Int, idIngrediente: Int): Int

    @Query("DELETE FROM ingrediente WHERE componenteId = :componenteId")
    suspend fun deleteByComponente(componenteId: Int)
} 