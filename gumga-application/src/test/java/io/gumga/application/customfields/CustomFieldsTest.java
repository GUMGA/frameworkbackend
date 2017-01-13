package io.gumga.application.customfields;

import io.gumga.application.*;
import io.gumga.core.QueryObject;
import io.gumga.domain.customfields.CustomFieldType;
import io.gumga.domain.customfields.GumgaCustomField;
import io.gumga.domain.customfields.GumgaCustomFieldValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class CustomFieldsTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private GumgaCustomFieldService customFieldService;

    @Autowired
    private GumgaCustomFieldValueService customFieldValueService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private GumgaCustomEnhancerService enhancerService;

    @Test
    public void injectionSanityCheck() {
        assertNotNull(companyService);
        assertNotNull(customFieldService);
        assertNotNull(customFieldValueService);
    }

    @Before
    public void insertData() {
        if (carRepository.findAll().size() > 0) {
            return;
        }
        GumgaCustomField textCustomField = new GumgaCustomField(Company.class.getName(), "textField", "Custom Text Field for test", true, CustomFieldType.TEXT, "Not empty", "return true;", "'new value'", "", 1.0, "MAIN_FIELDS", "a.b.c");
        GumgaCustomField numberCustomField = new GumgaCustomField(Company.class.getName(), "numerField", "Custom Number Field for test", true, CustomFieldType.NUMBER, "Not empty", "true", "13", "", 2.0, "MAIN_FIELDS", "a.b.c");
        GumgaCustomField dateCustomField = new GumgaCustomField(Company.class.getName(), "dateField", "Custom date Field for test", true, CustomFieldType.DATE, "Not empty", "true", "Date()", "", 3.0, "MAIN_FIELDS", "a.b.c");
        GumgaCustomField logicCustomField = new GumgaCustomField(Car.class.getName(), "logicField", "Custom logic Field for test", true, CustomFieldType.LOGIC, "Not empty", "true", "true", "", 4.0, "MAIN_FIELDS", "a.b.c");
        GumgaCustomField selectionCustomField = new GumgaCustomField(Car.class.getName(), "selectionField", "Custom selection Field for test", true, CustomFieldType.SELECTION, "Not empty", "true", "'Nothing'", "", 5.0, "MAIN_FIELDS", "a.b.c");

        customFieldService.save(dateCustomField);
        customFieldService.save(textCustomField);
        customFieldService.save(numberCustomField);
        customFieldService.save(logicCustomField);
        customFieldService.save(selectionCustomField);

        Car jetta = new Car("black");
        Car march = new Car("silver");

        carRepository.save(jetta);
        carRepository.save(march);

        customFieldValueService.save(new GumgaCustomFieldValue(logicCustomField, march.getId(), true));
        customFieldValueService.save(new GumgaCustomFieldValue(selectionCustomField, march.getId(), "one door"));
        customFieldValueService.save(new GumgaCustomFieldValue(selectionCustomField, march.getId(), "two doors"));
        customFieldValueService.save(new GumgaCustomFieldValue(logicCustomField, jetta.getId(), false));
        customFieldValueService.save(new GumgaCustomFieldValue(selectionCustomField, jetta.getId(), "four doors"));

        
        

    }

    @Test
    @Transactional
    public void listFieldsforCar() {
        List<GumgaCustomField> values = customFieldService.pesquisa(new QueryObject()).getValues();
        for (GumgaCustomField gcf:values){
            System.out.println("---------->"+gcf.getClazz()+"  "+gcf.getDescription()+"  "+gcf.getOi());
        }
        
        List<GumgaCustomField> result = customFieldService.findByClass(Car.class);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void listFieldsforCompany() {
        List<GumgaCustomField> result = customFieldService.findByClass(Company.class);
        assertEquals(3, result.size());
    }

    @Test
    @Transactional
    public void listFieldsforSomeCar() {
        Car someCar = new Car();
        List<GumgaCustomField> result = customFieldService.findByClass(someCar);
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void listEnhacedCars() {
        List<Car> result = carRepository.findAll();
        enhancerService.loadCustomFields(result.get(0));
        int size = result.get(0).getGumgaCustomFields().size();
        assertEquals(2, size);
    }

}
