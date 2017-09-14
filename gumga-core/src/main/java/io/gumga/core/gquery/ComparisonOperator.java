package io.gumga.core.gquery;

/**
 *
 * @author munif
 */
public enum ComparisonOperator {

    EQUAL("="), GREATER(">"), LOWER("<"), GREATER_EQUAL(">="), LOWER_EQUAL("<="), LIKE(" like "), NOT_EQUAL("!="), STARTS_WITH(" like "), ENDS_WITH(" like "), CONTAINS(" like ");
    public final String hql;

    ComparisonOperator(String s) {
        this.hql = s;
    }

}

/*

Expressions used in the where clause include the following:

    mathematical operators: +, -, *, /

    binary comparison operators: =, >=, <=, <>, !=, like

    operadores lógicos and, or, not

    Parentheses ( ) that indicates grouping

    in, not in, between, is null, is not null, is empty, is not empty, member of and not member of

    case "simples" , case ... when ... then ... else ... end, and "searched" case, case when ... then ... else ... end

    concatenação de string ...||... ou concat(...,...)

    current_date(), current_time(), and current_timestamp()

    second(...), minute(...), hour(...), day(...), month(...), and year(...)

    qualquer funcao ou operador definida pela EJB-QL 3.0: substring(), trim(), lower(), upper(), length(), locate(), abs(), sqrt(), bit_length(), mod()

    coalesce() and nullif()

    str() para converter valores numericos ou temporais para string

    cast(... as ...), onde o segundo argumento é o nome do tipo hibernate, eextract(... from ...) se ANSI cast() e extract() é suportado pele banco de dados usado

    A função HQL index() , que se aplicam a referencias de coleçôes associadas e indexadas

    HQL functions that take collection-valued path expressions: size(), minelement(), maxelement(), minindex(), maxindex(), along with the special elements() and indices functions that can be quantified using some, all, exists, any, in.

    Any database-supported SQL scalar function like sign(), trunc(), rtrim(), and sin()

    Parametros posicionais ao estilo JDBC ?

    named parameters :name, :start_date, and :x1

    Literais SQL 'foo', 69, 6.66E+2, '1970-01-01 10:00:01.0'

    Constantes Java public static finalex: Color.TABBY 


 */
