/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application.spring.config;

import io.gumga.core.GumgaValues;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe que implementa o NamingStrategy, responsável por gerir a estratégia de nomeação das entidades no Banco de dados
 *
 * @author felipe e munif
 * @see <a href="https://docs.jboss.org/hibernate/orm/3.2/api/org/hibernate/cfg/NamingStrategy.html">NamingStrategy</a>
 */
public class GumgaNamingStrategy implements NamingStrategy, Serializable {

    private static final Logger log = LoggerFactory.getLogger(GumgaNamingStrategy.class);

    public static final String[] RESERVED_WORDS = {
            //ORACLE
            "ACCESS", "ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AUDIT", "BETWEEN", "BY", "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT",
            "COMPRESS", "CONNECT", "CREATE", "CURRENT", "DATE", "DECIMAL", "DEFAULT", "DELETE", "DESC", "DISTINCT", "DROP", "ELSE", "EXCLUSIVE",
            "EXISTS", "FILE", "FLOAT", "FOR", "FROM", "GRANT", "GROUP", "HAVING", "IDENTIFIED", "IMMEDIATE", "IN", "INCREMENT", "INDEX", "INITIAL",
            "INSERT", "INTEGER", "INTERSECT", "INTO", "IS", "LEVEL", "LIKE", "LOCK", "LONG", "MAXEXTENTS", "MINUS", "MLSLABEL", "MODE", "MODIFY",
            "NOAUDIT", "NOCOMPRESS", "NOT", "NOWAIT", "NULL", "NUMBER", "OF", "OFFLINE", "ON", "ONLINE", "OPTION", "OR", "ORDER", "PCTFREE", "PRIOR",
            "PRIVILEGES", "PUBLIC", "RAW", "RENAME", "RESOURCE", "REVOKE", "ROW", "ROWID", "ROWNUM", "ROWS", "SELECT", "SESSION", "SET", "SHARE", "SIZE",
            "SMALLINT", "START", "SUCCESSFUL", "SYNONYM", "SYSDATE", "TABLE", "THEN", "TO", "TRIGGER", "UID", "UNION", "UNIQUE", "UPDATE", "USER",
            "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VIEW", "WHENEVER", "WHERE", "WITH",
            //MYSQL
            "KEY", "PASSWORD"

    };

    public static final int ORACLE_MAX_SIZE = 30;
    private final List<String> reservedWords;

    public GumgaNamingStrategy() {
        log.warn("-----------------GumgaNamingStrategy BETA -----------------------------");
        reservedWords = Arrays.asList(RESERVED_WORDS);
    }

    /**
     * Retorna um nome de tabela de acordo com o nome da classe
     *
     * @param string Nome da Classe
     * @return Nome da tabela para o Banco
     */
    @Override
    public String classToTableName(String string) {
        String table = StringHelper.unqualify(string);
        return oracleSafe(table).toUpperCase();
    }

    /**
     * Retorna um nome de tabela baseado numa coleção de nomes de classe, ex. uma associação que possua um join
     *
     * @param ownerEntity
     * @param ownerEntityTable
     * @param associatedEntity
     * @param associatedEntityTable
     * @param propertyName
     * @return String com o nome da tabela
     */
    @Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable,
                                      String propertyName) {
        return tableName(
                new StringBuilder(ownerEntityTable).append("_")
                        .append(
                                associatedEntityTable != null
                                        ? associatedEntityTable
                                        : StringHelper.unqualify(propertyName)
                        ).toString()
        );
    }

    /**
     * Altera o nome da coluna de acordo com o critério implementado, ex. retorna um nome compatível com Banco de Dados Oracle
     *
     * @param string Nome a ser alterado
     * @return Nome alterado
     */
    @Override
    public String columnName(String string) {
        return oracleSafe(string);
    }

    /**
     * Retorna a chave estrangeira de acordo com o nome da coluna passado por parâmetro
     * @param propertyName
     * @param propertyEntityName
     * @param propertyTableName
     * @param referencedColumnName
     * @return String chave_nomeDaColuna
     */
    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        String header = propertyName != null ? StringHelper.unqualify(propertyName) : propertyTableName;
        Objects.requireNonNull(header, "GumgaNamingStrategy not properly filled on foreignKeyColumnName");
        return columnName(header + "_" + referencedColumnName);
    }

    /**
     * Retorna a chave de junção a partir do nome de uma coluna, ex: chave estrangeira usada em uma associação Join ou para uma tabela secundária
     * @param joinedColumn Coluna unida
     * @param joinedTable Tabela unida
     * @return Nome da coluna
     */
    @Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return columnName(joinedColumn);
    }

    /**
     * Retorna o nome lógico da chave estrangeira usada para referenciar a coluna nos metadados mapeados
     * @param columnName Nome da coluna
     * @param propertyName Nome da propriedade
     * @param referencedColumn Coluna referenciada
     * @return Nome lógico da coluna
     */
    @Override
    public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
        return oracleSafe(StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName) + "_" + referencedColumn);
    }

    /**
     * Retorna o nome lógico de uma coleção de tabelas usadas para referenciar uma tabela nos metadados mapeados
     * @param tableName Nome da tabela
     * @param ownerEntityTable Dono da entidade
     * @param associatedEntityTable Tabela associada
     * @param propertyName nome da propriedade
     * @return Nome lógico
     */
    @Override
    public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
        if (tableName != null) {
            return oracleSafe(tableName);
        } else {
            //use of a stringbuffer to workaround a JDK bug
            return oracleSafe(new StringBuffer(ownerEntityTable).append("_").append(associatedEntityTable != null ? associatedEntityTable : StringHelper.unqualify(propertyName)).toString());
        }
    }

    /**
     *  Retorna o nome lógico da coluna usada para referenciar a coluna nos metadados
     * @param columnName
     * @param propertyName
     * @return
     */
    @Override
    public String logicalColumnName(String columnName, String propertyName) {
        return oracleSafe(StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName));
    }

    /**
     * Retorna um nome de coluna a partir de uma expressão de propriedade
     * @param propertyName Nome da propriedade
     * @return Nome da tabela
     */
    @Override
    public String propertyToColumnName(String propertyName) {
        return oracleSafe(StringHelper.unqualify(propertyName));
    }

    /**
     * Altera o nome da tabela dado no documento de mapeamento
     * @param string Nome da tabela
     * @return Nome da tabela
     */
    @Override
    public String tableName(String string) {
        return oracleSafe(string);
    }

    private String oracleSafe(String name) {
        boolean loga = false;
        String originalName = name;

        if (name.length() >= ORACLE_MAX_SIZE) {
            loga = true;
            String initials = "";
            int lastPos = 0;
            int i = 0;
            for (char c : name.toCharArray()) {
                i++;
                if (Character.isUpperCase(c) || i == 1) {
                    initials += c;
                    lastPos = i;
                }

            }
            initials += name.substring(lastPos);
            name = initials;
        }
        name = name.toUpperCase();
        if (reservedWords.contains(name)) {
            loga = true;
            name = "G_" + name;
        }
        if (name.length() >= ORACLE_MAX_SIZE) {
            loga = true;
        }
        name = name.substring(0, name.length() >= ORACLE_MAX_SIZE ? ORACLE_MAX_SIZE : name.length());

        if (loga) {
            log.warn("--- GumgaNamingStrategy -->" + originalName + "=>" + name);
        }

        return name;
    }

}
