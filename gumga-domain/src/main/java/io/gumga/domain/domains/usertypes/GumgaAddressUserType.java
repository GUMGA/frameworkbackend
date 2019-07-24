package io.gumga.domain.domains.usertypes;

import io.gumga.domain.domains.GumgaAddress;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.type.DoubleType;

/**
 * UserType que permite serializar o tipo dentro do Hibernate, mapeia o tipo {@link GumgaAddress} para o banco de dados
 */
public class GumgaAddressUserType implements CompositeUserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{
            "zipCode",
            "premisseType",
            "premisse",
            "number",
            "information",
            "neighbourhood",
            "localization",
            "state",
            "country",
            "latitude",
            "longitude",
            "formalCode",
            "stateCode"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{
            StringType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE,
            DoubleType.INSTANCE,
            DoubleType.INSTANCE,
            StringType.INSTANCE,
            StringType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(final Object component, final int property) throws HibernateException {
        switch (property) {
            case 0:
                return ((GumgaAddress) component).getZipCode();
            case 1:
                return ((GumgaAddress) component).getPremisseType();
            case 2:
                return ((GumgaAddress) component).getPremisse();
            case 3:
                return ((GumgaAddress) component).getNumber();
            case 4:
                return ((GumgaAddress) component).getInformation();
            case 5:
                return ((GumgaAddress) component).getNeighbourhood();
            case 6:
                return ((GumgaAddress) component).getLocalization();
            case 7:
                return ((GumgaAddress) component).getState();
            case 8:
                return ((GumgaAddress) component).getCountry();
            case 9:
                return ((GumgaAddress) component).getLatitude();
            case 10:
                return ((GumgaAddress) component).getLongitude();
            case 11:
                return ((GumgaAddress) component).getFormalCode();
            case 12:
                return ((GumgaAddress) component).getStateCode();
            default:
                return null;

        }
    }

    @Override
    public void setPropertyValue(final Object component, final int property, final Object setValue) throws HibernateException {
        switch (property) {
            case 0:
                ((GumgaAddress) component).setZipCode((String) setValue);
                break;
            case 1:
                ((GumgaAddress) component).setPremisseType((String) setValue);
                break;
            case 2:
                ((GumgaAddress) component).setPremisse((String) setValue);
                break;
            case 3:
                ((GumgaAddress) component).setNumber((String) setValue);
                break;
            case 4:
                ((GumgaAddress) component).setInformation((String) setValue);
                break;
            case 5:
                ((GumgaAddress) component).setNeighbourhood((String) setValue);
                break;
            case 6:
                ((GumgaAddress) component).setLocalization((String) setValue);
                break;
            case 7:
                ((GumgaAddress) component).setState((String) setValue);
                break;
            case 8:
                ((GumgaAddress) component).setCountry((String) setValue);
                break;
            case 9:
                ((GumgaAddress) component).setLatitude((Double) setValue);
                break;
            case 10:
                ((GumgaAddress) component).setLongitude((Double) setValue);
                break;
            case 11:
                ((GumgaAddress) component).setFormalCode((String) setValue);
                break;
            case 12:
                ((GumgaAddress) component).setStateCode((String) setValue);
                break;
        }
    }

    @Override
    public Class returnedClass() {
        return GumgaAddress.class;
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
        GumgaAddress object = null;
        final String zipCode = resultSet.getString(names[0]);
        //Deferred check after first read
        for (int i = 0; i < names.length; i++) {
            if (resultSet.getObject(names[i]) != null) {
                return new GumgaAddress(zipCode, resultSet.getString(names[1]), resultSet.getString(names[2]), resultSet.getString(names[3]), resultSet.getString(names[4]),
                        resultSet.getString(names[5]), resultSet.getString(names[6]), resultSet.getString(names[7]), resultSet.getString(names[8]),
                        resultSet.getDouble(names[9]), resultSet.getDouble(names[10]), resultSet.getString(names[11]), resultSet.getString(names[12]));
            }
        }
        return new GumgaAddress();
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int property, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(property + 0, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 1, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 2, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 3, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 4, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 5, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 6, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 7, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 8, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 9, java.sql.Types.DOUBLE);
            preparedStatement.setNull(property + 10, java.sql.Types.DOUBLE);
            preparedStatement.setNull(property + 11, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 12, java.sql.Types.VARCHAR);
        } else {
            final GumgaAddress object = (GumgaAddress) value;
            preparedStatement.setString(property + 0, object.getZipCode());
            preparedStatement.setString(property + 1, object.getPremisseType());
            preparedStatement.setString(property + 2, object.getPremisse());
            preparedStatement.setString(property + 3, object.getNumber());
            preparedStatement.setString(property + 4, object.getInformation());
            preparedStatement.setString(property + 5, object.getNeighbourhood());
            preparedStatement.setString(property + 6, object.getLocalization());
            preparedStatement.setString(property + 7, object.getState());
            preparedStatement.setString(property + 8, object.getCountry());

            if (object.getLatitude() != null) {
                preparedStatement.setDouble(property + 9, object.getLatitude());
            } else {
                preparedStatement.setNull(property + 9, java.sql.Types.DOUBLE);
            }

            if (object.getLongitude() != null) {
                preparedStatement.setDouble(property + 10, object.getLongitude());
            } else {
                preparedStatement.setNull(property + 10, java.sql.Types.DOUBLE);
            }

            preparedStatement.setString(property + 11, object.getFormalCode());
            preparedStatement.setString(property + 12, object.getStateCode());

        }
    }



    /**
     * Used to create Snapshots of the object
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
//        return value; if object was immutable we could return the object as its is
        if (value == null) {
            return null;
        }

        final GumgaAddress recebido = (GumgaAddress) value;
        final GumgaAddress aRetornar = new GumgaAddress(recebido);
        return aRetornar;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException {
        //Thus the data Types must implement serializable
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object o1, SharedSessionContractImplementor sharedSessionContractImplementor, Object o2) throws HibernateException {
        return this.deepCopy(original);
    }

}
