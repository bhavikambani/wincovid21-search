/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.utility;


import com.covimyn.search.pojo.ApiHelperResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@AllArgsConstructor
public class HttpHelper {

    ObjectMapper objectMapper;

    public ApiHelperResponse makeHttpPostRequest(String url, Object payload) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(objectMapper.writeValueAsString(payload));
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        ApiHelperResponse apiHelperResponse = new ApiHelperResponse(response.getStatusLine().getStatusCode(),
                response.getStatusLine().getReasonPhrase(),
                EntityUtils.toString(response.getEntity()));
        return apiHelperResponse;
    }
}
