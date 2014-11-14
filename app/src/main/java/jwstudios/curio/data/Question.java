package jwstudios.curio.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import jwstudios.curio.data.Option.AbstractOption.OptionMcp;
import jwstudios.curio.data.Option.AbstractOption.OptionMct;
import jwstudios.curio.persistence.DatabaseSchema.AnswerSql;
import jwstudios.curio.persistence.DatabaseSchema.OptionMcpSql;
import jwstudios.curio.persistence.DatabaseSchema.OptionMctSql;
import jwstudios.curio.persistence.DatabaseSchema.QuestionTargetSql;
import jwstudios.curio.persistence.DatabaseSchema.QuestionsSql;
import jwstudios.curio.persistence.PersistenceManager;
import jwstudios.curio.persistence.StandardSql;

/**
 * @author john.wright
 * @since Nov 11 2014
 */
public abstract class Question {
    private static final String LOGTAG = "Question";

    private long id;
    private final User user;
    private final String question;
    private final Collection<User> targets;
    private final QuestionType type;
    private final long startDate;
    private final long endDate;
    private final QuestionStatus status;

    private List<Option> options;
    private Collection<Answer> answers;

    public Question(final User user,
                    final String question,
                    final Collection<User> targets,
                    final QuestionType type,
                    final long startDate,
                    final long endDate,
                    final QuestionStatus status) {
        this.user = user;
        this.question = question;
        this.targets = targets;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public abstract List<Option> queryOptionsForQuestion(final SQLiteDatabase db);

    public void setOptions(final List<Option> options) {
        this.options = options;
    }

    public void setAnswers(final Collection<Answer> answers) {
        this.answers = answers;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Collection<User> getTargets() {
        return this.targets;
    }

    public QuestionType getType() {
        return this.type;
    }

    public QuestionStatus getStatus() {
        return this.status;
    }

    public Collection<Option> getOptions() {
        return this.options;
    }

    public String getQuestion() {
        return this.question;
    }

    public long persist(final SQLiteDatabase db) {
        final ContentValues values = new ContentValues(3);
        values.put(QuestionsSql.COLUMN_NAME_USER, this.user.getId());
        values.put(QuestionsSql.COLUMN_NAME_TYPE, this.type.getId());
        values.put(QuestionsSql.COLUMN_NAME_QUESTION, this.question);
        values.put(QuestionsSql.COLUMN_NAME_STATUS, this.status.getId());
        final long questionId = db.insert(QuestionsSql.TABLE_NAME, null, values);

        for (final Option option : this.options) {
            option.persist(questionId, db);
        }
        persistTargets(questionId, db);

        //TODO throw error if answers is filled out here? really shouldn't be creating a question with answers..

        return questionId;
    }

    private void persistTargets(final long questionId, final SQLiteDatabase db) {
        for (final User toUser : this.targets) {
            final ContentValues values = new ContentValues(3);
            values.put(QuestionTargetSql.COLUMN_NAME_QUESTION, questionId);
            values.put(QuestionTargetSql.COLUMN_NAME_FROM_USER, this.user.getId());
            values.put(QuestionTargetSql.COLUMN_NAME_TO_USER, toUser.getId());
            db.insert(QuestionTargetSql.TABLE_NAME, null, values);
        }
    }

    public static Collection<Question> getAllFullQuestions(final Context context) {
        final SQLiteDatabase db = PersistenceManager.get().getReadableDb(context);
        final List<Question> questions = new ArrayList<Question>();

        try {
            final Cursor c = db.rawQuery(QuestionsSql.SELECT_ALL_QUESTIONS, null);
            if (!c.moveToFirst()) {
                return Collections.EMPTY_LIST;
            }
            while (!c.isAfterLast()) {
                questions.add(buildQuestion(c, db));

                c.moveToNext();
            }
            c.close();
            db.close();
        }
        catch (final Exception e) {
            Log.e(LOGTAG, "error getting users from db", e);
            return Collections.EMPTY_LIST;
        }
        return questions;
    }

    //TODO can probably make this generic to handle optional stuff (ie queries that don't get every col)
    private static Question buildQuestion(final Cursor c, final SQLiteDatabase db) {
        final long id = c.getLong(c.getColumnIndexOrThrow(QuestionsSql._ID));
        final User user = User.fromId(c.getLong(c.getColumnIndexOrThrow(QuestionsSql.COLUMN_NAME_USER)), db);
        final String questionText = c.getString(c.getColumnIndexOrThrow(QuestionsSql.COLUMN_NAME_QUESTION));
        final QuestionType type = QuestionType.fromId(c.getInt(c.getColumnIndexOrThrow(QuestionsSql.COLUMN_NAME_TYPE)));
        final long startDate = c.getLong(c.getColumnIndexOrThrow(QuestionsSql.COLUMN_NAME_START_DATE));
        final long endDate = c.getLong(c.getColumnIndexOrThrow(QuestionsSql.COLUMN_NAME_END_DATE));
        final QuestionStatus status =
                QuestionStatus.fromId(c.getInt(c.getColumnIndexOrThrow(QuestionsSql.COLUMN_NAME_STATUS)));

        final Collection<User> targets = User.getTargetsForQuestionId(id, db);

        final Question question = Question.buildQuestion(user, questionText, targets, type, startDate, endDate, status);
        question.setId(id);

        question.fetchAndSetOptions(db);
        question.fetchAndSetAnswers(db);

        return question;
    }

    protected abstract void fetchAndSetAnswers(final SQLiteDatabase db);

    protected abstract void fetchAndSetOptions(final SQLiteDatabase db);

    protected Option getOptionFromId(final int optionId) {
        for (final Option option : getOptions()) {
            if (option.getId() == optionId) {
                return option;
            }
        }
        return null;
    }

    public static Question buildQuestion(final User user,
                                         final String questionText,
                                         final Collection<User> targets,
                                         final QuestionType type,
                                         final long startDate,
                                         final long endDate,
                                         final QuestionStatus status) {
        switch (type) {
            case MULTI_TEXT:
                return new QuestionMCT(user, questionText, targets, type, startDate, endDate, status);
            case MULTI_PIC:
                return new QuestionMCP(user, questionText, targets, type, startDate, endDate, status);
            case YES_NO:
                return new QuestionYN(user, questionText, targets, type, startDate, endDate, status);
        }
        throw new IllegalArgumentException("unexpected type");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("Question id: ").append(this.id).append("\n");
        sb.append("user: ").append(this.user).append("\n");
        sb.append("question: ").append(this.question).append("\n");

        sb.append("Targets: ");
        if (this.targets != null) {
            for (final User target : this.targets) {
                sb.append(target).append(" ");
            }
        }
        sb.append("\n");

        sb.append("Options: ");
        if (this.options != null) {
            for (final Option option : this.options) {
                sb.append(option).append(" ");
            }
        }
        sb.append("\n");

        sb.append("type: ").append(this.type).append("\n");
        sb.append("startDate: ").append(this.startDate).append("\n");
        sb.append("endDate: ").append(this.endDate).append("\n");
        sb.append("status: ").append(this.status).append("\n");

        sb.append("Answers: ");
        if (this.answers != null) {
            for (final Answer answer : this.answers) {
                sb.append(answer);
            }
        }
        return sb.toString();
    }

    public static class QuestionMCT
            extends Question {

        public QuestionMCT(final User user,
                           final String question,
                           final Collection<User> targets,
                           final QuestionType type,
                           final long startDate,
                           final long endDate,
                           final QuestionStatus status) {
            super(user, question, targets, type, startDate, endDate, status);
        }

        @Override
        public List<Option> queryOptionsForQuestion(final SQLiteDatabase db) {
            final Cursor c = db.rawQuery(StandardSql.OPTIONS_MCT_FOR_QUESTION, new String[]{String.valueOf(getId())});
            final List<Option> options = new ArrayList<Option>();

            try {
                if (!c.moveToFirst()) {
                    return Collections.EMPTY_LIST;
                }
                while (!c.isAfterLast()) {
                    final long id = c.getLong(c.getColumnIndexOrThrow(OptionMctSql._ID));
                    final String optionText = c.getString(c.getColumnIndexOrThrow(OptionMctSql.COLUMN_NAME_OPTION));
                    final Option option = new OptionMct(optionText);
                    option.setId(id);
                    options.add(option);

                    c.moveToNext();
                }
            }
            finally {
                c.close();
            }
            return options;
        }

        @Override
        protected void fetchAndSetAnswers(final SQLiteDatabase db) {
            final Cursor c = db.rawQuery(AnswerSql.ANSWER_FOR_QUESTION, new String[]{String.valueOf(getId())});
            final List<Answer> answers = new ArrayList<Answer>();

            try {
                if (!c.moveToFirst()) {
                    setAnswers(Collections.EMPTY_LIST);
                    return;
                }
                while (!c.isAfterLast()) {
                    final User user = User.fromId(c.getLong(c.getColumnIndexOrThrow(AnswerSql.COLUMN_NAME_USER)), db);
                    final Option option =
                            getOptionFromId(c.getInt(c.getColumnIndexOrThrow(AnswerSql.COLUMN_NAME_ANSWER)));
                    final Answer answer = new Answer(user, option);
                    answers.add(answer);

                    c.moveToNext();
                }
            }
            finally {
                c.close();
            }
            setAnswers(answers);
        }

        @Override
        protected void fetchAndSetOptions(final SQLiteDatabase db) {
            setOptions(queryOptionsForQuestion(db));
        }
    }

    public static class QuestionMCP
            extends Question {

        public QuestionMCP(final User user,
                           final String question,
                           final Collection<User> targets,
                           final QuestionType type,
                           final long startDate,
                           final long endDate,
                           final QuestionStatus status) {
            super(user, question, targets, type, startDate, endDate, status);
        }

        @Override
        public List<Option> queryOptionsForQuestion(final SQLiteDatabase db) {
            final Cursor c = db.rawQuery(StandardSql.OPTIONS_MCP_FOR_QUESTION, new String[]{String.valueOf(getId())});
            final List<Option> options = new ArrayList<Option>();

            try {
                if (!c.moveToFirst()) {
                    return Collections.EMPTY_LIST;
                }
                while (!c.isAfterLast()) {
                    final long id = c.getLong(c.getColumnIndexOrThrow(OptionMcpSql._ID));
                    final String optionText = c.getString(c.getColumnIndexOrThrow(OptionMcpSql.COLUMN_NAME_OPTION));
                    final String picture = c.getString(c.getColumnIndexOrThrow(OptionMcpSql.COLUMN_NAME_PICTURE));
                    final Option option = new OptionMcp(optionText, picture);
                    option.setId(id);
                    options.add(option);

                    c.moveToNext();
                }
            }
            finally {
                c.close();
            }
            return options;
        }

        @Override
        protected void fetchAndSetAnswers(final SQLiteDatabase db) {
            final Cursor c = db.rawQuery(AnswerSql.ANSWER_FOR_QUESTION, new String[]{String.valueOf(getId())});
            final List<Answer> answers = new ArrayList<Answer>();

            try {
                if (!c.moveToFirst()) {
                    setAnswers(Collections.EMPTY_LIST);
                    return;
                }
                while (!c.isAfterLast()) {
                    final User user = User.fromId(c.getLong(c.getColumnIndexOrThrow(AnswerSql.COLUMN_NAME_USER)), db);
                    final Option option = getOptionFromId(c.getInt(c.getColumnIndexOrThrow(AnswerSql.COLUMN_NAME_ANSWER)));
                    final Answer answer = new Answer(user, option);
                    answers.add(answer);

                    c.moveToNext();
                }
            }
            finally {
                c.close();
            }
            setAnswers(answers);
        }

        @Override
        protected void fetchAndSetOptions(final SQLiteDatabase db) {
            setOptions(queryOptionsForQuestion(db));
        }
    }

    public static class QuestionYN
            extends Question {
        private static final List<Option> OPTIONS = new ArrayList<Option>(2);

        static {
            OPTIONS.add(OptionYesNo.YES);
            OPTIONS.add(OptionYesNo.NO);
        }

        public QuestionYN(final User user,
                          final String question,
                          final Collection<User> targets,
                          final QuestionType type,
                          final long startDate,
                          final long endDate,
                          final QuestionStatus status) {
            super(user, question, targets, type, startDate, endDate, status);
        }

        @Override
        public List<Option> queryOptionsForQuestion(final SQLiteDatabase db) {
            return OPTIONS;
        }

        @Override
        protected void fetchAndSetAnswers(final SQLiteDatabase db) {
            final Cursor c = db.rawQuery(AnswerSql.ANSWER_FOR_QUESTION, new String[]{String.valueOf(getId())});
            final List<Answer> answers = new ArrayList<Answer>();

            try {
                if (!c.moveToFirst()) {
                    setAnswers(Collections.EMPTY_LIST);
                    return;
                }
                while (!c.isAfterLast()) {
                    final User user = User.fromId(c.getLong(c.getColumnIndexOrThrow(AnswerSql.COLUMN_NAME_USER)), db);
                    final Option option =
                            OptionYesNo.fromId(c.getInt(c.getColumnIndexOrThrow(AnswerSql.COLUMN_NAME_ANSWER)));
                    final Answer answer = new Answer(user, option);
                    answers.add(answer);

                    c.moveToNext();
                }
            }
            finally {
                c.close();
            }
            setAnswers(answers);
        }

        @Override
        protected void fetchAndSetOptions(final SQLiteDatabase db) {
            // dont actually have to do anything, options are predefined for Y/N
        }
    }
}