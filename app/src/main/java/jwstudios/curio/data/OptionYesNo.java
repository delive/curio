package jwstudios.curio.data;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author john.wright
 * @since 21
 */
public enum OptionYesNo
        implements Option {
    YES(1) {
        @Override
        public long persist(final long questionId, final SQLiteDatabase db) {
            throw new UnsupportedOperationException("cannot persist yes/no option");
        }

        @Override
        public long getId() {
            return this.id;
        }

        @Override
        public void setId(final long id) {
            throw new UnsupportedOperationException("cannot setid for yes/no option");
        }
    },
    NO(0) {
        @Override
        public long persist(final long questionId, final SQLiteDatabase db) {
            throw new UnsupportedOperationException("cannot persist yes/no option");
        }

        @Override
        public long getId() {
            return this.id;
        }

        @Override
        public void setId(final long id) {
            throw new UnsupportedOperationException("cannot setid for yes/no option");
        }
    };
    final int id;

    OptionYesNo(final int id) {
        this.id = id;
    }

    public static OptionYesNo fromId(final int id) {
        return id == 0 ? NO : YES;
    }
}
