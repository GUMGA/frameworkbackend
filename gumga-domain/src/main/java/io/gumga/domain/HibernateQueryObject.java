package io.gumga.domain;

import io.gumga.core.QueryObject;
import io.gumga.core.utils.ReflectionUtils;
import java.io.Serializable;
import java.lang.reflect.Field;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.UnsupportedDataTypeException;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permite utilizar um QueryObject do framework em com o Hibernate.
 */
public class HibernateQueryObject {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected final QueryObject queryObject;

    protected final Map<Class<?>, CriterionParser> parsers;

    public HibernateQueryObject(QueryObject queryObject) {

        this.queryObject = queryObject;

        this.parsers = new HashMap<>(GumgaQueryParserProvider.defaultMap);

        if (null == GumgaQueryParserProvider.defaultMap) {
            throw new HibernateQueryObjectException("GumgaQueryParserProvider.defaultMap must be set in Application configuration");
        }

        if (!queryObject.isPhonetic()) {
            //this.parsers.put(String.class, GumgaQueryParserProvider.STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
            //this.parsers.put(String.class, GumgaQueryParserProvider.STRING_CRITERION_PARSER);

        }

        //Não está selecionando registros salvos no banco com acento.
        this.queryObject.setQ(Normalizer.normalize(queryObject.getQ(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase());
        //this.queryObject.setQ(queryObject.getQ().toUpperCase());
    }

    public Criterion[] getCriterions(Class<?> clazz) {
        if (!queryObject.isValid()) {
            return new Criterion[0];
        }

        List<Criterion> criterions = new ArrayList<>();

        for (String field : queryObject.getSearchFields()) {
            try {
                Criterion criterion = createCriterion(field, queryObject.getQ(), clazz);

                criterions.add(criterion);
            } catch (ParseException ex) {
                throw new HibernateQueryObjectException("Problem creating creterion.Cannot parse field " + field);
            } catch (NumberFormatException ex) {
                throw new HibernateQueryObjectException("Problem creating creterion.Number format problem in field  " + field);
            } catch (UnsupportedDataTypeException ex) {
                throw new HibernateQueryObjectException("Problem creating creterion.Unsupported data type in field " + field);
            }
        }

        if (criterions.isEmpty()) {
            forceNoResults(criterions);
        }

        return criterions.toArray(new Criterion[criterions.size()]);
    }

    private Criterion createCriterion(String field, String value, Class<?> clazz) throws ParseException, NumberFormatException, UnsupportedDataTypeException {
        String[] chain = field.split("\\.");
        Class<?> type;
        Field javaField = null;

        if (chain.length > 1) {
            javaField = ReflectionUtils.findField(clazz, chain[0]);
            Class<?> superType = javaField.getType();
            type = ReflectionUtils.findField(superType, chain[1]).getType();
        } else {
            javaField = ReflectionUtils.findField(clazz, field);
            type = javaField.getType();
        }
        if (javaField.getType().equals(Serializable.class)) {
            Class c = clazz;
            while (!((c.getSuperclass().equals(GumgaModel.class) || c.getSuperclass().equals(GumgaModelUUID.class) || c.getSuperclass().equals(Object.class)))) {
                c = c.getSuperclass();
            }
            type = ReflectionUtils.inferGenericType(c, 0);
        }
        CriterionParser parser = parsers.get(type);
        if (type.isEnum()) {
            parser = parsers.get(Enum.class);
        }

        if (parser == null) {
            throw new UnsupportedDataTypeException(type.getName());
        }

        Field findField = ReflectionUtils.findField(clazz, field);
        String collumName = null;
        if(findField != null) {
            collumName = (findField.getType().isEnum() && findField.isAnnotationPresent(javax.persistence.Column.class)) ? findField.getAnnotation(javax.persistence.Column.class).name() : field;
        } else {
            collumName = field;
        }


        return parser.parse(collumName, value);
//        return parser.parse(field, value);
    }

    protected void forceNoResults(List<Criterion> criterions) {
        criterions.add(Restrictions.sqlRestriction("(1=0)"));
    }

}

class HibernateQueryObjectException extends RuntimeException {

    public HibernateQueryObjectException(String message) {
        super(message);
    }

}
