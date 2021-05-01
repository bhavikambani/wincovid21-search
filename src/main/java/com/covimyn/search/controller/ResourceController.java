package com.covimyn.search.controller;

import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.List;

@RestController
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    // GET
    //Query1: state, city, resourceType
    //Query2: state, city, resourceType,isVerifiedcurl --location --request GET 'http://localhost:8080/search?id=1234'
    //Query3: id
    @GetMapping(path = "/search")
    public ResponseEntity search(@QueryParam("id") String id,
                                         @QueryParam("state") String state,
                                         @QueryParam("city") String city,
                                         @QueryParam("resourceType") String resourceType,
                                         @QueryParam("isVerified") String isVerified,
                                         @DefaultValue("0") @QueryParam("offset") int offset,
                                         @DefaultValue("10") @QueryParam("rows") int rows,
                                         @DefaultValue("ASC") @QueryParam("sortOrder") String sortOrder
    ){

        try {
            List<ResourceResponse> resourceResponseList = resourceService.search(id, state, city, resourceType, isVerified, offset, rows, sortOrder);
            return new ResponseEntity(resourceResponseList,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/upsert", consumes = "application/json", produces = "application/json")
    public ResponseEntity addResourceRecord(@RequestBody ResourceRequest resourceRequest) {
        try {
            String upsertResponse = resourceService.upsert(resourceRequest);
            System.out.println(upsertResponse);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
