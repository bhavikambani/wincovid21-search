/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.dao;

import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.pojo.ApiHelperResponse;
import com.covimyn.search.pojo.Pair;
import com.covimyn.search.utility.Constant;
import com.covimyn.search.utility.EsQueryBuilder;
import com.covimyn.search.utility.HttpHelper;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Component
public class ResourceDaoImpl implements ResourceDao {
    private static final Logger logger = LoggerFactory.getLogger(ResourceDaoImpl.class);

    @Value("${alias.endpoint}")
    private String esResourceEndPoint;
    @Autowired
    private HttpHelper httpHelper;
    @Autowired
    private EsQueryBuilder esQueryBuilder;
    @Autowired
    private JSONParser jsonParser;

    @Override
    public String upsert(ResourceModel resourceModel) throws IOException {
        ApiHelperResponse response = httpHelper.makeHttpPostRequest(esResourceEndPoint + "/" +
                Constant.ES_RESOURCE_INDEX_TYPE + "/" + resourceModel.getId(), resourceModel);
        logger.info("ES upsert response= "+response);
        if(response.getStatusCode() == HttpStatus.SC_CREATED || response.getStatusCode() == HttpStatus.SC_OK)  {
            return (String) response.getPayload();
        }

        throw new RuntimeException("Returned non 201 response code[" +response.getStatusCode()+ "] " +
                "with a phrase '" + response.getReasonPhrase()+ "'on persistence");
    }

    @Override
    public List<ResourceModel> search(List<Pair> must, List<Pair> should, int page, int size) throws Exception {
        JSONObject esSearchQuery = esQueryBuilder.generateSearchQuery(must, should, page, size);
        ApiHelperResponse response = httpHelper.makeHttpPostRequest(esResourceEndPoint + "/_search", esSearchQuery);
        if(response.getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("Returned non 200 response code on persistence");
        }
        JSONObject jsonResponseObject = (JSONObject)jsonParser.parse(response.getPayload().toString());
        return getResourceModelsFromEsResponse(jsonResponseObject);
    }

    @Override
    public List<ResourceModel> searchByLatest(List<Pair> must, List<Pair> should, int page, int size) throws Exception {
        Pair sortBy = new Pair(Constant.UPDATED_AT, "desc");
        JSONObject esSearchQuery = esQueryBuilder.generateSearchQueryWithSort(must, should, page, size, sortBy);
        logger.info("Elastic search query= "+esSearchQuery.toString());
        ApiHelperResponse response = httpHelper.makeHttpPostRequest(esResourceEndPoint + "/_search", esSearchQuery);
        logger.info("Elastic search response= "+response.toString());
        if(response.getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("Returned non 200 response code on persistence");
        }
        JSONObject jsonResponseObject = (JSONObject)jsonParser.parse(response.getPayload().toString());
        return getResourceModelsFromEsResponse(jsonResponseObject);
    }


    private List<ResourceModel> getResourceModelsFromEsResponse(JSONObject jsonObject) {
        List<ResourceModel> resourceModels = new ArrayList<>();
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        JSONArray result = (JSONArray) hits.get("hits");
        result.iterator().forEachRemaining(element -> {
            JSONObject hitObject = (JSONObject) element;
            resourceModels.add(getResourceModelFromJson((JSONObject) hitObject.get("_source")));
        });
        return resourceModels;
    }

    /**
     * Es to Pojo transformation
     * @param jsonObject
     * @return
     */
    private ResourceModel getResourceModelFromJson(JSONObject jsonObject) {
        ResourceModel resourceModel = new ResourceModel();
        resourceModel.setId((String) jsonObject.get(Constant.ID));
        resourceModel.setName((String)jsonObject.get(Constant.NAME));
        resourceModel.setCategory((String)jsonObject.get(Constant.CATEGORY));
        resourceModel.setSubcategory((String) jsonObject.get(Constant.SUBCATEGORY));
        resourceModel.setAddress((String) jsonObject.get(Constant.ADDRESS));
        resourceModel.setPincode((String) jsonObject.get(Constant.PINCODE));
        resourceModel.setDescription((String) jsonObject.get(Constant.DESCRIPTION));
        resourceModel.setPhone1((String) jsonObject.get(Constant.PHONE1));
        resourceModel.setPhone2((String) jsonObject.get(Constant.PHONE2));
        resourceModel.setEmail((String) jsonObject.get(Constant.EMAIL));
        resourceModel.setCity((String) jsonObject.get(Constant.CITY));
        resourceModel.setState((String) jsonObject.get(Constant.STATE));
        resourceModel.setAvailable((boolean) jsonObject.get(Constant.AVAILABLE));
        resourceModel.setCreatedBy((String) jsonObject.get(Constant.CREATED_BY));
        resourceModel.setCreatedAt((String) jsonObject.get(Constant.CREATED_AT));
        resourceModel.setUpdatedBy((String) jsonObject.get(Constant.UPDATED_BY));
        resourceModel.setUpdatedAt((String) jsonObject.get(Constant.UPDATED_AT));
        resourceModel.setVerified((boolean) jsonObject.get(Constant.VERIFIED));
        return resourceModel;
    }

}
