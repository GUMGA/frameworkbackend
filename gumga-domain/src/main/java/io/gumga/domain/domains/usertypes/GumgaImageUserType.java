package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaImage;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BinaryType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserType que permite serializar o tipo dentro do Hibernate
 */
public class GumgaImageUserType implements CompositeUserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{"name", "size", "mimeType", "bytes"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{StringType.INSTANCE, IntegerType.INSTANCE, StringType.INSTANCE, BinaryType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(final Object component, final int property) throws HibernateException {
        switch (property) {
            case 0:
                return ((GumgaImage) component).getName();
            case 1:
                return ((GumgaImage) component).getSize();
            case 2:
                return ((GumgaImage) component).getMimeType();
            case 3:
                return ((GumgaImage) component).getBytes();
        }
        return null;
    }

    @Override
    public void setPropertyValue(final Object component, final int property, final Object setValue) throws HibernateException {
        switch (property) {
            case 0:
                ((GumgaImage) component).setName((String) setValue);
            case 1:
                ((GumgaImage) component).setSize((long) setValue);
            case 2:
                ((GumgaImage) component).setMimeType((String) setValue);
            case 3:
                ((GumgaImage) component).setBytes((byte[]) setValue);
        }
    }

    @Override
    public Class returnedClass() {
        return GumgaImage.class;
    }

    @Override
    public boolean equals(final Object o1, final Object o2) throws HibernateException {
        boolean isEqual;
        if (o1 == o2) {
            isEqual = true;
        } else if (null == o1 || null == o2) {
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
        GumgaImage object = null;
        final String name = resultSet.getString(names[0]);
        //Deferred check after first read
        if (!resultSet.wasNull()) {
            object = new GumgaImage(name, resultSet.getLong(names[1]), resultSet.getString(names[2]), resultSet.getBytes(names[3]));
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
            preparedStatement.setNull(property + 0, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 1, java.sql.Types.INTEGER);
            preparedStatement.setNull(property + 2, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 3, java.sql.Types.VARBINARY);
        } else {
            final GumgaImage object = (GumgaImage) value;
            preparedStatement.setString(property + 0, object.getName());
            preparedStatement.setLong(property + 1, object.getSize());
            preparedStatement.setString(property + 2, object.getMimeType());
            preparedStatement.setBytes(property + 3, object.getBytes());
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

        final GumgaImage recebido = (GumgaImage) value;
        final GumgaImage aRetornar = new GumgaImage(recebido);
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

        return this.deepCopy(original);
    }

}
