package io.gumga.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gumga.core.gquery.ComparisonOperator;
import io.gumga.core.gquery.Criteria;
import io.gumga.core.gquery.GQuery;

/**
 * Created by gumgait on 14/09/17.
 */
public class testeGquery {


    public static void main(String[] args) throws Exception {
        GQuery gQuery = new GQuery(new Criteria("name", ComparisonOperator.CONTAINS, "Mat"));
                gQuery = gQuery.or(new Criteria("idade", ComparisonOperator.GREATER, 3));

        System.out.println(gQuery.toString());
        ObjectMapper m=new ObjectMapper();

        System.out.println(m.writeValueAsString(gQuery));

    }
}