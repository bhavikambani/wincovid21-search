/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.dao.ResourceDao;
import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.pojo.Pair;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class ResourceServiceImpl implements ResourceService {

    private ResourceDao resourceDao;

    @Override
    public String upsert (ResourceRequest resourceRequest) throws IOException  {
        ResourceModel resourceModel = transformResourceRequestToResourceModel(resourceRequest);
        return resourceDao.upsert(resourceModel);
    }

    @Override
    public List<ResourceResponse> search(String id, String state, String city, String resourceType, String isVerified,
                           Integer offset, Integer rows, String sortOrder) throws Exception{
        List<ResourceResponse> resourceResponses = new ArrayList<>();
        List<Pair> must = new ArrayList<>();
        if(id != null) {
            must.add(new Pair("id", id));
        }

        if(state != null) {
            must.add(new Pair("state", state));
        }

        if(city != null) {
            must.add(new Pair("city", city));
        }

        if(resourceType != null) {
            must.add(new Pair("resourceType", resourceType));
        }

        if(isVerified != null) {
            must.add(new Pair("verified", true));
        }

        List<ResourceModel> resourceModels = resourceDao.searchByLatest(must, new ArrayList<Pair>(), offset,
                rows);

        for(ResourceModel resourceModel : resourceModels) {
                       resourceResponses.add(transformResourceModelToResourceResponse(resourceModel));
        }

        return resourceResponses;
    }

    /**
     * A transformation logic.
     * @param resourceRequest
     * @return
     */
    private ResourceModel transformResourceRequestToResourceModel(ResourceRequest resourceRequest) {
        ResourceModel resourceModel = new ResourceModel();
        String resourceId = resourceRequest.getId() + "_" + resourceRequest.getResourceType();
        resourceId = resourceId.replaceAll("[^a-zA-Z0-9]+", "");
        resourceModel.setId(resourceId);
        resourceModel.setName(resourceRequest.getName());
        resourceModel.setComment(resourceRequest.getComment());
        resourceModel.setAddress(resourceRequest.getAddress());
        resourceModel.setDistrict(resourceRequest.getDistrict());
        resourceModel.setEmail(resourceRequest.getEmail());
        resourceModel.setPincode(resourceRequest.getPincode());
        resourceModel.setCity(resourceRequest.getCity());
        resourceModel.setState(resourceRequest.getState());
        resourceModel.setPhone(resourceRequest.getPhone());
        resourceModel.setLocation(resourceRequest.getLocation());
        resourceModel.setVerified(resourceRequest.isVerified());
        resourceModel.setCreatedAt(resourceRequest.getCreatedAt());
        resourceModel.setCreatedBy(resourceRequest.getCreatedBy());
        resourceModel.setUpdatedAt(resourceRequest.getUpdatedAt());
        resourceModel.setUpdatedBy(resourceRequest.getUpdatedBy());
        resourceModel.setResourceType(resourceRequest.getResourceType());
        resourceModel.setAvailable(resourceRequest.isAvailable());
        resourceModel.setFeedback(resourceRequest.getFeedback());
        return resourceModel;
    }

    private ResourceResponse transformResourceModelToResourceResponse(ResourceModel resourceModel) {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setId(resourceModel.getId());
        resourceResponse.setName(resourceModel.getName());
        resourceResponse.setComment(resourceModel.getComment());
        resourceResponse.setAddress(resourceModel.getAddress());
        resourceResponse.setDistrict(resourceModel.getDistrict());
        resourceResponse.setEmail(resourceModel.getEmail());
        resourceResponse.setPincode(resourceModel.getPincode());
        resourceResponse.setCity(resourceModel.getCity());
        resourceResponse.setState(resourceModel.getState());
        resourceResponse.setPhone(resourceModel.getPhone());
        resourceResponse.setLocation(resourceModel.getLocation());
        resourceResponse.setVerified(resourceModel.isVerified());
        resourceResponse.setResourceType(resourceModel.getResourceType());
        resourceResponse.setAvailable(resourceModel.isAvailable());
        resourceResponse.setFeedback(resourceModel.getFeedback());
        resourceResponse.setCreatedAt(resourceModel.getCreatedAt());
        resourceResponse.setCreatedBy(resourceModel.getCreatedBy());
        resourceResponse.setUpdatedAt(resourceModel.getUpdatedAt());
        resourceResponse.setCreatedBy(resourceModel.getCreatedBy());
        return resourceResponse;
    }

}
