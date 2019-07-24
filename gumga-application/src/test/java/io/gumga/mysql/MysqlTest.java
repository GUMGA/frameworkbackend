package io.gumga.mysql;

import io.gumga.alldatabases.AllDatabasesTest;
import io.gumga.core.gquery.Criteria;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {MysqlSpringConfig.class})
//@Disabled
public class MysqlTest {//extends AllDatabasesTest {

    public MysqlTest() {
        Criteria.doTranslate = false;
    }

}
