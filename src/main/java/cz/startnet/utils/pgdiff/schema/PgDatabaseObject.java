/**
 * Copyright 2006 StartNet s.r.o.
 *
 * Distributed under MIT license
 */
package cz.startnet.utils.pgdiff.schema;

import cz.startnet.utils.pgdiff.privileges.PgPrivilege;
import cz.startnet.utils.pgdiff.roles.PgRole;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Database Object.
 * Database Object can have privileges associated with it.
 */
public class PgDatabaseObject {
    /**
     * List of known DB object types.
     */
    public enum DbObjectType {
        table,
        sequence,
        database,
        domain,
        foreign_data_wrapper,
        foreign_server,
        function,
        language,
        large_object,
        schema,
        tablespace,
        type,
        all_tables_in_schema,
        all_sequences_in_schema,
        all_functions_in_schema;

        public String toString() {
            return super.toString().replace('_', ' ').toUpperCase();
        }
    }

    private static final DbObjectType defaultDbObjectType = DbObjectType.table;

    /**
     * List of privileges grouped by roles.
     */
    private Map<PgRole, List<PgPrivilege>> privileges = new HashMap<PgRole, List<PgPrivilege>>();

    /**
     * Gets default DB object type.
     * @return {@link #defaultDbObjectType}
     */
    public static DbObjectType getDefaultDbObjectType() {
        return defaultDbObjectType;
    }

    /**
     * Gets unmodifiable list of {@link #priviligets} grouped by roles.
     * @return {@link #priviligets}
     */
    public Map<PgRole, List<PgPrivilege>> getPrivileges() {
        return Collections.unmodifiableMap(privileges);
    }

    public void addPrivilege(PgPrivilege privilege) {

    }

}
