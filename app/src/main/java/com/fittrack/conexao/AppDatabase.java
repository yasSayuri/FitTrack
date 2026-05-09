package com.fittrack.conexao;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.fittrack.entidades.Treino;
import com.fittrack.entidades.User;
import com.fittrack.dao.TreinoDao;
import com.fittrack.dao.UserDao;

@Database(entities = {User.class, Treino.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract TreinoDao treinoDao();

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