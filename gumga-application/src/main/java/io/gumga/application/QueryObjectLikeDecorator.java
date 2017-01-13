/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import io.gumga.core.QueryObject;

/**
 *
 * @author Gyowanny
 */
public class QueryObjectLikeDecorator {

    private QueryObject query;

    public QueryObjectLikeDecorator(QueryObject _query) {
        query = _query;
    }

    public QueryObject build() {
        if (query.isAdvanced()){
            return query;
        }
        StringBuilder where = new StringBuilder();
        if (query.getSearchFields() != null) {
            for (String param : query.getSearchFields()) {
                where.append("obj." + param + " like '%" + query.getQ() + "%'");
            }
            
        }
        else{
            where.append("obj.id is not null ");
        }
        query.setAq(where.toString());
        return query;
    }
}
