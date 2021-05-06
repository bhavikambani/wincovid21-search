/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.dao;

import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.pojo.Pair;
import com.covimyn.search.pojo.RangeEntity;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface ResourceDao {

    String upsert(ResourceModel resourceModel) throws IOException;
    List<ResourceModel> searchByLatest(List<Pair> must, List<Pair> should, List<Pair> sortOrder, int page, int size,
                                       RangeEntity rangeEntity) throws Exception;
    long count(List<Pair> must, List<Pair> should) throws Exception;
}
