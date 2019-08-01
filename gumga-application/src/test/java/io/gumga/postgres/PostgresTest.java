package io.gumga.postgres;

import io.gumga.alldatabases.AllDatabasesTest;
import io.gumga.core.gquery.Criteria;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {PostgresSpringConfig.class})
//@Disabled
public class PostgresTest {//extends AllDatabasesTest {

    public PostgresTest() {
        Criteria.doTranslate = true;
    }

}
