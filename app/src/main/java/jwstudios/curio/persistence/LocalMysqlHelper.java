package jwstudios.curio.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import jwstudios.curio.persistence.DatabaseSchema.AnswerSql;
import jwstudios.curio.persistence.DatabaseSchema.CustomAnswerSql;
import jwstudios.curio.persistence.DatabaseSchema.OptionMcpSql;
import jwstudios.curio.persistence.DatabaseSchema.OptionMctSql;
import jwstudios.curio.persistence.DatabaseSchema.QuestionStatusSql;
import jwstudios.curio.persistence.DatabaseSchema.QuestionTargetSql;
import jwstudios.curio.persistence.DatabaseSchema.QuestionTypesSql;
import jwstudios.curio.persistence.DatabaseSchema.QuestionsSql;
import jwstudios.curio.persistence.DatabaseSchema.UsersSql;

/**
 * Sqlite db helper for versioning of the sqlite loader.  Handles schema changes.
 *
 * @author john.wright
 * @since Nov 9 2014
 */
class LocalMysqlHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "curio.db";

    LocalMysqlHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(UsersSql.SQL_CREATE_TABLE);
        db.execSQL(QuestionsSql.SQL_CREATE_TABLE);
        db.execSQL(QuestionTargetSql.SQL_CREATE_TABLE);
        db.execSQL(QuestionStatusSql.SQL_CREATE_TABLE);
        db.execSQL(QuestionTypesSql.SQL_CREATE_TABLE);
        db.execSQL(OptionMctSql.SQL_CREATE_TABLE);
        db.execSQL(OptionMcpSql.SQL_CREATE_TABLE);
        db.execSQL(AnswerSql.SQL_CREATE_TABLE);
        db.execSQL(CustomAnswerSql.SQL_CREATE_TABLE);

        //TODO remove this before publishing!
        TestDataLoadFactory.get().loadTestData(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(UsersSql.SQL_DELETE_TABLE);
        db.execSQL(QuestionsSql.SQL_DELETE_TABLE);
        db.execSQL(QuestionTargetSql.SQL_DELETE_TABLE);
        db.execSQL(QuestionStatusSql.SQL_DELETE_TABLE);
        db.execSQL(QuestionTypesSql.SQL_DELETE_TABLE);
        db.execSQL(OptionMctSql.SQL_DELETE_TABLE);
        db.execSQL(OptionMcpSql.SQL_DELETE_TABLE);
        db.execSQL(AnswerSql.SQL_DELETE_TABLE);
        db.execSQL(CustomAnswerSql.SQL_DELETE_TABLE);
        onCreate(db);
    }
}
