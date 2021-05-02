/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.controller.ResourceController;
import com.covimyn.search.dao.ResourceDao;
import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.pojo.Pair;
import com.covimyn.search.utility.Constant;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ResourceServiceImpl implements ResourceService {
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private ResourceDao resourceDao;

    @Override
    public String upsert (ResourceRequest resourceRequest) throws IOException  {
        ResourceModel resourceModel = transformResourceRequestToResourceModel(resourceRequest);
        return resourceDao.upsert(resourceModel);
    }

    @Override
    public List<ResourceResponse> search(String id, String state, String city, String category,String subcategory ,String isVerified,
                                         Integer offset, Integer rows, String sortOrder) throws Exception{
        List<ResourceResponse> resourceResponses;
        List<Pair> must = new ArrayList<>();
        if(id != null) {
            must.add(new Pair(Constant.ID, id));
        }

        if(state != null) {
            must.add(new Pair(Constant.STATE, state));
        }

        if(city != null) {
            must.add(new Pair(Constant.CITY, city));
        }

        if(category != null) {
            must.add(new Pair(Constant.CATEGORY, category));
        }

        if(!Objects.isNull(subcategory)){
            must.add(new Pair(Constant.SUBCATEGORY, subcategory));
        }

        if(isVerified != null) {
            must.add(new Pair(Constant.VERIFIED, Boolean.parseBoolean(isVerified)));
        }

        if(Objects.isNull(offset)){
            offset = 0;
        }

        if(Objects.isNull(rows)){
            rows = 10;
        }


        List<ResourceModel> resourceModels = resourceDao.searchByLatest(must, new ArrayList<Pair>(), offset,
                rows);

        List<ResourceModel> verifiedResults = resourceModels.stream().filter(ResourceModel::isVerified).collect(Collectors.toList());
        List<ResourceModel> unVerifiedResults = resourceModels.stream().filter(resourceModel -> !resourceModel.isVerified()).collect(Collectors.toList());

        // add first verified and then unverified results in response
        resourceResponses = verifiedResults.stream().map(this::transformResourceModelToResourceResponse).collect(Collectors.toList());
        unVerifiedResults.stream().map(this::transformResourceModelToResourceResponse).forEach(resourceResponses::add);
        logger.info("Returned result size= "+resourceResponses.size());
        return resourceResponses;
    }

    /**
     * A transformation logic.
     * @param resourceRequest
     * @return
     */
    private ResourceModel transformResourceRequestToResourceModel(ResourceRequest resourceRequest) {
        ResourceModel resourceModel = new ResourceModel();
        resourceModel.setId(resourceRequest.getId());
        resourceModel.setName(resourceRequest.getName());
        resourceModel.setCategory(resourceRequest.getCategory());
        resourceModel.setSubcategory(resourceRequest.getSubcategory());
        resourceModel.setAddress(resourceRequest.getAddress());
        resourceModel.setPincode(resourceRequest.getPincode());
        resourceModel.setDescription(resourceRequest.getDescription());
        resourceModel.setPhone1(resourceRequest.getPhone1());
        resourceModel.setPhone2(resourceRequest.getPhone2());
        resourceModel.setEmail(resourceRequest.getEmail());
        resourceModel.setCity(resourceRequest.getCity());
        resourceModel.setState(resourceRequest.getState());
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
        resourceResponse.setSubcategory(resourceModel.getSubcategory());
        resourceResponse.setAddress(resourceModel.getAddress());
        resourceResponse.setPincode(resourceModel.getPincode());
        resourceResponse.setDescription(resourceModel.getDescription());
        resourceResponse.setPhone1(resourceModel.getPhone1());
        resourceResponse.setPhone2(resourceModel.getPhone2());
        resourceResponse.setEmail(resourceModel.getEmail());
        resourceResponse.setCity(resourceModel.getCity());
        resourceResponse.setState(resourceModel.getState());
        resourceResponse.setAvailable(resourceModel.isAvailable());
        resourceResponse.setCreatedBy(resourceModel.getCreatedBy());
        resourceResponse.setCreatedAt(resourceModel.getCreatedAt());
        resourceResponse.setUpdatedBy(resourceModel.getUpdatedBy());
        resourceResponse.setUpdatedAt(resourceModel.getUpdatedAt());
        resourceResponse.setVerified(resourceModel.isVerified());
        return resourceResponse;
    }

}
