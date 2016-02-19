package cz.startnet.utils.pgdiff.roles;

/**
 * Represents user role.
 */
public class PgRole {
    /**
     * Role name.
     */
    private String name;

    /**
     * Getter for {@link #name}.
     *
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for {@link #name}.
     *
     * @param privilege {@link #name}
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PgRole pgRole = (PgRole) o;

        return name != null ? name.equals(pgRole.name) : pgRole.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
