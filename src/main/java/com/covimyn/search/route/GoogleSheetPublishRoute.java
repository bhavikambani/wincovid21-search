/**
 * Created by Avishek Gurung on 2021-05-06
 */

package com.covimyn.search.route;

import com.covimyn.search.processors.GoogleSheetPublishProcessor;
import org.apache.camel.Predicate;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ValueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleSheetPublishRoute extends RouteBuilder {

    private static final String ROUTE = GoogleSheetPublishRoute.class.getSimpleName();
    private static final String ENABLED = "enabled";

    @Value("${google.sheet.publish.enable}")
    private Boolean enabled;

    @Value("${google.sheet.publish.interval.ms}")
    private int interval;

    @Autowired
    private GoogleSheetPublishProcessor googleSheetPublishProcessor;

    @Override
    public void configure() throws Exception {

        from("timer://" + ROUTE + "?fixedRate=true&period=" + interval)
                .setHeader(ENABLED, constant(enabled))
                //.log("GoogleSheetPublish Route is " + enabled)
                .choice()
                .when(header(ENABLED).isEqualTo(Boolean.TRUE))
                    .process(googleSheetPublishProcessor);
    }
}
