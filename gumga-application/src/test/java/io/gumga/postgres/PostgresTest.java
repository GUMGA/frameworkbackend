package io.gumga.postgres;

import io.gumga.alldatabases.AllDatabasesTest;
import io.gumga.core.gquery.Criteria;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PostgresSpringConfig.class})
@Ignore
public class PostgresTest extends AllDatabasesTest {

    public PostgresTest() {
        Criteria.doTranslate = true;
    }

}
