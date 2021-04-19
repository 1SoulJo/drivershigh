package humber.its.drivershigh.data.localdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Route::class, History::class],
    version = 1
)
abstract class MainDatabase: RoomDatabase() {
    companion object {
        private var db: MainDatabase? = null
        fun build(ctx: Context): MainDatabase {
            if (db == null) {
                db = Room.databaseBuilder(ctx, MainDatabase::class.java, "routes-db").build()
            }
            return db as MainDatabase
        }
    }

    abstract fun routeDao(): RouteDao
    abstract fun historyDao(): HistoryDao
}