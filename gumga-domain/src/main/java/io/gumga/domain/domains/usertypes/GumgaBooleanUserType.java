package io.gumga.domain.domains.usertypes;

import io.gumga.domain.GumgaQueryParserProvider;
import io.gumga.domain.domains.GumgaBoolean;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.usertype.EnhancedUserType;

import java.sql.*;

/**
 * UserType que permite serializar o tipo dentro do Hibernate, mapeia o tipo {@link GumgaBoolean} para o banco de dados
 */
public class GumgaBooleanUserType extends MutableUserType implements EnhancedUserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{
                BooleanType.INSTANCE.sqlType()
        };
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class returnedClass() {
        return GumgaBoolean.class;
    }

//    @Override
//    public boolean equals(Object obj) {
//        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        if (o == null || o1 == null) {
            return false;
        }
        return o.equals(o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        if (o == null) {
            return 0;
        }
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(final ResultSet resultSet,
            final String[] names,
            final SessionImplementor paramSessionImplementor, final Object paramObject)
            throws HibernateException, SQLException {
        GumgaBoolean object = null;
        final boolean valor = resultSet.getBoolean(names[0]);
        if (!resultSet.wasNull()) {
            object = new GumgaBoolean(valor);
        }
        return object;
    }

    @Override
    public void nullSafeSet(final PreparedStatement preparedStatement,
            final Object value, final int property,
            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(property, java.sql.Types.BOOLEAN);
        } else {
            Boolean result = value.toString().equals("true") ? Boolean.TRUE : Boolean.FALSE;
            BooleanType.INSTANCE.set(preparedStatement, result, property, sessionImplementor);
//            preparedStatement.setBoolean(property, value.toString().equals("true") ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        Boolean result = value.toString().equals("true") ? Boolean.TRUE : Boolean.FALSE;
        return new GumgaBoolean(result);
    }

    @Override
    public String objectToSQLString(Object value) {
        if(GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getOracleLikeMap()) || GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getOracleLikeMapWithAdjust())) {
            return value != null && value.toString().equals("true") ? "1" : "0";
        }
        return value != null && value.toString().equals("true") ? "true" : "false";
    }

    @Override
    public String toXMLString(Object value) {
        return null;
    }

    @Override
    public Object fromXMLString(String xmlValue) {
        return null;
    }

//    @Override
//    public int hashCode() {
//        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
//    }


}
