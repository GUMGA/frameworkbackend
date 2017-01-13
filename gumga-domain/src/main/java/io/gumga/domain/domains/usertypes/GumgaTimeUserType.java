package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaTime;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import java.sql.*;
import java.util.Date;

/**
 * Permite armazenar um horário com hora, minuto e segundo em atributos
 * separados
 *
 * @author munif
 */
public class GumgaTimeUserType extends MutableUserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.TIME};
    }

    @Override
    public Class returnedClass() {
        return GumgaTime.class;
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
        GumgaTime object = null;
        final Date valor = resultSet.getTime(names[0]);
        if (!resultSet.wasNull()) {
            object = new GumgaTime(valor);
        }
        return object;
    }

    @Override
    public void nullSafeSet(final PreparedStatement preparedStatement,
            final Object value, final int property,
            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(property, java.sql.Types.TIME);
        } else {
            final GumgaTime object = (GumgaTime) value;
            preparedStatement.setTime(property, new Time(object.getValue().getTime()));
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

        final GumgaTime recebido = (GumgaTime) value;
        final GumgaTime aRetornar = new GumgaTime(recebido);
        return aRetornar;
    }

}
