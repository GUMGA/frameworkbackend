/*
    This file is part of Opes.

    Opes is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Opes is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Opes.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.gumga.domain.domains.usertypes;

import br.com.insula.opes.CpfCnpj;
import br.com.insula.opes.hibernate.usertype.ImmutableUserType;
import org.hibernate.engine.spi.SessionImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * UserType que permite serializar o tipo CPF/CNPJ dentro do Hibernate
 */
public class CpfCnpjUserType extends ImmutableUserType {

    private static final long serialVersionUID = 1L;

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        } else {
            return CpfCnpj.fromString(value);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            CpfCnpj document = (CpfCnpj) value;
            st.setString(index, document.toString());
        }
    }

    @Override
    public Class<?> returnedClass() {
        return CpfCnpj.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

}
