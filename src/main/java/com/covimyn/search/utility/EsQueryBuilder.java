/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class EsQueryBuilder {

    //TODO: Need proper verification
    public JSONObject generateSearchQuery(Map<String, Object> mustParams, Map<String, Object> shouldParams,
                                          AbstractMap.SimpleEntry<String, String> sortParam) {
        JSONObject payload = new JSONObject();
        JSONObject query = new JSONObject();
        JSONObject sort = new JSONObject();
        JSONObject bool = new JSONObject();
        JSONArray must = new JSONArray();
        JSONArray should = new JSONArray();

        mustParams.entrySet().stream().forEach(e -> {
            JSONObject term = new JSONObject();
            term.put(e.getKey(), e.getValue());
            must.add(term);
        });

        shouldParams.entrySet().stream().forEach(e -> {
            JSONObject term = new JSONObject();
            term.put(e.getKey(), e.getValue());
            should.add(term);
        });

        JSONObject mustToBool = new JSONObject();
        mustToBool.put("must", should);
        should.add(mustToBool);


        bool.put("bool", must);
        payload.put("query", query);

        if(shouldParams != null) {
            sort.put(sortParam.getKey(), sortParam.getValue());
            payload.put("sort", sort);
        }

        return payload;

    }

}
