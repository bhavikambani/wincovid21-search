/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;

import java.util.List;

public interface ResourceService {

    String insert(ResourceRequest resourceRequest);

    List<ResourceResponse> search(List<String> must, List<String> should);

}
