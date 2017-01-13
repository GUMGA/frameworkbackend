package io.gumga.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária para facilitar reflexão.
 *
 * @author Equipe Gumga
 */
public class ReflectionUtils {

    private ReflectionUtils() {
    }

    /**
     * Procura um atributo pelo nome em uma classe ou em suas superclasses
     * recursivamente na ordem inversa da hierarquia. Inicialmente na classe,
     * depois na super classe e assim respectivamente.
     *
     * @param clazz classe que inicia a busca
     * @param name nome do atributo
     * @return Atributo encontrado, retorna null se não encontrado.
     */
    public static Field getField(Class clazz, String name) {
        try {
            Field toReturn = clazz.getDeclaredField(name);
            return toReturn;
        } catch (NoSuchFieldException | SecurityException ex) {

        }
        if (!Object.class.equals(clazz.getSuperclass())) {
            return getField(clazz.getSuperclass(), name);
        }

        return null;
    }

    /**
     * Procura um atributo pelo nome em uma classe ou em suas superclasses na
     * ordem inversa da hierarquia. Inicialmente na classe, depois na super
     * classe e assim respectivamente. * Procura y
     *
     * @param clazz classe a se procurada
     * @param name nome do atributo
     * @return atributo ou null se não encontrar
     */
    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    /**
     * Procura um atributo pelo nome e/ou tipo em uma classe ou em suas
     * superclasses na ordem inversa da hierarquia. Inicialmente na classe,
     * depois na super classe e assim respectivamente. * Procura y
     *
     * @param clazz classe a se procurada
     * @param name nome do atributo
     * @param type tipo do atriburo
     * @return atributo ou null se não encontrar
     */
    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class must not be null");
        }

        if (name == null && type == null) {
            throw new IllegalArgumentException("Either name or type of the field must be specified");
        }

        Class<?> searchType = clazz;

        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();

            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }

            searchType = searchType.getSuperclass();
        }

        return null;
    }

    /**
     * Infere o tipo genérico de uma classe, por exemplo, em uma lista,
     * deseja-se saber o tipo que foi utilizado na lista.
     *
     * @param clazz Classe
     * @return Tipo genérico
     */
    public static Class<?> inferGenericType(Class<?> clazz) {
        return inferGenericType(clazz, 0);
    }

    /**
     * Infere um dos tipos genérico de uma classe, por exemplo, em um mapa,
     * deseja-se saber um dos dois tipos que foi utilizado no mapa.
     *
     * @param clazz Classe
     * @param index posição do tipo
     * @return Tipo genérico
     */
    public static Class<?> inferGenericType(Class<?> clazz, int index) {
        Type superClass = clazz.getGenericSuperclass();
        return (Class<?>) ((ParameterizedType) superClass).getActualTypeArguments()[index];
    }

    /**
     * Atribui um valor a o atributo ID por reflexão
     *
     * @param object
     * @param id
     */
    public static void setId(Object object, Object id) {
        try {
            Field field = getField(object.getClass(), "id");
            field.setAccessible(true);
            field.set(object, id);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retorna um mapa com os valores dos atributos que estão em um objetos
     *
     * @param fields 
     * @param obj
     */
      public static Map<String, Object> objectFieldsToMap(String[] fields, Object obj) {
        Map<String, Object> row = new HashMap<>();
        for (String f : fields) {
            try {
                Field field = ReflectionUtils.getField(obj.getClass(), f.trim());
                field.setAccessible(true);
                row.put(f.trim(), field.get(obj));
            } catch (Exception ex) {
                Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return row;
    }

}
