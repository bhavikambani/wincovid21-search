/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.utility;

import com.covimyn.search.pojo.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EsQueryBuilder {

    public JSONObject generateSearchQuery(List<Pair> mustParams, List<Pair> shouldParams, int page, int size) {
        JSONObject payload = new JSONObject();

        JSONObject bool = new JSONObject();
        JSONArray must = new JSONArray();
        JSONArray should = new JSONArray();


        for(Pair e : mustParams) {
            JSONObject term = new JSONObject();
            JSONObject object = new JSONObject();
            object.put(e.getKey(), e.getValue());
            term.put("term", object);
            must.add(term);
        }

        for(Pair e : shouldParams) {
            JSONObject term = new JSONObject();
            JSONObject object = new JSONObject();
            object.put(e.getKey(), e.getValue());
            term.put("term", object);
            should.add(term);
        }

        if(shouldParams != null && shouldParams.size() > 0) {
            JSONObject mustToBool = new JSONObject();
            JSONObject mustToBoolVal = new JSONObject();
            mustToBoolVal.put("should", should);
            mustToBool.put("bool", mustToBoolVal);
            must.add(mustToBool);
        }

        JSONObject boolValue = new JSONObject();
        boolValue.put("must", must);

        bool.put("bool", boolValue);
        payload.put("query", bool);

        payload.put("from", page);
        payload.put("size", size);

        return payload;
    }


    public JSONObject generateSearchQueryWithSort(List<Pair> mustParams, List<Pair> shouldParams, int page, int size,
                                                  Pair sortParam) {
        JSONObject sort = new JSONObject();
        sort.put(sortParam.getKey(), sortParam.getValue());
        JSONObject payload = generateSearchQuery(mustParams, shouldParams, page, size);
        payload.put("sort", sort);
        return payload;
    }
}
