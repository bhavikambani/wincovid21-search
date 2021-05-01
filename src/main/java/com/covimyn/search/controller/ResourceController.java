package com.covimyn.search.controller;

import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@RestController
public class ResourceController {

    @Autowired
    ResourceService resourceService;


        // GET
    //apicontollurl/path?q:id=1&city="bangalore"&district="xyz"
    //Query1: state, city, resourceType
    //Query2: state, city, resourceType,isVerifiedcurl --location --request GET 'http://localhost:8080/search?id=1234'
    //Query3: id
    @GetMapping(path = "/search")
    public List<ResourceResponse> search( @QueryParam("id") String id,
                                    @QueryParam("state") String state,
                                    @QueryParam("city") String city,
                                    @QueryParam("resourceType") String resourceType,
                                    @QueryParam("isVerified") String isVerified,
                                    @DefaultValue("0") @QueryParam("offset") int offset,
                                    @DefaultValue("10") @QueryParam("rows") int rows,
                                    @DefaultValue("ASC") @QueryParam("sortOrder") String sortOrder
                                   ) throws Exception{

        return resourceService.search(id,state,city,resourceType,isVerified,offset,rows,sortOrder);

    }



        // POST

}
