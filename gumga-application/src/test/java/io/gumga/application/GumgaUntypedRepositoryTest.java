//package io.gumga.application;
//
//import io.gumga.core.GumgaThreadScope;
//import io.gumga.domain.domains.GumgaBoolean;
//import io.gumga.testmodel.Lamp;
//import io.gumga.testmodel.LampService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {SpringConfig.class})
//public class GumgaUntypedRepositoryTest {
//
//    @Autowired
//    private GumgaUntypedRepository gumgaUntypedRepository;
//
//    @Autowired
//    private LampService lampService;
//
//    public GumgaUntypedRepositoryTest() {
//        GumgaThreadScope.organizationCode.set("1.");
//        GumgaThreadScope.login.set("gumga@gumga.com.br");
//    }
//
//    @Test
//    public void gumgaBooleanVersion() {
//        Lamp l=new Lamp("lampada 1", new GumgaBoolean(true));
//        l=lampService.save(l);
//        assertEquals(0, l.getVersion().intValue());
//        l=lampService.view(l.getId());
//        l.setIson(new GumgaBoolean(true));
//        l=lampService.save(l);
//        assertEquals(0, l.getVersion().intValue());
//
//        List<Object> objects1 = gumgaUntypedRepository.fullTextSearch("lampada");
//        List<Object> objects2 = gumgaUntypedRepository.fullTextSearch("lampada 1");
//        System.out.println(objects1);
//    }
//
//
//}
