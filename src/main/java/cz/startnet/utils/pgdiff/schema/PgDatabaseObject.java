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
     * List of privileges grouped by roles.
     */
    private Map<PgRole, List<PgPrivilege>> privileges = new HashMap<PgRole, List<PgPrivilege>>();

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
