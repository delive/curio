package jwstudios.curio.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import jwstudios.curio.persistence.DatabaseSchema.UsersSql;

/**
 * helper class for userdata
 */
public class User {
    private final long id;
    private String firstName;
    private String lastName;

    public User(final long id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public long getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String toString() {
        return "uid: " + this.id + ", " + "firstname: " + this.firstName + ", " + "lastname: " + this.lastName;
    }

    public static User fromId(final long userId, final SQLiteDatabase db) {
        final Cursor c = db.rawQuery(UsersSql.SELECT_FROM_ID, new String[]{String.valueOf(userId)});
        try {
            if (!c.moveToFirst()) {
                return null;
            }
            final long uid = c.getLong(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_ID));
            final String firstName = c.getString(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_FIRST_NAME));
            final String lastName = c.getString(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_LAST_NAME));
            return new User(uid, firstName, lastName);
        }
        finally {
            c.close();
        }
    }

    public static Collection<User> getTargetsForQuestionId(final long id, final SQLiteDatabase db) {
        final List<User> users = new ArrayList<User>();

        final Cursor c = db.rawQuery(UsersSql.SELECT_TARGETS_FOR_QUESTION, new String[]{String.valueOf(id)});
        try {

            if (!c.moveToFirst()) {
                return Collections.EMPTY_LIST;
            }
            while (!c.isAfterLast()) {
                // shouldn't i have to refer to the column name as tables._id since i aliased them?
                final long uid = c.getLong(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_ID));
                final String firstName = c.getString(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_FIRST_NAME));
                final String lastName = c.getString(c.getColumnIndexOrThrow(UsersSql.COLUMN_NAME_LAST_NAME));
                final User user = new User(uid, firstName, lastName);
                users.add(user);

                c.moveToNext();
            }
            return users;
        }
        finally {
            c.close();
        }
    }
}
