package jp.second_wave.equipment_management_app.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  @ColumnInfo(name = "first_name") val firstName: String,
  @ColumnInfo(name = "last_name") val lastName: String?,
  val email: String?
)
