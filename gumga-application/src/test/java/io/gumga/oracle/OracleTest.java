package io.gumga.oracle;

import io.gumga.alldatabases.AllDatabasesTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {OracleSpringConfig.class})
public class OracleTest extends AllDatabasesTest{
    
}