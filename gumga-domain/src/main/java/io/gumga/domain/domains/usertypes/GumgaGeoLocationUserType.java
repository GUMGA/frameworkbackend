package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaGeoLocation;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.DoubleType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserType que permite serializar o tipo dentro do Hibernate
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
    public Object nullSafeGet(final ResultSet resultSet,
            final String[] names,
            final SessionImplementor paramSessionImplementor, final Object paramObject)
            throws HibernateException, SQLException {
        //owner here is of type TestUser or the actual owning Object
        GumgaGeoLocation object = null;
        final double latitude = resultSet.getDouble(names[0]);
        //Deferred check after first read
        if (!resultSet.wasNull()) {
            object = new GumgaGeoLocation(latitude, resultSet.getDouble(names[1]));
        }
        return object;
    }

    /**
     * Before executing the save call this method is called. It will set the
     * values in the prepared statement
     */
    @Override
    public void nullSafeSet(final PreparedStatement preparedStatement,
            final Object value, final int property,
            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
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

    /**
     * method called when Hibernate puts the data in a second level cache. The
     * data is stored in a serializable form
     */
    @Override
    public Serializable disassemble(final Object value,
            final SessionImplementor paramSessionImplementor)
            throws HibernateException {
        //Thus the data Types must implement serializable
        return (Serializable) value;
    }

    /**
     * Returns the object from the 2 level cache
     */
    @Override
    public Object assemble(final Serializable cached,
            final SessionImplementor sessionImplementor, final Object owner)
            throws HibernateException {
        //would work as the class is Serializable, and stored in cache as it is - see disassemble
        return cached;
    }

    /**
     * Method is called when merging two objects.
     */
    @Override
    public Object replace(final Object original, final Object target,
            final SessionImplementor paramSessionImplementor, final Object owner)
            throws HibernateException {
        //        return original; // if immutable use this
        //For mutable types at bare minimum return a deep copy of first argument
        return this.deepCopy(original);
    }

}
