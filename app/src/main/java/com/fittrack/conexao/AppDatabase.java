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

@Database(entities = {User.class, Treino.class, TreinoPlano.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract TreinoDao treinoDao();
    public abstract TreinoPlanoDao treinoPlanoDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fittrack_db")
                    .fallbackToDestructiveMigration(false)
                    .build();
        }
        return INSTANCE;
    }
}