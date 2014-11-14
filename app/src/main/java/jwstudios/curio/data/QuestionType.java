package jwstudios.curio.data;

/**
 * @author john.wright
 * @since 21
 */
public enum QuestionType {
    MULTI_TEXT(0, "Multi-choice Text"),
    MULTI_PIC(1, "Multi-choice Picture"),
    YES_NO(2, "Yes-no");

    private final long id;
    private final String name;

    QuestionType(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static QuestionType fromId(final int id) {
        for (final QuestionType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("invalid type id");
    }

    @Override
    public String toString() {
        return "id: " + this.id + " name: " + this.name;
    }
}
