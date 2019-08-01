package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaGeoLocation;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserType que permite serializar o tipo dentro do Hibernate, mapeia o tipo {@link GumgaGeoLocation} para o banco de dados
 */
public class GumgaGeoLocationUserType implements CompositeUserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{"latitude", "longitude"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{DoubleType.INSTANCE, DoubleType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(final Object component, final int property) throws HibernateException {
        switch (property) {
            case 0:
                return ((GumgaGeoLocation) component).getLatitude();
            case 1:
                return ((GumgaGeoLocation) component).getLongitude();
        }
        return null;
    }

    @Override
    public void setPropertyValue(final Object component, final int property, final Object setValue) throws HibernateException {
        switch (property) {
            case 0:
                ((GumgaGeoLocation) component).setLatitude((Double) setValue);
            case 1:
                ((GumgaGeoLocation) component).setLongitude((Double) setValue);
        }
    }

    @Override
    public Class returnedClass() {
        return GumgaGeoLocation.class;
    }

    @Override
    public boolean equals(final Object o1, final Object o2) throws HibernateException {
        boolean isEqual = false;
        if (o1 == o2) {
            isEqual = false;
        }
        if (null == o1 || null == o2) {
            isEqual = false;
        } else {
            isEqual = o1.equals(o2);
        }
        return isEqual;
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        //owner here is of type TestUser or the actual owning Object
        GumgaGeoLocation object = null;
        final double latitude = resultSet.getDouble(names[0]);
        //Deferred check after first read
        if (!resultSet.wasNull()) {
            object = new GumgaGeoLocation(latitude, resultSet.getDouble(names[1]));
        }
        return object;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int property, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setDouble(property + 0, java.sql.Types.DOUBLE);
            preparedStatement.setDouble(property + 1, java.sql.Types.DOUBLE);
        } else {
            final GumgaGeoLocation object = (GumgaGeoLocation) value;
            preparedStatement.setDouble(property + 0, object.getLatitude());
            preparedStatement.setDouble(property + 1, object.getLongitude());
        }
    }



    /**
     * Used to create Snapshots of the object
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

//        return value; if object was immutable we could return the object as its is
        final GumgaGeoLocation recebido = (GumgaGeoLocation) value;
        final GumgaGeoLocation aRetornar = new GumgaGeoLocation(recebido);
        return aRetornar;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object o, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException {
        return (Serializable) o;
    }

    @Override
    public Object assemble(Serializable serializable, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return serializable;
    }

    @Override
    public Object replace(Object o, Object o1, SharedSessionContractImplementor sharedSessionContractImplementor, Object o2) throws HibernateException {
        return this.deepCopy(o);
    }


}
