/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.pojo.Pair;

import java.io.IOException;
import java.util.List;

public interface ResourceService {

    String upsert (ResourceRequest resourceRequest) throws IOException;
    List<ResourceResponse> search(List<Pair> must, List<Pair> should, int page, int size, boolean byLatest) throws Exception;

}
