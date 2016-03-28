/**
 * Copyright 2006 StartNet s.r.o.
 *
 * Distributed under MIT license
 */
package cz.startnet.utils.pgdiff.parsers;

import cz.startnet.utils.pgdiff.Resources;
import cz.startnet.utils.pgdiff.privileges.PgPrivilege;
import cz.startnet.utils.pgdiff.privileges.PgTablePrivilege;
import cz.startnet.utils.pgdiff.schema.PgDatabase;
import cz.startnet.utils.pgdiff.schema.PgDatabaseObject;
import cz.startnet.utils.pgdiff.schema.PgFunction;
import cz.startnet.utils.pgdiff.schema.PgSchema;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses GRANT statements.
 */
public class GrantParser {
    /**
     * Parses GRANT statement.
     *
     * @param database  database
     * @param statement GRANT statement
     */
    public static void parse(final PgDatabase database,
                             final String statement) {
        List<PgPrivilege> privileges = parseImpl(statement);
        //PgSchema schema = getSchemaByDatabaseObjectName(database, privilege.getDatabaseObjectName(), statement);
        //schema.addPrivilege(privilege);
    }

    private static List<PgPrivilege> parseImpl(final String statement) {
        final Parser parser = new Parser(statement);
        parser.expect("GRANT");

        // parse privilege types { { SELECT | UPDATE ... } [, ...] | ALL [ PRIVILEGES ] }
        final Map<PgPrivilege.PrivilegeType, List<String>> privilegeTypes = parsePrivilegeTypes(parser);
        parser.expect("ON");

        // parse object type { TABLE | SEQUENCE | FUNCTION ... }
        final PgDatabaseObject.DbObjectType dbObjectType = parseDbObjectType(parser);

        // parse DB object identifiers (table's, sequence's, function's, ... names)
        Map<String, List<PgFunction.Argument>> dbObjectIdentifiers = parseDbObjectIdentifiers(parser, dbObjectType);
        parser.expect("TO");

        // parse roles
        List<String> roles = parseRoles(parser);

        // parse With grant option
        boolean withGrantOption = parser.expectOptional("WITH GRANT OPTION");

        // creating privilege objects
        return createPrivileges(dbObjectType,
                privilegeTypes, dbObjectIdentifiers, roles, withGrantOption);
    }

    private static PgDatabaseObject.DbObjectType parseDbObjectType(Parser parser) {
        for (PgDatabaseObject.DbObjectType dbObjectType : PgDatabaseObject.DbObjectType.values()) {
            if (parser.expectOptional(dbObjectType.toString())) {
                return dbObjectType;
            }
        }

        return PgDatabaseObject.getDefaultDbObjectType();
    }

    private static PgSchema getSchemaByDatabaseObjectName(final PgDatabase database,
                                                          final String databaseObjectName,
                                                          final String statement) {
        final String schemaName = ParserUtils.getSchemaName(databaseObjectName, database);
        final PgSchema schema = database.getSchema(schemaName);

        if (schema == null) {
            throw new RuntimeException(MessageFormat.format(
                    Resources.getString("CannotFindSchema"), schemaName,
                    statement));
        }

        return schema;
    }

    private static PgPrivilege.PrivilegeType privilegeTypeFromString(Parser parser) {
        for (Map.Entry<PgPrivilege.PrivilegeType, List<String>> entry :
                PgPrivilege.PrivilegeType.stringRepresentation.entrySet()) {
            for (final String privilegeString : entry.getValue()) {
                if (parser.expectOptional(privilegeString))
                    return entry.getKey();
            }
        }

        throw new RuntimeException(
                MessageFormat.format(
                        Resources.getString("CannotRecognizePrivilegeTypeInGrantStatement"),
                        parser.getString()));
    }

    private static Map<PgPrivilege.PrivilegeType, List<String>> parsePrivilegeTypes(Parser parser) {
        Map<PgPrivilege.PrivilegeType, List<String>> privilegeTypes =
                parsePrivilegeTypesImpl(parser, new HashMap<PgPrivilege.PrivilegeType, List<String>>());

        if (privilegeTypes.size() == 1 && privilegeTypes.keySet().iterator().next() == PgPrivilege.PrivilegeType.all) {
            // special case for ALL, it can be followed to by PRIVILEGES
            parser.expectOptional("PRIVILEGES");
        }

        return privilegeTypes;
    }

