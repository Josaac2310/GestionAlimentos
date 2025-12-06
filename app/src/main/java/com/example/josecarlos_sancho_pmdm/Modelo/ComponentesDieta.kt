package com.example.josecarlos_sancho_pmdm.Modelo

import java.io.Serializable
import androidx.room.*


enum class TipoComponente {

    SIMPLE,PROCESADO,MENU,RECETA,DIETA

}
data class IngredienteData(
    var id: Int=0,
    var nombre: String ="",
    var grHC_ini: Double=0.0,
    var grLip_ini:Double=0.0,
    var grPro_ini:Double=0.0,
    var cantidad:Double=100.0,
    var componenteId: Int=0
)

data class ComponenteDietaData(
    var id: Int=0,
    var tipo:String="",
    var nombre: String ="",
    var grHC_ini: Double=0.0,
    var grLip_ini:Double=0.0,
    var grPro_ini:Double=0.0,
    var ingredientes:ArrayList<IngredienteData> = arrayListOf()
)

@Entity(tableName = "ComponenteDieta")
data class ComponenteDieta(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var tipo: String = "",
    var nombre: String = "",
    var grHC_ini: Double = 0.0,
    var grLip_ini: Double = 0.0,
    var grPro_ini: Double = 0.0
) : Serializable

@Entity(tableName = "Ingrediente",
    foreignKeys = [ForeignKey(
        entity = ComponenteDieta::class,
        parentColumns = ["id"],
        childColumns = ["componenteId"],
        onDelete = ForeignKey.CASCADE
    )])
data class Ingrediente(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    var nombre: String ="",
    var grHC_ini: Double=0.0,
    var grLip_ini:Double=0.0,
    var grPro_ini:Double=0.0,
    var cantidad:Double=100.0,
    var componenteId: Int=0
): Serializable

