/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.interfaces.ResourceEntryResponse;
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
    ResourceEntryResponse search(String id, Long stateId, Long cityId, Long categoryId, Long subcategoryId, String isVerified, Integer offset, Integer rows, String sortOrder) throws Exception;
}