    private static Map<PgPrivilege.PrivilegeType, List<String>> parsePrivilegeTypesImpl(Parser parser,
                                                                                        Map<PgPrivilege.PrivilegeType, List<String>> privileges) {
        PgPrivilege.PrivilegeType privilegeType = privilegeTypeFromString(parser);
        privileges.put(privilegeType, new ArrayList<String>());

        if (parser.expectOptional("(")) {
            // parse column list
            privileges.get(privilegeType).addAll(parser.parseCommaSeparatedList());
            parser.expect(")");
        }

        if (parser.expectOptional(","))
            parsePrivilegeTypesImpl(parser, privileges);

        return privileges;
    }

    private static List<String> parseRoles(final Parser parser) {
        List<String> roles = new ArrayList<String>();

        roles.add(parseRole(parser));
        while (parser.expectOptional(","))
            roles.add(parseRole(parser));

        return roles;
    }

    private static String parseRole(final Parser parser) {
        parser.expectOptional("GROUP");
        return parser.parseIdentifier();
    }

    private static Map<String, List<PgFunction.Argument>>
    parseDbObjectIdentifiers(final Parser parser,
                             final PgDatabaseObject.DbObjectType dbObjectType) {
         return parseDbObjectIdentifiersImpl(parser, dbObjectType, new HashMap<String, List<PgFunction.Argument>>());
    }

    private static Map<String, List<PgFunction.Argument>>
    parseDbObjectIdentifiersImpl(final Parser parser,
                                 final PgDatabaseObject.DbObjectType dbObjectType,
                                 Map<String, List<PgFunction.Argument>> identifiers) {
        String identifier = parser.parseIdentifier();
        identifiers.put(identifier, new ArrayList<PgFunction.Argument>());

        List<PgFunction.Argument> functionArguments = null;
        if (dbObjectType == PgDatabaseObject.DbObjectType.function)
            identifiers.get(identifier).addAll(CreateFunctionParser.parseArguments(parser));

        if (parser.expectOptional(","))
            parseDbObjectIdentifiersImpl(parser, dbObjectType, identifiers);

        return identifiers;
    }

    private static List<PgPrivilege> createPrivileges(final PgDatabaseObject.DbObjectType dbObjectType,
                                                      final Map<PgPrivilege.PrivilegeType, List<String>> privilegeTypes,
                                                      final Map<String, List<PgFunction.Argument>> dbOjectIdentifiers,
                                                      final List<String> roles,
                                                      final boolean withGrantOption) {
        List<PgPrivilege> privileges = new ArrayList<PgPrivilege>();

        for (Map.Entry<PgPrivilege.PrivilegeType, List<String>> privilegeType : privilegeTypes.entrySet()) {
            for (Map.Entry<String, List<PgFunction.Argument>> objectIdentifier : dbOjectIdentifiers.entrySet()) {
                for (String role : roles) {
                    privileges.addAll(createPrivileges(
                            dbObjectType,
                            privilegeType.getKey(),
                            privilegeType.getValue(),
                            objectIdentifier.getKey(),
                            objectIdentifier.getValue(),
                            role,
                            withGrantOption));
                }
            }
        }

        return privileges;
    }

    private static List<PgPrivilege> createPrivileges(final PgDatabaseObject.DbObjectType dbObjectType,
                                                      final PgPrivilege.PrivilegeType privilegeType,
                                                      final List<String> columns,
                                                      final String objectIdentifier,
                                                      final List<PgFunction.Argument> functionArguments,
                                                      final String role,
                                                      final boolean withGrantOption) {
        List<PgPrivilege> privileges = new ArrayList<PgPrivilege>();

        switch (dbObjectType) {
            case table:
                if (columns.isEmpty()) {
                    privileges.add(new PgTablePrivilege());
                } else {
                    for (String column : columns) {
                        PgTablePrivilege privilege = new PgTablePrivilege();
                        privilege.setColumn(column);
                        privileges.add(privilege);
                    }
                }

                for (PgPrivilege privilege : privileges) {
                    privilege.setDatabaseObjectName(ParserUtils.getObjectName(objectIdentifier));
                }
                break;

            case sequence:

                break;

            case database:

                break;

            case domain:

                break;

            case foreign_data_wrapper:

                break;

            case foreign_server:

                break;

            case function:

                break;

            case language:

                break;

            case large_object:

                break;

            case schema:

                break;

            case tablespace:

                break;

            case type:

                break;

            case all_tables_in_schema:

                break;

            case all_sequences_in_schema:

                break;

            case all_functions_in_schema:

                break;
        }

        return privileges;
    }

    /**
     * Creates a new instance of GrantParser.
     */
    private GrantParser() {
    }
}
