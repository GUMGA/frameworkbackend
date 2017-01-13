package io.gumga.domain;

import io.gumga.core.QueryObject;
import io.gumga.core.utils.ReflectionUtils;
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
            this.parsers.put(String.class, GumgaQueryParserProvider.STRING_CRITERION_PARSER_WITHOUT_TRANSLATE);
        }

        this.queryObject.setQ(Normalizer.normalize(queryObject.getQ(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase());
    }

    public Criterion[] getCriterions(Class<?> clazz) {
        if (!queryObject.isValid()) {
            return new Criterion[0];
        }

        List<Criterion> criterions = new ArrayList<>();

        for (String field : queryObject.getSearchFields()) {
            try {
                criterions.add(createCriterion(field, queryObject.getQ(), clazz));
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

        if (chain.length > 1) {
            Class<?> superType = ReflectionUtils.findField(clazz, chain[0]).getType();
            type = ReflectionUtils.findField(superType, chain[1]).getType();
        } else {
            type = ReflectionUtils.findField(clazz, field).getType();
        }

        CriterionParser parser = parsers.get(type);

        if (parser == null) {
            throw new UnsupportedDataTypeException(type.getName());
        }

        return parser.parse(field, value);
    }

    protected void forceNoResults(List<Criterion> criterions) {
        criterions.add(Restrictions.sqlRestriction("(1=0)"));
    }

}


class HibernateQueryObjectException extends RuntimeException{

    public HibernateQueryObjectException(String message) {
        super(message);
    }
    
    
}