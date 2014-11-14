package jwstudios.curio.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import jwstudios.curio.persistence.DatabaseSchema.OptionMcpSql;
import jwstudios.curio.persistence.DatabaseSchema.OptionMctSql;

/**
 * @author john.wright
 * @since 21
 */
public interface Option {

    public long persist(long questionId, SQLiteDatabase db);

    public long getId();

    public void setId(long id);

    public abstract class AbstractOption
            implements Option {
        private long id;

        private AbstractOption() {
        }

        /**
         * persists an option for a question. do not close the db connection
         *
         * @return option row id
         */
        public abstract long persist(final long questionId, final SQLiteDatabase db);

        @Override
        public void setId(final long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return this.id;
        }

        public static class OptionMcp
                extends AbstractOption {
            private final String option;
            private final String picture;

            public OptionMcp(final String option, final String picture) {
                super();
                this.option = option;
                this.picture = picture;
            }

            @Override
            public long persist(final long questionId, final SQLiteDatabase db) {
                final ContentValues values = new ContentValues(2);
                values.put(OptionMcpSql.COLUMN_NAME_QUESTION, questionId);
                values.put(OptionMcpSql.COLUMN_NAME_OPTION, this.option);
                values.put(OptionMcpSql.COLUMN_NAME_PICTURE, this.picture);
                return db.insert(OptionMcpSql.TABLE_NAME, null, values);
            }

            @Override
            public String toString() {
                return "id: " + getId() + " option: " + this.option + " picture: " + this.picture;
            }
        }

        public static class OptionMct
                extends AbstractOption {
            private final String option;

            public OptionMct(final String option) {
                super();
                this.option = option;
            }

            @Override
            public long persist(final long questionId, final SQLiteDatabase db) {
                final ContentValues values = new ContentValues(2);
                values.put(OptionMctSql.COLUMN_NAME_QUESTION, questionId);
                values.put(OptionMctSql.COLUMN_NAME_OPTION, this.option);
                return db.insert(OptionMctSql.TABLE_NAME, null, values);
            }

            @Override
            public String toString() {
                return "id: " + getId() + " option: " + this.option;
            }
        }
    }
}
