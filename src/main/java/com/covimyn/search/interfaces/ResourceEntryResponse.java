package com.covimyn.search.interfaces;

import lombok.Data;

import java.util.List;

@Data
public class ResourceEntryResponse extends AbstractResponse {
        List<ResourceResponse> data;

    public static ResourceEntryResponse of(List<ResourceResponse> responseEntries){
        ResourceEntryResponse response = new ResourceEntryResponse();
        StatusResponse statusResponse = new StatusResponse(StatusResponse.Type.SUCCESS,responseEntries.size());
        response.setStatus(statusResponse);
        response.data = responseEntries;
        return response;
    }

    public static ResourceEntryResponse errorResponseOfException(){
        ResourceEntryResponse response = new ResourceEntryResponse();
        StatusResponse statusResponse = new StatusResponse(StatusResponse.Type.ERROR);
        response.setStatus(statusResponse);
        return response;
    }

}
