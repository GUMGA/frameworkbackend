package io.gumga.mysql;

import io.gumga.alldatabases.AllDatabasesTest;
import io.gumga.mysql.MysqlSpringConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MysqlSpringConfig.class})
public class MysqlTest extends AllDatabasesTest {

}
