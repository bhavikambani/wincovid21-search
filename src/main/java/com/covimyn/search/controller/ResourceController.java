package com.covimyn.search.controller;

import com.covimyn.search.interfaces.ResourceEntryResponse;
import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.pojo.Pair;
import com.covimyn.search.services.ResourceService;
import com.covimyn.search.utility.Constant;
import com.covimyn.search.utility.OperationType;
import com.covimyn.search.utility.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.DefaultValue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ResourceController {

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Value("${google.sheet.id}")
    private String sheetId;

    @Value("${google.sheet.name}")
    private String sheetName;

    @Autowired
    ResourceService resourceService;

    @GetMapping(path = "/search")
    public ResponseEntity search(@QueryParam("id") String id,
                                         @QueryParam("stateId") Long stateId,
                                         @QueryParam("cityId") Long cityId,
                                         @QueryParam("categoryId") Long categoryId,
                                         @QueryParam("subcategoryId") Long subcategoryId,
                                         @QueryParam("isVerified") String isVerified,
                                         @DefaultValue("0") @QueryParam("offset") Integer offset,
                                         @DefaultValue("10") @QueryParam("rows") Integer rows,
                                         @DefaultValue("ASC") @QueryParam("sortOrder") String sortOrder,
                                         @DefaultValue("seeker") @QueryParam("userType") String userType
    ){
        try {
            List<Pair> must = new ArrayList<>();
            must.add(new Pair(Constant.ID, id));
            must.add(new Pair(Constant.STATEID, stateId));
            must.add(new Pair(Constant.CITYID, cityId));
            must.add(new Pair(Constant.CATEGORYID, categoryId));
            must.add(new Pair(Constant.SUBCATEGORYID, subcategoryId));
            must.add(new Pair(Constant.VERIFIED, isVerified));
            must.add(new Pair(Constant.VALID, true));

            if(userType == null) {
                userType = UserType.seeker.name();
            }

            logger.info(new ObjectMapper().writeValueAsString(must));

            ResourceEntryResponse response = resourceService.search(must, new ArrayList<Pair>(), offset, rows, UserType.valueOf(userType));
            logger.info("search api response: " +response );
            return new ResponseEntity(response,HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception in search request:",e);
            return new ResponseEntity(ResourceEntryResponse.errorResponseOfException(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/upsert", consumes = "application/json", produces = "application/json")
    public ResponseEntity addResourceRecord(@RequestBody ResourceRequest resourceRequest) {
        try {
            String upsertResponse = resourceService.upsert(resourceRequest);
            logger.info("upsert API response= "+upsertResponse);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Exception in upsert request:",e);
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/dump")
    public ResponseEntity dumpOperation(
                                 @QueryParam("isVerified") boolean isVerified,
                                 @QueryParam("date") String date,
                                 @QueryParam("operation") String operation){
        try {
            List<Pair> must = new ArrayList<>();
            must.add(new Pair(Constant.VERIFIED, isVerified));
            logger.info(new ObjectMapper().writeValueAsString(must));

            if(operation != null && operation.equals(OperationType.upload.name())) {
                logger.info("Performing upload operations");
                resourceService.upload(must, date, sheetId, sheetName);
                return new ResponseEntity(HttpStatus.OK);
            }
            else {
                logger.info("Performing download operations");
                InputStreamResource file = new InputStreamResource(resourceService.download(must, date));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wincovid-" + date+".csv")
                        .contentType(MediaType.parseMediaType("application/csv"))
                        .body(file);
            }

        } catch (Exception e) {
            logger.error("Exception in search request:", e);
            return new ResponseEntity(ResourceEntryResponse.errorResponseOfException(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
