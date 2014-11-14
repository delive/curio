package jwstudios.curio.data;

/**
 * @author john.wright
 * @since 21
 */
public enum QuestionStatus {
    DRAFT(0, "Draft"),
    IN_PROGRESS(1, "In Progress"),
    CANCELED(2, "Canceled"),
    COMPLETED(3, "Completed");

    private final long id;
    private final String name;

    QuestionStatus(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "id: " + this.id + " name: " + this.name;
    }

    public static QuestionStatus fromId(final int id) {
        for (final QuestionStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid type id");
    }
}
