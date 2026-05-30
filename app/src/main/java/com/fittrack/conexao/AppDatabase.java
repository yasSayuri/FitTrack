package com.fittrack.conexao;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.fittrack.entidades.Treino;
import com.fittrack.entidades.TreinoPlano;
import com.fittrack.entidades.User;
import com.fittrack.dao.TreinoDao;
import com.fittrack.dao.TreinoPlanoDao;
import com.fittrack.dao.UserDao;

@Database(entities = {User.class, Treino.class, TreinoPlano.class}, version = 15, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract TreinoDao treinoDao();
    public abstract TreinoPlanoDao treinoPlanoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "fittrack_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}