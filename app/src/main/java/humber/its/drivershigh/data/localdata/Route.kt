package humber.its.drivershigh.data.localdata

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "routes")
data class Route (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val duration: Int,
    val length: Float,
    val startLat: Double,
    val startLng: Double,
    val endLat: Double,
    val endLng: Double
    )