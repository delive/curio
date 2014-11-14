package jwstudios.curio.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import jwstudios.curio.data.User;
import jwstudios.curio.persistence.DatabaseSchema.UsersSql;

/**
 * Manages the local persistence layer on sqlite
 *
 * @author john.wright
 * @since Nov 13, 2014
 */
public class PersistenceManager {
    private static final String LOGTAG = "PersistenceManager";
    private static final PersistenceManager SINGLETON = new PersistenceManager();

    private PersistenceManager() {
        //singleton
    }

    public static PersistenceManager get() {
        return SINGLETON;
    }

    public Collection<User> getUsers(final Context context) {
        final SQLiteDatabase db = getReadableDb(context);
        final List<User> users = new ArrayList<User>();

        try {
            final Cursor c = db.rawQuery(UsersSql.SELECT_ALL_USERS, null);
            if (!c.moveToFirst()) {
                return Collections.EMPTY_LIST;
            }
            while (!c.isAfterLast()) {
                final long uid = c.getLong(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_ID));
                final String firstName = c.getString(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_FIRST_NAME));
                final String lastName = c.getString(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_LAST_NAME));
                final User user = new User(uid, firstName, lastName);
                users.add(user);

                c.moveToNext();
            }
            c.close();
            db.close();
        }
        catch (final Exception e) {
            Log.e(LOGTAG, "error getting users from db", e);
            return Collections.EMPTY_LIST;
        }
        return users;
    }

    //TODO why do i have to reconstruct the local helper with every new context?
    public SQLiteDatabase getReadableDb(final Context context) {
        final LocalMysqlHelper dbHelper = new LocalMysqlHelper(context);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            throw new IllegalStateException("could not get readable db to local storage");
        }
        return db;
    }

    public SQLiteDatabase getWriteableDb(final Context context) {
        final LocalMysqlHelper dbHelper = new LocalMysqlHelper(context);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            throw new IllegalStateException("could not get writeable db to local storage");
        }
        return db;
    }
}
