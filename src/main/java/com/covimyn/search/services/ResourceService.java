/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.services;

import com.covimyn.search.interfaces.ResourceEntryResponse;
import com.covimyn.search.interfaces.ResourceRequest;
import com.covimyn.search.pojo.Pair;
import com.covimyn.search.utility.UserType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public interface ResourceService {

    String upsert (ResourceRequest resourceRequest) throws IOException;

    ByteArrayInputStream download(List<Pair> mustParams, String queryDate) throws Exception;

    ResourceEntryResponse search(List<Pair> mustParams, List<Pair> shouldParams, Integer offset, Integer rows,
                                 UserType userType) throws Exception;

    void upload(List<Pair> mustParams, String queryDate, String sheetId, String sheetName)
            throws Exception;
}
