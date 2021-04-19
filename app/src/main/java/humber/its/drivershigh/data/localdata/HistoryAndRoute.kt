package humber.its.drivershigh.data.localdata

import androidx.room.Embedded
import androidx.room.Relation

data class HistoryAndRoute(
    @Embedded val history: History,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "id"
    )
    val route: Route
)