package com.jkraken.utils;

import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiSign {

    public static String calculateSignature(String  nonce, String data, String secret, String path) {
        var signature = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + data).getBytes());
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(Base64.decodeBase64(secret.getBytes()), "HmacSHA512"));
            mac.update(path.getBytes());
            signature = new String(Base64.encodeBase64(mac.doFinal(md.digest())));
        } catch(Exception e) {}
        return signature;
    }

    public static RequestEntity<String> getRequest (String url, String path, LocalPropLoader props) {
        return getRequest(url, new HashMap<>(), path, props);
    }

    public static RequestEntity<String> getRequest (String url, Map<String,Object> formData, String path, LocalPropLoader props) {
        var headers = new HttpHeaders();
        long nonce = System.currentTimeMillis();
        var data = formData.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue() + "&").collect(Collectors.joining());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("API-Key", props.getApi());
        headers.add("API-Sign", ApiSign.calculateSignature(nonce +"", data, props.getApiSecret(), path));
        headers.add("User-Agent", "Kraken REST API - 0");
        try {
            return new RequestEntity<String>(data, headers, HttpMethod.POST, new URI(url));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void availableKeys(LocalPropLoader properties) {
        if (!properties.keysFound())
            throw new RuntimeException("Missing auth keys, private API request won't work");
    }
}
