/**
 * Copyright 2006 StartNet s.r.o.
 *
 * Distributed under MIT license
 */
package cz.startnet.utils.pgdiff.privileges;

/**
 * Represents a privilege on table
 */
public class PgTablePrivilege extends PgPrivilege {
    /**
     * Column name.
     */
    private String column;

    /**
     * Getter for {@link #column}
     * @return {@link #column}
     */
    public String getColumn() {
        return column;
    }

    /**
     * Setter for {@link #column}
     * @param {@link #column}
     */
    public void setColumn(String column) {
        this.column = column;
    }
}
