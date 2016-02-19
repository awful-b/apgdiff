/**
 * Copyright 2006 StartNet s.r.o.
 *
 * Distributed under MIT license
 */
package cz.startnet.utils.pgdiff.privileges;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for privileges.
 */
public class PgPrivilege {
    /**
     * Privilege types.
     */
    public enum Privilege {
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

        public static final Map<Privilege, String> stringRepresentation;

        static {
            Map<Privilege, String> aMap = new HashMap<Privilege, String>();
            aMap.put(Privilege.select, "SELECT");
            aMap.put(Privilege.insert, "INSERT");
            aMap.put(Privilege.update, "UPDATE");
            aMap.put(Privilege.delete, "DELETE");
            aMap.put(Privilege.truncate, "TRUNCATE");
            aMap.put(Privilege.references, "REFERENCES");
            aMap.put(Privilege.trigger, "TRIGGER");
            aMap.put(Privilege.create, "CREATE");
            aMap.put(Privilege.connect, "CONNECT");
            aMap.put(Privilege.temporary, "TEMPORARY");
            aMap.put(Privilege.execute, "EXECUTE");
            aMap.put(Privilege.usage, "USAGE");
            aMap.put(Privilege.all, "ALL");

            stringRepresentation = Collections.unmodifiableMap(aMap);
        }
    }

    /**
     * Privilege type.
     */
    private Privilege privilege;

    /**
     * Getter for {@link #privilege}.
     *
     * @return {@link #privilege}
     */
    public Privilege getPrivilege() {
        return privilege;
    }

    /**
     * Setter for {@link #privilege}.
     *
     * @param privilege {@link #privilege}
     */
    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }
}
