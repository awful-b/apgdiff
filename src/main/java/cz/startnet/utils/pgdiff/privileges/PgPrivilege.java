/**
 * Copyright 2006 StartNet s.r.o.
 *
 * Distributed under MIT license
 */
package cz.startnet.utils.pgdiff.privileges;

import java.util.*;

/**
 * Base class for privileges.
 */
public class PgPrivilege {
    /**
     * PrivilegeType types.
     */
    public enum PrivilegeType {
        select,
        insert,
        update,
        delete,
        truncate,
        references,
        trigger,
        create,
        connect,
        temporary,
        execute,
        usage,
        all;

        public static final Map<PrivilegeType, List<String>> stringRepresentation;

        static {
            Map<PrivilegeType, List<String>> aMap = new HashMap<PrivilegeType, List<String>>();
            aMap.put(PrivilegeType.select, Arrays.asList("SELECT"));
            aMap.put(PrivilegeType.insert, Arrays.asList("INSERT"));
            aMap.put(PrivilegeType.update, Arrays.asList("UPDATE"));
            aMap.put(PrivilegeType.delete, Arrays.asList("DELETE"));
            aMap.put(PrivilegeType.truncate, Arrays.asList("TRUNCATE"));
            aMap.put(PrivilegeType.references, Arrays.asList("REFERENCES"));
            aMap.put(PrivilegeType.trigger, Arrays.asList("TRIGGER"));
            aMap.put(PrivilegeType.create, Arrays.asList("CREATE"));
            aMap.put(PrivilegeType.connect, Arrays.asList("CONNECT"));
            aMap.put(PrivilegeType.temporary, Arrays.asList("TEMPORARY", "TEMP"));
            aMap.put(PrivilegeType.execute, Arrays.asList("EXECUTE"));
            aMap.put(PrivilegeType.usage, Arrays.asList("USAGE"));
            aMap.put(PrivilegeType.all, Arrays.asList("ALL", "ALL PRIVILEGES"));

            stringRepresentation = Collections.unmodifiableMap(aMap);
        }
    }

    /**
     * PrivilegeType type.
     */
    private PrivilegeType privilegeType;

    /**
     * Name of database object the privilegeType is granted on.
     */
    private String databaseObjectName;

    /**
     * Getter for {@link #privilegeType}.
     *
     * @return {@link #privilegeType}
     */
    public PrivilegeType getPrivilegeType() {
        return privilegeType;
    }

    /**
     * Setter for {@link #privilegeType}.
     *
     * @param privilegeType {@link #privilegeType}
     */
    public void setPrivilegeType(PrivilegeType privilegeType) {
        this.privilegeType = privilegeType;
    }

    /**
     * Getter for {@link #databaseObjectName}.
     *
     * @return {@link #databaseObjectName}
     */
    public String getDatabaseObjectName() {
        return databaseObjectName;
    }

    /**
     * Setter for {@link #databaseObjectName}.
     *
     * @param databaseObjectName {@link #databaseObjectName}
     */
    public void setDatabaseObjectName(String databaseObjectName) {
        this.databaseObjectName = databaseObjectName;
    }
}
