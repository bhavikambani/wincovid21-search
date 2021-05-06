/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.controller.ResourceController;
import com.covimyn.search.dao.ResourceDao;
import com.covimyn.search.interfaces.ResourceEntryResponse;
import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.pojo.Pair;
import com.covimyn.search.pojo.RangeEntity;
import com.covimyn.search.utility.Constant;
import com.covimyn.search.utility.CsvHelper;
import com.covimyn.search.utility.UserType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ResourceServiceImpl implements ResourceService {
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private ResourceDao resourceDao;
    private DateUtil dateUtil;
    private CsvHelper csvHelper;

    @Override
    public String upsert(ResourceRequest resourceRequest) throws IOException {
        ResourceModel resourceModel = transformResourceRequestToResourceModel(resourceRequest);
        logger.info("Insert into ES request object:" + resourceModel);
        return resourceDao.upsert(resourceModel);
    }

    @Override
    public ByteArrayInputStream externalDownload(List<Pair> mustParams, String queryDate) throws Exception {
        int page = 0;
        int size = 10000; //getting max page.


        Long queryDateInMilli = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        if(queryDate != null) {
            queryDateInMilli = dateUtil.dateToEpoch(queryDate);
        }
        RangeEntity rangeEntity = new RangeEntity(Constant.UPDATED_AT);
        rangeEntity.setParam1(Constant.GTE);
        rangeEntity.setValue1(queryDateInMilli.toString());

        List<Pair> sortOrder = new ArrayList<>();
        sortOrder.add(new Pair(Constant.AVAILABLE, Constant.DESCENDING));
        sortOrder.add(new Pair(Constant.UPDATED_AT, Constant.DESCENDING));
        List<ResourceModel> resourceModels = resourceDao.searchByLatest(mustParams, null, sortOrder, 0, size, rangeEntity);
        return csvHelper.convertResourceModelToCSV(resourceModels);
    }

    @Override
    public ResourceEntryResponse search(List<Pair> mustParams, List<Pair> shouldParams, Integer offset, Integer rows, UserType userType)
            throws Exception {
        List<ResourceResponse> resourceResponses;
        List<Pair> must = new ArrayList<>();
        List<Pair> should = new ArrayList<>();

        for(Pair pair : mustParams) {
            if(pair.getValue() != null) {
                must.add(pair);
            }
        }

        for(Pair pair : shouldParams) {
            if(pair.getValue() != null) {
                should.add(pair);
            }
        }

        offset = offset == null ? 0 : offset;
        rows = rows == null ? 10 : rows;

        List<Pair> sortOrder = new ArrayList<>();
        if(userType == UserType.seeker) {
            sortOrder.add(new Pair(Constant.VERIFIED, Constant.DESCENDING));
            sortOrder.add(new Pair(Constant.AVAILABLE, Constant.DESCENDING));
        }
        else {
            must.add(new Pair(Constant.AVAILABLE, false));
            sortOrder.add(new Pair(Constant.VERIFIED, Constant.DESCENDING));
        }
        sortOrder.add(new Pair(Constant.UPDATED_AT, Constant.DESCENDING));

        List<ResourceModel> resourceModels = resourceDao.searchByLatest(must, should, sortOrder, offset, rows, null);
        long count = resourceDao.count(must, new ArrayList<>());

        //List<ResourceModel> verifiedResults = resourceModels.stream().filter(ResourceModel::isVerified).collect(Collectors.toList());
        //List<ResourceModel> unVerifiedResults = resourceModels.stream().filter(resourceModel -> !resourceModel.isVerified()).collect(Collectors.toList());

        // add first verified and then unverified results in response
        //resourceResponses = verifiedResults.stream().map(this::transformResourceModelToResourceResponse).collect(Collectors.toList());
        //unVerifiedResults.stream().map(this::transformResourceModelToResourceResponse).forEach(resourceResponses::add);

        resourceResponses = resourceModels.stream().map(this::transformResourceModelToResourceResponse).collect(Collectors.toList());
        logger.info("Returned result size= " + resourceResponses.size());

        ResourceEntryResponse response = ResourceEntryResponse.of(resourceResponses, count);
        return response;
    }

    /**
     * A transformation logic.
     *
     * @param resourceRequest
     * @return
     */
    private ResourceModel transformResourceRequestToResourceModel(ResourceRequest resourceRequest) {
        ResourceModel resourceModel = new ResourceModel();
        resourceModel.setId(resourceRequest.getId());
        resourceModel.setName(resourceRequest.getName());
        resourceModel.setCategory(resourceRequest.getCategory());
        resourceModel.setCategoryId(resourceRequest.getCategoryId());
        resourceModel.setSubcategory(resourceRequest.getSubcategory());
        resourceModel.setSubcategoryId(resourceRequest.getSubcategoryId());
        resourceModel.setAddress(resourceRequest.getAddress());
        resourceModel.setPincode(resourceRequest.getPincode());
        resourceModel.setDescription(resourceRequest.getDescription());
        resourceModel.setPhone1(resourceRequest.getPhone1());
        resourceModel.setPhone2(resourceRequest.getPhone2());
        resourceModel.setEmail(resourceRequest.getEmail());
        resourceModel.setCity(resourceRequest.getCity());
        resourceModel.setCityId(resourceRequest.getCityId());
        resourceModel.setState(resourceRequest.getState());
        resourceModel.setStateId(resourceRequest.getStateId());
        resourceModel.setAvailable(resourceRequest.isAvailable());
        resourceModel.setCreatedBy(resourceRequest.getCreatedBy());
        resourceModel.setCreatedAt(resourceRequest.getCreatedAt());
        resourceModel.setUpdatedBy(resourceRequest.getUpdatedBy());
        resourceModel.setUpdatedAt(resourceRequest.getUpdatedAt());
        resourceModel.setVerified(resourceRequest.isVerified());
        return resourceModel;
    }

    private ResourceResponse transformResourceModelToResourceResponse(ResourceModel resourceModel) {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setId(resourceModel.getId());
        resourceResponse.setName(resourceModel.getName());
        resourceResponse.setCategory(resourceModel.getCategory());
        resourceResponse.setCategoryId(resourceModel.getCategoryId());
        resourceResponse.setSubcategory(resourceModel.getSubcategory());
        resourceResponse.setSubcategoryId(resourceModel.getSubcategoryId());
        resourceResponse.setAddress(resourceModel.getAddress());
        resourceResponse.setPincode(resourceModel.getPincode());
        resourceResponse.setDescription(resourceModel.getDescription());
        resourceResponse.setPhone1(resourceModel.getPhone1());
        resourceResponse.setPhone2(resourceModel.getPhone2());
        resourceResponse.setEmail(resourceModel.getEmail());
        resourceResponse.setCity(resourceModel.getCity());
        resourceResponse.setCityId(resourceModel.getCityId());
        resourceResponse.setState(resourceModel.getState());
        resourceResponse.setStateId(resourceModel.getStateId());
        resourceResponse.setAvailable(resourceModel.isAvailable());
        resourceResponse.setCreatedBy(resourceModel.getCreatedBy());
        resourceResponse.setCreatedAt(resourceModel.getCreatedAt());
        resourceResponse.setUpdatedBy(resourceModel.getUpdatedBy());
        resourceResponse.setUpdatedAt(resourceModel.getUpdatedAt());
        resourceResponse.setVerified(resourceModel.isVerified());
        return resourceResponse;
    }

}
