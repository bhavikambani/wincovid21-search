package com.covimyn.search.controller;

import com.covimyn.search.interfaces.ResourceEntryResponse;
import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.DefaultValue;
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

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    ResourceService resourceService;

    //Query1: state, city, resourceType
    //Query2: state, city, resourceType,isVerified
    //Query3: id
    @GetMapping(path = "/search")
    public ResponseEntity search(@QueryParam("id") String id,
                                         @QueryParam("state") String state,
                                         @QueryParam("city") String city,
                                         @QueryParam("category") String category,
                                         @QueryParam("subcategory") String subcategory,
                                         @QueryParam("isVerified") String isVerified,
                                         @DefaultValue("0") @QueryParam("offset") Integer offset,
                                         @DefaultValue("10") @QueryParam("rows") Integer rows,
                                         @DefaultValue("ASC") @QueryParam("sortOrder") String sortOrder
    ){
        try {
            List<ResourceResponse> resourceResponseList = resourceService.search(id, state, city, category,subcategory, isVerified, offset, rows, sortOrder);
            return new ResponseEntity(ResourceEntryResponse.of(resourceResponseList),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(ResourceEntryResponse.errorResponseOfException(),HttpStatus.INTERNAL_SERVER_ERROR);
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