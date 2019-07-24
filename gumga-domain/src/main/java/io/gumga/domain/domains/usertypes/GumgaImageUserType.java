package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaImage;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
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
 * UserType que permite serializar o tipo dentro do Hibernate, mapeia o tipo {@link GumgaImage} para o banco de dados
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
    public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        //owner here is of type TestUser or the actual owning Object
        GumgaImage object = null;
        final String name = resultSet.getString(names[0]);
        //Deferred check after first read
        if (!resultSet.wasNull()) {
            object = new GumgaImage(name, resultSet.getLong(names[1]), resultSet.getString(names[2]), resultSet.getBytes(names[3]));
        }
        return object;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int property, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
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
