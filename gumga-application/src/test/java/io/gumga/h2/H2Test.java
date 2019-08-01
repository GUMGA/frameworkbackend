package io.gumga.h2;

import io.gumga.alldatabases.AllDatabasesTest;
import io.gumga.application.SpringConfig;
import io.gumga.core.gquery.Criteria;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class H2Test extends AllDatabasesTest {

    public H2Test() {
        Criteria.doTranslate = true;
    }

}
