package net.noyark.b4j.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送api的主类
 */
public class Requester {

    Requester(String field){
        this.field = new StringBuilder(API_WEB).append(field);
    }

    public static final String API_WEB = "http://api.bilibili.cn/";

    private StringBuilder field;

    private Map<String ,String> args = new HashMap<String, String>();

    public Requester append(String key,String value){
        args.put(key, value);
        return this;
    }

    public Requester url(){
        if(args.size()>0){
            field.append("?");
            int count = 0;
            for (Map.Entry<String, String> entry : args.entrySet()) {
                field.append(entry.getKey()).append("=").append(entry.getValue());
                if(count!= args.size()-1){
                    field.append("&");
                }
                count++;
            }
        }
        return this;
    }

    public HttpEntity to() throws IOException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(field.toString());
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)
                .setConnectTimeout(2000)
                .build();
        get.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();
        if(entity!=null){
            return entity;
        }
        httpClient.close();
        response.close();
        return null;
    }

    public String json() throws IOException{
        return EntityUtils.toString(to());
    }

    public JSONObject object() throws IOException{
        return JSON.parseObject(json());
    }

}
