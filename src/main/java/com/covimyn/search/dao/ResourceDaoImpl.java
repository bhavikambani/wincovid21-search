/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.dao;

import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.pojo.ApiHelperResponse;
import com.covimyn.search.utility.Constant;
import com.covimyn.search.utility.EsQueryBuilder;
import com.covimyn.search.utility.HttpHelper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
public class ResourceDaoImpl implements ResourceDao {

    private String esResourceEndPoint;
    private HttpHelper httpHelper;
    private EsQueryBuilder esQueryBuilder;

    @Override
    public String upsert(ResourceModel resourceModel) throws IOException {
        ApiHelperResponse response = httpHelper.makeHttpPostRequest(esResourceEndPoint + "/" +
                Constant.ES_RESOURCE_INDEX_TYPE + "/" + resourceModel.getId(), resourceModel);
        if(response.getStatusCode() != HttpStatus.SC_CREATED) {
            throw new RuntimeException("Returned non 201 response code on persistence");
        }
        return (String) response.getPayload();
    }

    @Override
    public List<ResourceModel> search(Map<String, Object> must, Map<String, Object> should,
                                      AbstractMap.SimpleEntry<String, String> sortBy) throws IOException {
        JSONObject esSearchQuery = esQueryBuilder.generateSearchQuery(must, should, sortBy);
        ApiHelperResponse response = httpHelper.makeHttpPostRequest(esResourceEndPoint + "/_search", esSearchQuery);
        if(response.getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("Returned non 201 response code on persistence");
        }
        return (List<ResourceModel>) response.getPayload();
    }
}
