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
    var id: String = UUID.randomUUID().toString(),
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