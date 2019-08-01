package io.gumga.application.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gumga.application.SpringConfig;
import io.gumga.domain.tag.GumgaTag;
import io.gumga.domain.tag.GumgaTagDefinition;
import io.gumga.testmodel.Car;
import io.gumga.testmodel.CarRepository;
import io.gumga.testmodel.CompanyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class TagsTest {

    private static final Map<String, String> tagVersaoValuesJetta = new HashMap<>();
    private GumgaTag versaoJetta;
    private GumgaTag motorizacaoJetta;
    private GumgaTag versaoMarch;
    private GumgaTag motorizacaoMarch;

    {
        tagVersaoValuesJetta.put("Nome", "TSi Turbo");
        tagVersaoValuesJetta.put("Ano", "2015");
    }

    private static final Map<String, String> tagMotorizacaoValuesJetta = new HashMap<>();

    {
        tagMotorizacaoValuesJetta.put("Potência", "180cv");
        tagMotorizacaoValuesJetta.put("Cilindros", "6");
    }

    private static final Map<String, String> tagVersaoValuesMarch = new HashMap<>();

    {
        tagVersaoValuesMarch.put("Nome", "Cross");
        tagVersaoValuesMarch.put("Ano", "2015");
    }

    private static final Map<String, String> tagMotorizacaoValuesMarch = new HashMap<>();

    {
        tagMotorizacaoValuesMarch.put("Potência", "90cv");
        tagMotorizacaoValuesMarch.put("Cilindros", "4");
    }

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private GumgaTagService tagService;

    @Autowired
    private GumgaTagRepository tagRepository;

    @Autowired
    private GumgaTagDefinitionService tagDefinitionService;

    private GumgaTagDefinition versaoDef;

    private GumgaTagDefinition motorizacaoDef;

    private Car jetta;

    private Car march;

    @BeforeEach
    public void insertData() {
        //Check sanity before making any calls
        injectionSanityCheck();

        jetta = carRepository.save(new Car("black"));
        march = carRepository.save(new Car("silver"));

        assertNotNull(jetta);
        assertNotNull(jetta.getId());
        assertNotNull(march);
        assertNotNull(march.getId());

        versaoDef = createTagDefinition("Versão", "Nome", "Ano");
        motorizacaoDef = createTagDefinition("Motorização", "Potência", "Cilindros");

    }

    @AfterEach
    public void tearDown() {
        versaoDef = null;
        motorizacaoDef = null;
    }

    private void injectionSanityCheck() {
        assertNotNull(companyService);
        assertNotNull(tagService);
        assertNotNull(tagDefinitionService);
    }

    private void setTagValues(GumgaTag tag, Map<String, String> tagValues) {
        tag.getDefinition().getAttributes().stream().forEach((attr) -> {
            tag.addValue(attr, tagValues.get(attr.getName()));
        });
    }

    private GumgaTagDefinition createTagDefinition(String name, String... attr) {
        GumgaTagDefinition tagDef = tagDefinitionService.createNew(name, attr);
        tagDef = tagDefinitionService.save(tagDef);
        assertNotNull(tagDef);
        assertNotNull(tagDef.getId());
        return tagDef;
    }

    @Test
    @Transactional
    public void createTagsForCar() {
        versaoJetta = tagService.createNew(versaoDef, jetta.getClass().getName(), jetta.getId());
        setTagValues(versaoJetta, tagVersaoValuesJetta);
        tagService.save(versaoJetta);
        motorizacaoJetta = tagService.createNew(motorizacaoDef, jetta.getClass().getName(), jetta.getId());
        setTagValues(motorizacaoJetta, tagMotorizacaoValuesJetta);
        tagService.save(motorizacaoJetta);

        versaoMarch = tagService.createNew(versaoDef, march.getClass().getName(), march.getId());
        setTagValues(versaoMarch, tagVersaoValuesMarch);
        tagService.save(versaoMarch);
        motorizacaoMarch = tagService.createNew(motorizacaoDef, march.getClass().getName(), march.getId());
        setTagValues(motorizacaoMarch, tagMotorizacaoValuesMarch);
        tagService.save(motorizacaoMarch);

        assertTrue(tagRepository.count() > 0);
    }

    @Test
    @Transactional
    public void findTagsForCar() throws Exception{
        createTagsForCar();

        List<GumgaTag> tags = tagService.findByObject(jetta);
        assertTrue(!tags.isEmpty());
        assertTrue(tags.contains(versaoJetta));
        assertTrue(tags.contains(motorizacaoJetta));
        
        ObjectMapper om = new ObjectMapper();
        System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(tags));

        tags = tagService.findByObject(march);
        assertTrue(!tags.isEmpty());
        assertTrue(tags.contains(versaoMarch));
        assertTrue(tags.contains(motorizacaoMarch));
    }
}
