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

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import java.io.Serializable;

public abstract class ImmutableUserType implements UserType, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public final Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public final Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public final Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public final boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public final int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public final boolean isMutable() {
        return false;
    }

    @Override
    public final Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

}
