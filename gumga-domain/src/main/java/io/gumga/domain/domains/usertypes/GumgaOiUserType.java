package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaOi;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * UserType que permite serializar o tipo dentro do Hibernate
 */
public class GumgaOiUserType extends ImmutableUserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class returnedClass() {
        return GumgaOi.class;
    }

    @Override
    public Object nullSafeGet(final ResultSet resultSet,
            final String[] names,
            final SessionImplementor paramSessionImplementor, final Object paramObject)
            throws HibernateException, SQLException {
        GumgaOi object = null;
        final String valor = resultSet.getString(names[0]);
        if (!resultSet.wasNull()) {
            object = new GumgaOi(valor);
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
            final GumgaOi object = new GumgaOi(value.toString());
            preparedStatement.setString(property, object.getValue());
        }
    }

}
