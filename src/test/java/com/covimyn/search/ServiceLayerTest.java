/**
 * Created by Avishek Gurung on 2021-05-01
 */

package com.covimyn.search;

import com.covimyn.search.dao.ResourceDao;
import com.covimyn.search.dao.ResourceDaoImpl;
import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.pojo.Pair;
import com.covimyn.search.services.ResourceService;
import com.covimyn.search.services.ResourceServiceImpl;
import com.covimyn.search.utility.EsQueryBuilder;
import com.covimyn.search.utility.HttpHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceLayerTest {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static HttpHelper httpHelper = new HttpHelper(objectMapper);
    private static EsQueryBuilder esQueryBuilder = new EsQueryBuilder();
    private static String esIndex = "http://localhost:9200/resource_alias";
    private static JSONParser jsonParser = new JSONParser();


    public static void main(String[] args) throws Exception {
        //testUpsert();
        testSearch();
    }


    public static void testSearch() throws Exception {
        ResourceDao resourceDao = new ResourceDaoImpl(esIndex, httpHelper, esQueryBuilder, jsonParser);
        ResourceService resourceService = new ResourceServiceImpl(resourceDao);

        //conditions
        List<Pair> must = new ArrayList<>();
        must.add(new Pair("verified", true));
        must.add(new Pair("available", false));

        List<Pair> should = new ArrayList<>();
        should.add(new Pair("resourceType", "Oxygen Cyclinder"));
        should.add(new Pair("resourceType", "Oxymeter"));

        List<ResourceResponse> resourceResponses = resourceService.search(must, should, 1, 10, true);

        for(ResourceResponse resourceResponse : resourceResponses) {
            System.out.println(objectMapper.writeValueAsString(resourceResponse));
        }
    }

    private static void testUpsert() throws IOException {
        ResourceDao resourceDao = new ResourceDaoImpl(esIndex, httpHelper, esQueryBuilder, jsonParser);
        ResourceService resourceService = new ResourceServiceImpl(resourceDao);

        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setId("5");
        resourceRequest.setName("C");
        resourceRequest.setComment("This is fine for now");
        resourceRequest.setAddress("Ladenla road, Darjeeling");
        resourceRequest.setDistrict("Darjeeling");
        resourceRequest.setEmail("avishek@gmail.com");
        resourceRequest.setPincode("734101");
        resourceRequest.setCity("Darjeeling");
        resourceRequest.setState("West Bengal");
        resourceRequest.setPhone("090-8787633456");
        resourceRequest.setLocation("Darjeeling");
        resourceRequest.setVerified(false);
        resourceRequest.setResourceType("Oxymeter");
        resourceRequest.setAvailable(false);
        resourceRequest.setFeedback("Available");
        resourceRequest.setCreatedAt("2021-05-01T01:10:30Z");
        resourceRequest.setUpdatedAt("2021-05-02T01:10:30Z");
        resourceRequest.setCreatedBy("Avishek");
        resourceRequest.setUpdatedBy("Yogesh");
        resourceService.upsert(resourceRequest);
        System.out.println("DONE");

    }

}
