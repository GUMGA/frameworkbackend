package io.gumga.postgres;

import io.gumga.alldatabases.AllDatabasesTest;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PostgresSpringConfig.class})
public class PostgresTest extends AllDatabasesTest{
    
}
