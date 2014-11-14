package jwstudios.curio.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import jwstudios.curio.data.Answer;
import jwstudios.curio.data.Option;
import jwstudios.curio.data.Option.AbstractOption.OptionMcp;
import jwstudios.curio.data.Option.AbstractOption.OptionMct;
import jwstudios.curio.data.OptionYesNo;
import jwstudios.curio.data.Question;
import jwstudios.curio.data.QuestionStatus;
import jwstudios.curio.data.QuestionType;
import jwstudios.curio.data.User;
import jwstudios.curio.persistence.DatabaseSchema.QuestionStatusSql;
import jwstudios.curio.persistence.DatabaseSchema.QuestionTypesSql;
import jwstudios.curio.persistence.DatabaseSchema.UsersSql;

/**
 * @author john.wright
 * @since Nov 11, 2014
 */
class TestDataLoadFactory {
    private static TestDataLoadFactory DEFAULT = new TestDataLoadFactory();

    private static final String LOGTAG = "TestDataLoadFactory";
    private static final int DAY_INCRMENT = (1000 * 60 * 60 * 24);

    private static final PersistenceManager PERSISTENCE = PersistenceManager.get();

    private static User[] TEST_USERS =
            new User[]{new User(1, "John", "Wright"), new User(2, "Bryan", "Regis"), new User(3, "Mike", "Barrameda"), new User(4, "Michael", "Jackson")};

    private TestDataLoadFactory() {
        //factory class
    }

    static TestDataLoadFactory get() {
        return DEFAULT;
    }

    /**
     * loads test data into the db
     *
     * @param db writeable sqlitedb - DOES NOT CLOSE DB CONNECTION
     */
    void loadTestData(final SQLiteDatabase db) {
        // most likely calling this on boot, don't throw a hard error for now
        try {
            loadUsers(db);
            loadQuestionStatus(db);
            loadQuestionTypes(db);
            loadQuestionAndAnswers(db);
        }
        catch (final Exception e) {
            Log.e(LOGTAG, "error loading test data", e);
        }
    }

    private void loadQuestionTypes(final SQLiteDatabase db) {
        ContentValues values = new ContentValues(2);
        values.put(QuestionTypesSql.COLUMN_NAME_ID, QuestionType.MULTI_TEXT.getId());
        values.put(QuestionTypesSql.COLUMN_NAME_NAME, QuestionType.MULTI_TEXT.getName());
        db.insert(QuestionTypesSql.TABLE_NAME, null, values);

        values = new ContentValues(2);
        values.put(QuestionTypesSql.COLUMN_NAME_ID, QuestionType.MULTI_PIC.getId());
        values.put(QuestionTypesSql.COLUMN_NAME_NAME, QuestionType.MULTI_PIC.getName());
        db.insert(QuestionTypesSql.TABLE_NAME, null, values);

        values = new ContentValues(2);
        values.put(QuestionTypesSql.COLUMN_NAME_ID, QuestionType.YES_NO.getId());
        values.put(QuestionTypesSql.COLUMN_NAME_NAME, QuestionType.YES_NO.getName());
        db.insert(QuestionTypesSql.TABLE_NAME, null, values);

    }

    private void loadQuestionStatus(final SQLiteDatabase db) {
        ContentValues values = new ContentValues(2);
        values.put(QuestionStatusSql.COLUMN_NAME_ID, QuestionStatus.IN_PROGRESS.getId());
        values.put(QuestionStatusSql.COLUMN_NAME_NAME, QuestionStatus.IN_PROGRESS.getName());
        db.insert(QuestionStatusSql.TABLE_NAME, null, values);

        values = new ContentValues(2);
        values.put(QuestionStatusSql.COLUMN_NAME_ID, QuestionStatus.COMPLETED.getId());
        values.put(QuestionStatusSql.COLUMN_NAME_NAME, QuestionStatus.COMPLETED.getName());
        db.insert(QuestionStatusSql.TABLE_NAME, null, values);
    }

    private void loadUsers(final SQLiteDatabase db) {
        for (final User user : TEST_USERS) {
            ContentValues values = new ContentValues(4);
            values.put(UsersSql.COLUMN_NAME_ID, user.getId());
            values.put(UsersSql.COLUMN_NAME_FIRST_NAME, user.getFirstName());
            values.put(UsersSql.COLUMN_NAME_LAST_NAME, user.getLastName());
            db.insert(UsersSql.TABLE_NAME, null, values);
        }
    }

    private void loadQuestionAndAnswers(final SQLiteDatabase db) {
        final long startDate = new Date().getTime() - DAY_INCRMENT;
        final long endDate = new Date().getTime();

        final Question question1 = Question.buildQuestion(TEST_USERS[0], "What should we eat for dinner?", Arrays.asList(TEST_USERS[1], TEST_USERS[2]), QuestionType.MULTI_TEXT, startDate, endDate, QuestionStatus.COMPLETED);
        List<Option> options = new ArrayList<Option>(4);
        options.add(new OptionMct("Pizza"));
        options.add(new OptionMct("Burger"));
        options.add(new OptionMct("Chinese"));
        options.add(new OptionMct("Hotdog"));

        question1.setOptions(options);

        final long question1Id = question1.persist(db);
        question1.setId(question1Id);

        List<Option> persistedOptions = question1.queryOptionsForQuestion(db);

        new Answer(TEST_USERS[1], persistedOptions.get(2)).persist(question1Id, db);
        new Answer(TEST_USERS[2], persistedOptions.get(1)).persist(question1Id, db);


        final Question question2 = Question.buildQuestion(TEST_USERS[1], "Best picture?", Arrays.asList(TEST_USERS[2]), QuestionType.MULTI_PIC, startDate, endDate, QuestionStatus.IN_PROGRESS);
        options = new ArrayList<Option>(3);
        options.add(new OptionMcp("Beach", "beach.jpg"));
        options.add(new OptionMcp("Space", "space.jpg"));
        options.add(new OptionMcp("Chair", "chair.jpg"));

        question2.setOptions(options);

        final long question2Id = question2.persist(db);
        question2.setId(question2Id);

        persistedOptions = question2.queryOptionsForQuestion(db);

        new Answer(TEST_USERS[2], persistedOptions.get(1)).persist(question2Id, db);

        final Question question3 = Question.buildQuestion(TEST_USERS[2], "Go to the movies today?", Arrays.asList(TEST_USERS[0], TEST_USERS[1], TEST_USERS[3]), QuestionType.YES_NO, startDate, endDate, QuestionStatus.COMPLETED);

        question3.setOptions(options);

        final long question3Id = question3.persist(db);
        question3.setId(question3Id);

        new Answer(TEST_USERS[0], OptionYesNo.NO).persist(question3Id, db);
        new Answer(TEST_USERS[1], OptionYesNo.YES).persist(question3Id, db);
        new Answer(TEST_USERS[3], OptionYesNo.YES).persist(question3Id, db);
    }
}
