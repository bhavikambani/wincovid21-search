/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.pojo.Pair;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Service
public interface ResourceService {

    String upsert (ResourceRequest resourceRequest) throws IOException;
    //Response search(List<Pair> must, List<Pair> should, int page, int size, boolean byLatest) throws Exception;
    List<ResourceResponse> search(String id, String state, String city, String resourceType, String isVerified, int offset, int rows, String sortOrder) throws Exception;
}
