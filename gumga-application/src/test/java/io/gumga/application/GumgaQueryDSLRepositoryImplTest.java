package io.gumga.application;

import com.mysema.query.types.expr.BooleanExpression;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.SearchResult;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { SpringConfig.class })
public class GumgaQueryDSLRepositoryImplTest {

    @Autowired
    private CarRepository carRepository;

    private static boolean started = false;

    @Before
    public void setUp() {

        if (!started) {
            GumgaThreadScope.organizationCode.set("1");
            carRepository.save(new Car("vermelho"));
            carRepository.save(new Car("azul"));
            carRepository.save(new Car("branco"));
            carRepository.save(new Car("verde"));
            carRepository.save(new Car("marrom"));
            carRepository.save(new Car("ciano"));
            carRepository.save(new Car("amarelo"));
            carRepository.save(new Car("bege"));

            GumgaThreadScope.organizationCode.set("2");
            carRepository.save(new Car("vermelho"));
            carRepository.save(new Car("azul"));
            carRepository.save(new Car("branco"));

            started = true;
        }
    }

    //@Test
    public void should_get_one() {

        GumgaThreadScope.organizationCode.set("1");
        assertNotNull(carRepository.findOne(QCar.car.color.eq("bege")));

        GumgaThreadScope.organizationCode.set("2");
        assertNull(carRepository.findOne(QCar.car.color.eq("bege")));
    }

    //@Test
    public void should_find_all() {

        GumgaThreadScope.organizationCode.set("1");
        assertEquals(8, carRepository.findAll().size());

        GumgaThreadScope.organizationCode.set("2");
        assertEquals(3, carRepository.findAll().size());
    }

    //@Test
    public void should_find_all_with_predicate() {

        GumgaThreadScope.organizationCode.set("1");
        assertEquals(2, carRepository.findAll(QCar.car.color.startsWith("ver")).size());

        GumgaThreadScope.organizationCode.set("2");
        assertEquals(1, carRepository.findAll(QCar.car.color.startsWith("ver")).size());
    }

   // @Test
    public void should_find_all_with_sort() {
        BooleanExpression expression = QCar.car.color.startsWith("ver");

        GumgaThreadScope.organizationCode.set("1");
        assertEquals(2, carRepository.findAll(expression, QCar.car.color.asc()).size());

        GumgaThreadScope.organizationCode.set("2");
        assertEquals(1, carRepository.findAll(expression, QCar.car.color.asc()).size());
    }


    //@Test
    public void should_find_all_with_specification() {
        BooleanExpression expression = QCar.car.color.startsWith("ver");

        GumgaThreadScope.organizationCode.set("1");
        assertEquals(2, carRepository.findAll(query -> query.where(expression)).size());

        GumgaThreadScope.organizationCode.set("2");
        assertEquals(1, carRepository.findAll(query -> query.where(expression)).size());
    }

    //@Test
    public void should_find_all_with_specification_and_projection() {
        BooleanExpression expression = QCar.car.color.startsWith("ver");

        GumgaThreadScope.organizationCode.set("1");
        assertEquals(2, carRepository.findAll(query -> query.where(expression), QCar.car.color).size());

        GumgaThreadScope.organizationCode.set("2");
        assertEquals(1, carRepository.findAll(query -> query.where(expression), QCar.car.color).size());
    }


    //@Test
    public void should_find_all_with_specification_and_page() {

        GumgaThreadScope.organizationCode.set("1");
        Pageable page = new PageRequest(0, 4);
        Page<Car> result = carRepository.findAll(query -> query, page);
        assertEquals(2, result.getTotalPages());
        assertEquals(8, result.getTotalElements());
        assertEquals(4, result.getSize());

    }

    //@Test
    public void should_find_all_with_specification_and_projection_and_page() {

        GumgaThreadScope.organizationCode.set("1");
        Pageable page = new PageRequest(0, 4);
        Page<String> result = carRepository.findAll(query -> query, page, QCar.car.color);
        assertEquals(2, result.getTotalPages());
        assertEquals(8, result.getTotalElements());
        assertEquals(4, result.getSize());

    }

    //@Test
    public void should_search_with_specification_and_page() {

        GumgaThreadScope.organizationCode.set("1");
        Pageable page = new PageRequest(0, 4);
        SearchResult<Car> result = carRepository.search(query -> query, page);
        assertEquals(4, result.getPageSize());
        assertEquals(0, result.getStart());
        assertEquals(Long.valueOf(8), result.getCount());

    }

    //@Test
    public void should_search_with_specification_and_page_and_projection() {

        GumgaThreadScope.organizationCode.set("1");
        Pageable page = new PageRequest(0, 4);
        SearchResult<String> result = carRepository.search(query -> query, page, QCar.car.color);
        assertEquals(4, result.getPageSize());
        assertEquals(0, result.getStart());
        assertEquals(Long.valueOf(8), result.getCount());

    }

    //@Test
    public void should_search_with_predicate_and_page() {

        BooleanExpression predicate = QCar.car.color.length().goe(5);

        GumgaThreadScope.organizationCode.set("1");
        Pageable page = new PageRequest(0, 4);
        SearchResult<Car> result = carRepository.search(predicate, page);
        assertEquals(4, result.getPageSize());
        assertEquals(0, result.getStart());
        assertEquals(Long.valueOf(6), result.getCount());

    }

    //@Test
    public void should_search_with_predicate_and_page_and_projection() {
        BooleanExpression predicate = QCar.car.color.length().goe(5);

        GumgaThreadScope.organizationCode.set("1");
        Pageable page = new PageRequest(0, 4);
        SearchResult<String> result = carRepository.search(predicate, page, QCar.car.color);
        assertEquals(4, result.getPageSize());
        assertEquals(0, result.getStart());
        assertEquals(Long.valueOf(6), result.getCount());

    }
}