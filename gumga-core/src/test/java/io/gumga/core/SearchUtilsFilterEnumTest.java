package io.gumga.core;

import org.junit.Before;
import org.junit.Test;

import static io.gumga.core.SearchUtilsFilterEnumTest.EnumLetras.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SearchUtilsFilterEnumTest {
	
	private QueryObject query = new QueryObject();
	
	@Before
	public void setup() {
		query.setQ("");
		query.setPageSize(2);
		query.setStart(0);
		query.setSortDir("asc");	
	}
	
	@Test
	public void filtroEmBranco() {
		assertResult(2, 0, 27L, AAA, AAB);
	}
	
	@Test
	public void filtroInformado() {
		query.setPageSize(50);
		
		query.setQ("A");		
		assertResult(50, 0, 9L, AAA, AAB, AAC, ABA, ABB, ABC, ACA, ACB, ACC);

		query.setQ("B");
		assertResult(50, 0, 9L, BAA, BAB, BAC, BBA, BBB, BBC, BCA, BCB, BCC);

		query.setQ("AA");		
		assertResult(50, 0, 3L, AAA, AAB, AAC);

		query.setQ("AB");		
		assertResult(50, 0, 3L, ABA, ABB, ABC);

		query.setQ("ABB");		
		assertResult(50, 0, 1L, ABB);
	}
	
	@Test
	public void paginacao() {
		query.setQ("B");
		query.setPageSize(4);
		
		assertResult(4, 0, 9L, BAA, BAB, BAC, BBA);

		query.setStart(2);
		assertResult(4, 2, 9L, BAC, BBA, BBB, BBC);
		
		query.setStart(4);
		assertResult(4, 4, 9L, BBB, BBC, BCA, BCB);
		
		query.setStart(8);
		assertResult(4, 8, 9L, BCC);
	}
	
	@Test
	public void paginacaoComResultadoSemPaginaCheia() {
		query.setQ("B");
		query.setPageSize(4);

		query.setStart(7);
		assertResult(4, 7, 9L, BCB, BCC);

		query.setStart(8);
		assertResult(4, 8, 9L, BCC);
	}
	
	@Test
	public void paginacaoComStartMaiorQueAQuantidadeDeFiltrados() {
		query.setQ("B");
		query.setPageSize(4);

		query.setStart(9);
		assertResult(4, 9, 9L);

		query.setStart(10);
		assertResult(4, 10, 9L);
	}
	
	@Test
	public void ordenacaoAsc() {
		query.setQ("C");
		query.setSortDir("asc");
		assertResult(2, 0, 9L, CAA, CAB);
	}
	
	@Test
	public void ordenacaoDesc() {
		query.setQ("C");
		query.setSortDir("desc");

		assertResult(2, 0, 9L, CCD, CCB);
	}
	
	@Test
	public void ordenacaoEPaginacaoComStart() {
		query.setQ("C");
		query.setStart(5);

		query.setSortDir("asc");
		assertResult(2, 5, 9L, CBC, CCA);

		query.setSortDir("desc");
		assertResult(2, 5, 9L, CBA, CAC);
	}
		
	private void assertResult(int pageSize, int start, Long count, EnumLetras... values) {
		SearchResult<EnumLetras> result = SearchUtils.filterEnum(EnumLetras.class, query);
		assertEquals("Result pageSize", pageSize, result.getPageSize());
		assertEquals("Result start", start, result.getStart());
		assertEquals("Result count", count, result.getCount());
		assertEquals("Result values", asList(values), result.getValues());
	}
	
	static enum EnumLetras {
		AAA,
		AAB,
		AAC,
		ABA,
		ABB,
		ABC,
		ACA,
		ACB,
		ACC,
		
		BAA,
		BAB,
		BAC,
		BBA,
		BBB,
		BBC,
		BCA,
		BCB,
		BCC,
		
		CBB,
		CCA,
		CBA,
		CAA,
		CAB,
		CAC,
		CCD,
		CCB,
		CBC,
	}
}
