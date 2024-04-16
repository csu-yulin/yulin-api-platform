package csu.yulin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import csu.yulin.client.AbstractRequest;
import csu.yulin.client.AbstractResponse;

/**
 * @author 刘飘
 */
public class DeserializationUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T extends AbstractResponse> T deserialize(AbstractRequest<T> request, String json) {
        if (json == null || json.isEmpty() || json.contains("null") || request == null) {
            return null;
        }
        // 判断json中是否包含了":{"字段
        if (json.contains(":{")) {
            String replace = json.replace("\"data\":{", "");
            json = replace.substring(0, replace.length() - 1);
        }
        try {
            return OBJECT_MAPPER.readValue(json, request.getResponseClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] objectToByteArray(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
