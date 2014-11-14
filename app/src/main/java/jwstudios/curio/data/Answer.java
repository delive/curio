package jwstudios.curio.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import jwstudios.curio.persistence.DatabaseSchema.AnswerSql;

/**
 * @author john.wright
 * @since 21
 */
public class Answer {
    private final User user;
    private final Option answer;

    public Answer(final User user, final Option answer) {
        this.user = user;
        this.answer = answer;
    }

    public User getUser() {
        return this.user;
    }

    public Option getAnswer() {
        return this.answer;
    }

    public long persist(final long questionId, final SQLiteDatabase db) {
        final ContentValues values = new ContentValues(3);
        values.put(AnswerSql.COLUMN_NAME_QUESTION, questionId);
        values.put(AnswerSql.COLUMN_NAME_USER, this.user.getId());
        values.put(AnswerSql.COLUMN_NAME_ANSWER, this.answer.getId());
        return db.insert(AnswerSql.TABLE_NAME, null, values);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.user).append(" - ");
        sb.append(this.answer).append(" - ");

        return sb.toString();
    }
}
