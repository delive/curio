package jwstudios.curio.persistence;

import android.provider.BaseColumns;

/**
 * DB schema for curio tables including create and delete sql.
 *
 * @author john.wright
 * @since Nov 10 2014
 */
public class DatabaseSchema {
    private DatabaseSchema() {
    }

    public static abstract class UsersSql
            implements BaseColumns {

        public static final String TABLE_NAME = "users";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FIRST_NAME = "firstname";
        public static final String COLUMN_NAME_LAST_NAME = "lastname";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + UsersSql.TABLE_NAME + " (" +
                        UsersSql.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                        UsersSql.COLUMN_NAME_FIRST_NAME + " TEXT, " +
                        UsersSql.COLUMN_NAME_LAST_NAME + " TEXT)";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + UsersSql.TABLE_NAME;

        public static final String SELECT_FROM_ID =
                "select * from " + UsersSql.TABLE_NAME + " where " + UsersSql.COLUMN_NAME_ID + " = ?";

        public static String SELECT_ALL_USERS = "select * from " + UsersSql.TABLE_NAME;

        public static final String SELECT_TARGETS_FOR_QUESTION = "select * from " + TABLE_NAME + " users " +
                "inner join ( " + QuestionTargetSql.SQL_SELECT_TARGET_FOR_QUESTION + ") targets " +
                "on users." + COLUMN_NAME_ID + " = targets."+ QuestionTargetSql.COLUMN_NAME_TO_USER;
    }

    public static abstract class QuestionsSql
            implements BaseColumns {

        public static final String TABLE_NAME = "questions";

        public static final String COLUMN_NAME_SID = "sid";
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_START_DATE = "startdate";
        public static final String COLUMN_NAME_END_DATE = "enddate";
        public static final String COLUMN_NAME_STATUS = "status";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + QuestionsSql.TABLE_NAME + " (" +
                        QuestionsSql._ID + " INTEGER PRIMARY KEY," +
                        QuestionsSql.COLUMN_NAME_SID + " INTEGER," +
                        QuestionsSql.COLUMN_NAME_USER + " INTEGER," +
                        QuestionsSql.COLUMN_NAME_QUESTION + " TEXT," +
                        QuestionsSql.COLUMN_NAME_TYPE + " INTEGER," +
                        QuestionsSql.COLUMN_NAME_START_DATE + " INTEGER," +
                        QuestionsSql.COLUMN_NAME_END_DATE + " INTEGER," +
                        QuestionsSql.COLUMN_NAME_STATUS + " INTEGER" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + QuestionsSql.TABLE_NAME;

        public static final String SELECT_ALL_QUESTIONS = "select * from " + TABLE_NAME;
    }

    public static abstract class QuestionTargetSql
            implements BaseColumns {

        public static final String TABLE_NAME = "questiontarget";

        public static final String COLUMN_NAME_SID = "sid";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_FROM_USER = "fromuser";
        public static final String COLUMN_NAME_TO_USER = "touser";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + QuestionTargetSql.TABLE_NAME + " (" +
                        QuestionTargetSql._ID + " INTEGER PRIMARY KEY," +
                        QuestionTargetSql.COLUMN_NAME_SID + " INTEGER," +
                        QuestionTargetSql.COLUMN_NAME_QUESTION + " INTEGER," +
                        QuestionTargetSql.COLUMN_NAME_FROM_USER + " INTEGER," +
                        QuestionTargetSql.COLUMN_NAME_TO_USER + " INTEGER" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + QuestionTargetSql.TABLE_NAME;

        public static final String SQL_SELECT_TARGET_FOR_QUESTION = "SELECT * from " + TABLE_NAME + " where " + QuestionTargetSql.COLUMN_NAME_QUESTION + " = ?";
    }

    public static abstract class QuestionStatusSql
            implements BaseColumns {

        public static final String TABLE_NAME = "questionstatus";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + QuestionStatusSql.TABLE_NAME + " (" +
                        QuestionStatusSql.COLUMN_NAME_ID + " INTEGER," +
                        QuestionStatusSql.COLUMN_NAME_NAME + " TEXT" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + QuestionStatusSql.TABLE_NAME;
    }

