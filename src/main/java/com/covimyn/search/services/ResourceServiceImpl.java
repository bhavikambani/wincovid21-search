/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.model.ResourceModel;

import java.util.List;

public class ResourceServiceImpl implements ResourceService {




    @Override
    public String insert(ResourceRequest resourceRequest) {
        ResourceModel resourceModel = transformResourceRequestToResourceModel(resourceRequest);




        return null;
    }


    /**
     * A transformation logic.
     * @param resourceRequest
     * @return
     */
    private ResourceModel transformResourceRequestToResourceModel(ResourceRequest resourceRequest) {
        ResourceModel resourceModel = new ResourceModel();
        String resourceId = resourceModel.getId() + "_" + resourceModel.getResourceType();
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


    @Override
    public List<ResourceResponse> search(List<String> must, List<String> should) {
        return null;
    }
}
