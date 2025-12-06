package com.example.josecarlos_sancho_pmdm.RoomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.josecarlos_sancho_pmdm.Modelo.ComponenteDieta
import com.example.josecarlos_sancho_pmdm.Modelo.Ingrediente
import com.example.josecarlos_sancho_pmdm.RoomDB.RoomDAO.ComponenteRoomDAOI
import com.example.josecarlos_sancho_pmdm.RoomDB.RoomDAO.IngredienteRoomDAOI

@Database(entities = [ComponenteDieta::class, Ingrediente::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun ingredienteDAO(): IngredienteRoomDAOI
    abstract fun componenteDietaDAO(): ComponenteRoomDAOI
}