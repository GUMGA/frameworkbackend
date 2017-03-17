package io.gumga.h2;

import io.gumga.mysql.*;
import io.gumga.alldatabases.AllDatabasesTest;
import io.gumga.application.SpringConfig;
import io.gumga.mysql.MysqlSpringConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class H2Test extends AllDatabasesTest {

}
