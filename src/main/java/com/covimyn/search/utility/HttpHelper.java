/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.utility;


import com.covimyn.search.pojo.ApiHelperResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class HttpHelper {

    private ObjectMapper objectMapper;

    public ApiHelperResponse makeHttpPostRequest(String url, Object payload) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();

            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(payload));
            httpPost.setEntity(entity);
            httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response = client.execute(httpPost);
            ApiHelperResponse apiHelperResponse = new ApiHelperResponse(response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase(),
                    EntityUtils.toString(response.getEntity()));
            return apiHelperResponse;
        }
        finally {
            httpPost.releaseConnection();
            response.close();
        }
    }
}
