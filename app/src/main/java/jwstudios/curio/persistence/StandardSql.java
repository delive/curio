package jwstudios.curio.persistence;

import jwstudios.curio.persistence.DatabaseSchema.OptionMcpSql;
import jwstudios.curio.persistence.DatabaseSchema.OptionMctSql;

/**
 * @author john.wright
 * @since 21
 */
public class StandardSql {
    public static String OPTIONS_MCT_FOR_QUESTION = "select _id, option from " + OptionMctSql.TABLE_NAME + " where question = ?";
    public static String OPTIONS_MCP_FOR_QUESTION = "select _id, option, picture from " + OptionMcpSql.TABLE_NAME + " where question = ?";
}
