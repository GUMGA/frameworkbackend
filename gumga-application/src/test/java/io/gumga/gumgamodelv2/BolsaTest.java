package io.gumga.gumgamodelv2;

import io.gumga.application.SpringConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.constraints.AssertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class BolsaTest {

    @Autowired
    private BolsaRepository bolsaRepository;
    @Autowired
    private BolsaService bolsaService;

    @Before
    public void setUp() throws Exception {
        Bolsa bolsa = new Bolsa();
        bolsa.setNome("Bolsa seed");
        this.bolsaRepository.saveAndFlush(bolsa);
    }

    @Test
    public void saveBolsaRepository() {
        Bolsa bolsa = new Bolsa();
        bolsa.setNome("Bolsa 1");
        Bolsa saved = this.bolsaRepository.saveAndFlush(bolsa);

        Bolsa result = this.bolsaRepository.findOne(saved.getId());
        Assert.assertNotNull(result);
    }

    @Test
    public void saveBolsaService() {
        Bolsa bolsa = new Bolsa();
        bolsa.setNome("Bolsa 2");
        Bolsa saved = this.bolsaService.save(bolsa);
        Bolsa result = this.bolsaService.view(saved.getId());

        Assert.assertNotNull(result);
    }
}
