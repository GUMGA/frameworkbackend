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

import io.gumga.domain.domains.GumgaYoutubeURL;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * UserType que permite serializar o tipo de URL do Youtube, mapeia o tipo {@link GumgaYoutubeURL} para o banco de dados
 */
public class GumgaYoutubeURLUserType extends ImmutableUserType {

    private static final long serialVersionUID = 1L;

    @Override
    public Class<?> returnedClass() {
        return GumgaYoutubeURL.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        } else {
            return GumgaYoutubeURL.fromString(value);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            GumgaYoutubeURL url = (GumgaYoutubeURL) value;
            st.setString(index, url.toString());
        }
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

}
