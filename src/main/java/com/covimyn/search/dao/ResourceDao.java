/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.dao;

import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.interfaces.ResourceResponse;
import com.covimyn.search.model.ResourceModel;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public interface ResourceDao {

    String upsert(ResourceModel resourceModel) throws IOException;

    List<ResourceModel> search(Map<String, Object> must, Map<String, Object> should,
                               AbstractMap.SimpleEntry<String, String> sortBy) throws IOException;

}
