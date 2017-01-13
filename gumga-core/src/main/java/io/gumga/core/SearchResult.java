package io.gumga.core;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Representa um resultado de uma pesquisa
 *
 * @author Equipe Gumga
 * @param <T> Tipo de cada elemento resultante da pesquisa
 */
public class SearchResult<T> {

    private final int pageSize;
    private final Long count;
    private final int start;
    private final List<T> values;

    public SearchResult(int start, int pageSize, Number count, List<T> data) {
        this.start = start;
        this.pageSize = pageSize;
        this.count = count.longValue();
        this.values = data;
    }

    public SearchResult(QueryObject query, Number count, List<T> data) {
        this(query.getStart(), query.getPageSize(), count, data);
    }

    public int getPageSize() {
        return pageSize;
    }

    public Long getCount() {
        return count;
    }

    public int getStart() {
        return start;
    }

    /**
     * Transforma o resultado da pesquisa em outro tipo de objeto
     *
     * @param fn A funcao a ser transformada
     * @param <A> O tipo da funcao
     * @return A funcao como SearchResult
     */
    public <A> SearchResult<A> map(Function<? super T, A> fn) {
        return new SearchResult<>(start, pageSize, count, values.stream().map(fn).collect(Collectors.toList()));
    }

    public List<T> getValues() {
        return values;
    }

}
