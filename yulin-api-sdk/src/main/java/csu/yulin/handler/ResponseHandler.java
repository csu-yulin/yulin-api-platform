package csu.yulin.handler;

import csu.yulin.exception.HttpStatusClientException;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

/**
 * @author 刘飘
 */
public class ResponseHandler implements HttpClientResponseHandler<String> {
    @Override
    public String handleResponse(ClassicHttpResponse response) {
        HttpEntity entity = response.getEntity();
        String responseBody = null;
        try {
            responseBody = EntityUtils.toString(entity);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        int statusCode = response.getCode();
        boolean is2xxSuccessful = statusCode >= 200 && statusCode <= 299;
        boolean is3xxSuccessful = statusCode >= 300 && statusCode <= 399;
        if (!(is2xxSuccessful || is3xxSuccessful)) {
            throw new HttpStatusClientException(statusCode, response.getReasonPhrase(), responseBody);
        }

        try {
            EntityUtils.consume(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }
}