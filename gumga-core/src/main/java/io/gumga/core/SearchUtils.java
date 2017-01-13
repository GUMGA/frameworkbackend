package io.gumga.core;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.reverse;
import static java.util.Collections.sort;

/**
 * Contém métodos estáticos para manipulação de pesquisas
 *
 * @author Equipe Gumga
 */
public class SearchUtils {

    private SearchUtils() {
    }

    /**
     * Aplica um filtro nos valores de um Enum e retorna em um SearchResult
     *
     * @param <E> Tipo do enum
     * @param enumClass Tipo do enum
     * @param query Pesquisa (será considerado apenas pesquisa simples iniciando
     * com esta query)
     * @return Resultado da aplicação do filtro
     */
    public static <E extends Enum<E>> SearchResult<E> filterEnum(Class<E> enumClass, QueryObject query) {
        List<E> filtered = new ArrayList<>();

        for (E value : enumClass.getEnumConstants()) {
            if (StringUtils.startsWithIgnoreCase(value.name(), query.getQ())) {
                filtered.add(value);
            }
        }

        sortEnum(query.getSortDir(), filtered);
        List<E> paginated = paginate(query.getStart(), query.getPageSize(), filtered);
        return new SearchResult<>(query, filtered.size(), paginated);
    }

    private static <T> List<T> paginate(int start, int pageSize, List<T> filtered) {
        int paginationStart = Math.min(start, filtered.size());
        int paginationEnd = Math.min(start + pageSize, filtered.size());
        return filtered.subList(paginationStart, paginationEnd);
    }

    private static <E extends Enum<E>> void sortEnum(String direction, List<E> filtered) {
        if ("asc".equalsIgnoreCase(direction)) {
            sort(filtered, new EnumNameComparator<>());
        } else if ("desc".equalsIgnoreCase(direction)) {
            sort(filtered, new EnumNameComparator<>());
            reverse(filtered);
        }
    }

    private static class EnumNameComparator<E extends Enum<E>> implements Comparator<E> {

        @Override
        public int compare(E o1, E o2) {
            return o1.name().compareTo(o2.name());
        }
    }
}
