/**
 * Created by Avishek Gurung on 2021-05-06
 */

package com.covimyn.search.processors;

import com.covimyn.search.pojo.Pair;
import com.covimyn.search.utility.DateUtil;
import com.covimyn.search.services.ResourceService;
import com.covimyn.search.utility.Constant;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleSheetPublishProcessor implements Processor {

    @Autowired
    @Value("${google.sheet.publish.enable}")
    private Boolean enabled;

    @Value("${google.sheet.publish.window.hour}")
    private long hourlyWindowToQuery;

    @Value("${google.sheet.id}")
    private String sheetId;

    @Value("${google.sheet.name}")
    private String sheetName;

    @Autowired
    ResourceService resourceService;

    @Autowired
    private DateUtil dateUtil;

    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetPublishProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("GoogleSheetPublishProcessor running");
        String date = dateUtil.manipulateDate(hourlyWindowToQuery, true);
        List<Pair> must = new ArrayList<>();
        must.add(new Pair(Constant.VERIFIED, Boolean.TRUE));
        resourceService.upload(must, date, sheetId, sheetName);
    }
}