    public static abstract class QuestionTypesSql
            implements BaseColumns {

        public static final String TABLE_NAME = "questiontypes";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + QuestionTypesSql.TABLE_NAME + " (" +
                        QuestionTypesSql.COLUMN_NAME_ID + " INTEGER," +
                        QuestionTypesSql.COLUMN_NAME_NAME + " TEXT" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + QuestionTypesSql.TABLE_NAME;
    }

    public static abstract class OptionMctSql
            implements BaseColumns {

        public static final String TABLE_NAME = "optionmct";

        public static final String COLUMN_NAME_SID = "sid";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_OPTION = "option";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + OptionMctSql.TABLE_NAME + " (" +
                        OptionMctSql._ID + " INTEGER PRIMARY KEY," +
                        OptionMctSql.COLUMN_NAME_SID + " INTEGER," +
                        OptionMctSql.COLUMN_NAME_QUESTION + " INTEGER," +
                        OptionMctSql.COLUMN_NAME_OPTION + " TEXT" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + OptionMctSql.TABLE_NAME;
    }

    public static abstract class OptionMcpSql
            implements BaseColumns {

        public static final String TABLE_NAME = "optionmcp";

        public static final String COLUMN_NAME_SID = "sid";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_OPTION = "option";
        public static final String COLUMN_NAME_PICTURE = "picture";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + OptionMcpSql.TABLE_NAME + " (" +
                        OptionMcpSql._ID + " INTEGER PRIMARY KEY," +
                        OptionMcpSql.COLUMN_NAME_SID + " INTEGER," +
                        OptionMcpSql.COLUMN_NAME_QUESTION + " INTEGER," +
                        OptionMcpSql.COLUMN_NAME_OPTION + " TEXT," +
                        OptionMcpSql.COLUMN_NAME_PICTURE + " TEXT" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + OptionMcpSql.TABLE_NAME;
    }

    public static abstract class AnswerSql
            implements BaseColumns {

        public static final String TABLE_NAME = "answers";

        public static final String COLUMN_NAME_SID = "sid";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_ANSWER = "answer";
        public static final String COLUMN_NAME_DATE = "date";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + AnswerSql.TABLE_NAME + " (" +
                        AnswerSql._ID + " INTEGER PRIMARY KEY," +
                        AnswerSql.COLUMN_NAME_SID + " INTEGER," +
                        AnswerSql.COLUMN_NAME_QUESTION + " INTEGER," +
                        AnswerSql.COLUMN_NAME_USER + " INTEGER," +
                        AnswerSql.COLUMN_NAME_ANSWER + " INTEGER," +
                        AnswerSql.COLUMN_NAME_DATE + " INTEGER" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + AnswerSql.TABLE_NAME;
        public static final String ANSWER_FOR_QUESTION = "select * from " + TABLE_NAME + " where " + COLUMN_NAME_QUESTION + " = ?";
    }

    public static abstract class CustomAnswerSql
            implements BaseColumns {

        public static final String TABLE_NAME = "customanswers";

        public static final String COLUMN_NAME_SID = "sid";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_ANSWER = "answer";
        public static final String COLUMN_NAME_DATE = "date";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + CustomAnswerSql.TABLE_NAME + " (" +
                        CustomAnswerSql._ID + " INTEGER PRIMARY KEY," +
                        CustomAnswerSql.COLUMN_NAME_SID + " INTEGER," +
                        CustomAnswerSql.COLUMN_NAME_QUESTION + " INTEGER," +
                        CustomAnswerSql.COLUMN_NAME_USER + " INTEGER," +
                        CustomAnswerSql.COLUMN_NAME_ANSWER + " TEXT," +
                        CustomAnswerSql.COLUMN_NAME_DATE + " INTEGER" +
                        " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + CustomAnswerSql.TABLE_NAME;
    }
}
