package humber.its.drivershigh.data.localdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "histories")
data class History (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val routeId: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val date: String
)
