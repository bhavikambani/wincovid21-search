/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.utility;


import com.covimyn.search.pojo.ApiHelperResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpHelper {

    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    private ObjectMapper objectMapper;

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    public HttpHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(300);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);
    }

    public ApiHelperResponse makeHttpPostRequest(String url, Object payload) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient client = HttpClients.custom()
                    .setConnectionManager(poolingHttpClientConnectionManager)
                    .build();

            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(payload));
            httpPost.setEntity(entity);
            httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response = client.execute(httpPost);
            logger.info("HttpHelper response= "+response.toString());
            ApiHelperResponse apiHelperResponse = new ApiHelperResponse(response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase(),
                    EntityUtils.toString(response.getEntity()));
            return apiHelperResponse;
        } catch (Exception e) {
            logger.error("Exception while executing HTTP call", e);
            throw new IOException(e);
        }
    }
}
