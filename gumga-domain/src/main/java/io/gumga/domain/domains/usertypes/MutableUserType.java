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

/**
 * Classe abstrata que implementa UserType, visando a mutabilidade dos tipos do hibernate criados
 */
public abstract class MutableUserType implements UserType, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

}
