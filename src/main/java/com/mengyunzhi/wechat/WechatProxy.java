package com.mengyunzhi.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mengyunzhi.wechat.exception.HttpRequestException;
import com.mengyunzhi.wechat.exception.QrCodeCanNotGetException;
import com.mengyunzhi.wechat.vo.MessageTemplateRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 微信代理服务
 */
public class WechatProxy {
    /**
     * 应用名称（在微服务注册时，应该唯一)
     */
    private String appName;

    private String host;

    private int port;

    /**
     * 微信appId
     */
    private String appId;

    /**
     * 默认的二维码生效时间为1分钟
     */
    private int expireSeconds;

    /**
     * 默认回调路径为空
     */
    private String defaultCallbackPath;

    private String baseUri;

    public WechatProxy(String appName, String appId, String host, int port) {
        new WechatProxy(appName, appId, host, port, 10 * 60, "", "/request");
    }

    public WechatProxy(String appName, String appId, String host, int port, int expireSeconds, String defaultCallbackPath, String baseUri) {
        this.appName = appName;
        this.appId = appId;
        this.host = host;
        this.port = port;
        this.expireSeconds = expireSeconds;
        this.defaultCallbackPath = defaultCallbackPath;
        this.baseUri = baseUri.startsWith("/") ? baseUri : "/" + baseUri;
    }

    public String getTmpQrCode(String sceneStr) {
        return this.getTmpQrCode(sceneStr, defaultCallbackPath, expireSeconds);
    }

    public String getTmpQrCode(String sceneStr, String callbackPath) {
        return this.getTmpQrCode(sceneStr, callbackPath, expireSeconds);
    }

    public String getTmpQrCode(String sceneStr, int expireSeconds) {
        return this.getTmpQrCode(sceneStr, defaultCallbackPath, expireSeconds);
    }

    /**
     * 发送模板消息
     *
     * @param messageTemplateRequest 模板消息请求
     */
    public String sendTemplateMessage(MessageTemplateRequest messageTemplateRequest) {
        String fullUrl = "";
        try {
            // Build the request URL
            String requestUrl = getRequestUrl("/getTmpQrCode");
            fullUrl = requestUrl + "?instance=" + encodeParams(appName)
                + "&appId=" + appId;

            // Create a URL object
            URL url = new URL(fullUrl);

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method
            connection.setRequestMethod("POST");

            // Set the Content-Type header to application/json
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable input/output streams for writing and reading data
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Convert messageTemplateRequest to JSON and write it to the request body
            try (OutputStream outputStream = connection.getOutputStream()) {
                String jsonRequest = convertObjectToJson(messageTemplateRequest);
                outputStream.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
            }
            // Get the response code
            int responseCode = connection.getResponseCode();

            // Read the response
            if (200 <= responseCode &&  responseCode < 300) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            } else {
                String message = "HTTP Request Failed with error code: " + responseCode;
                System.out.println(message);
                throw new HttpRequestException(message);
            }
        } catch (IOException e) {
            String message = "An exception occurred while sending a request to: " + fullUrl + " - " + e.getMessage();
            System.out.println(message);
            throw new QrCodeCanNotGetException(message);
        }
    }

    /**
     * 获取临时二维码
     *
     * @param sceneStr      扫码场景值
     * @param callbackPath  扫码后的回调路径
     * @param expireSeconds 临时二维码的过期时间
     * @return 二维码地址
     */
    public String getTmpQrCode(String sceneStr, String callbackPath, int expireSeconds) {
        String fullUrl = "";
        try {
            // Build the request URL
            String requestUrl = "http://" + host + ":" + port + "/request/getTmpQrCode";
            fullUrl = requestUrl + "?scene=" + encodeParams(sceneStr)
                + "&instance=" + encodeParams(appName)
                + "&appId=" + appId
                + "&callbackPath=" + encodeParams(callbackPath)
                + "&expireSeconds=" + expireSeconds;

            // Create a URL object
            URL url = new URL(fullUrl);

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    return reader.lines().collect(Collectors.joining("\n"));
                }
            } else {
                String message = "HTTP Request Failed with error code: " + responseCode;
                System.out.println(message);
                throw new HttpRequestException(message);
            }
        } catch (IOException e) {
            String message = "An exception occurred while sending a request to: " + fullUrl + " - " + e.getMessage();
            System.out.println(message);
            throw new QrCodeCanNotGetException(message);
        }
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private String encodeParams(String param) throws UnsupportedEncodingException {
        return URLEncoder.encode(param, StandardCharsets.UTF_8.toString());
    }

    private String getRequestUrl(String uri) {
        return "http://" + host + ":" + port + this.baseUri + (uri.startsWith("/") ? uri : "/" + uri);
    }
}
