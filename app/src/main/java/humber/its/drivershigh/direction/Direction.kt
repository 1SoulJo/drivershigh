package humber.its.drivershigh.direction

data class Direction(
    val routes: List<Route>
)

data class Route(
    val legs: List<Leg>,
    val overview_polyline: Points
)

data class Points(
    val points: String
)

data class Leg(
    val steps: List<Step>
)

data class Step(
    val start_location: Loc,
    val end_location: Loc
)

data class Loc(
    val lat: Double,
    val lng: Double
)