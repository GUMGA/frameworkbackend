package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaMoney;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * UserType que permite serializar o tipo dentro do Hibernate, mapeia o tipo {@link GumgaMoney} para o banco de dados
 */
public class GumgaMoneyUserType extends MutableUserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.DOUBLE};
    }

    @Override
    public Class returnedClass() {
        return GumgaMoney.class;
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
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        GumgaMoney object = null;
        final BigDecimal valor = resultSet.getBigDecimal(names[0]);
        if (!resultSet.wasNull()) {
            object = new GumgaMoney(valor);
        }
        return object;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int property, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(property, java.sql.Types.VARCHAR);
        } else {
            final GumgaMoney object = (GumgaMoney) value;
            preparedStatement.setBigDecimal(property, object.getValue());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

        final GumgaMoney recebido = (GumgaMoney) value;
        final GumgaMoney aRetornar = new GumgaMoney(recebido.getValue());
        return aRetornar;
    }

}
