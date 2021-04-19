package humber.its.drivershigh.api

import retrofit2.Retrofit

class DirectionClient {
    companion object {
        private var instance: DirectionApi? = null

        fun getInstance(): DirectionApi? {
            if (instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .build()
                    .create(DirectionApi::class.java)
            }

            return instance
        }
    }
}