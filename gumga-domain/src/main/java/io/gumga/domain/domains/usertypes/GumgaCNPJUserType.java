package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaCNPJ;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * UserType que permite serializar o tipo dentro do Hibernate
 */
public class GumgaCNPJUserType extends MutableUserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class returnedClass() {
        return GumgaCNPJ.class;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        if (o == null) {
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
        GumgaCNPJ object = null;
        final String valor = resultSet.getString(names[0]);
        if (!resultSet.wasNull()) {
            object = new GumgaCNPJ(valor);
        }
        return object;
    }

    @Override
    public void nullSafeSet(final PreparedStatement preparedStatement,
            final Object value, final int property,
            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(property, java.sql.Types.VARCHAR);
        } else {
            final GumgaCNPJ object = (GumgaCNPJ) value;
            preparedStatement.setString(property, object.getValue());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {

        if (value == null) {
            return null;
        }

        final GumgaCNPJ recebido = (GumgaCNPJ) value;
        final GumgaCNPJ aRetornar = new GumgaCNPJ(recebido);
        return aRetornar;
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

}
