package org.takingroot.assignment.models

import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.annotations.Expose
import java.util.*

@Entity(tableName = "surveys")
data class Survey(
    @Expose
    @PrimaryKey
    @NonNull
    var id: UUID = UUID.randomUUID(),

//    @NonNull
//    var name: String = "",
//
//    @NonNull
//    @Expose
//    var payload: Map<String, Any> = mapOf(),

//    @NonNull
//    var first_name: String = "John",
//    @NonNull
//    var last_name: String = "Tom",
//    @NonNull
//    var birth_date: String = "2001/03/11",
//    @NonNull
//    var email: String = "mil@kko.co",

    @Expose
    @NonNull
    val first_name: String,

    @Expose
    @NonNull
    val last_name: String,

    @Expose
    @NonNull
    val birth_date: String,

    @Expose
    @NonNull
    val email: String,


)

@Dao
interface SurveyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg survey: Survey)

    @Query("select * from surveys")
    suspend fun getAll(): List<Survey>

    @Delete
    suspend fun delete(vararg surveys: Survey)
}