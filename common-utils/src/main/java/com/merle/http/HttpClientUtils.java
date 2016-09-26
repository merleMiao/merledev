package com.merle.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.*;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CodingErrorAction;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by merle on 2016/8/25.
 */
public class HttpClientUtils {
    private static final int connectTimeout = 5000;
    private static final int socketTimeout = 30 * 1000;
    private static final int requestTimeout = 30 * 1000;
    private static final int tryTimes = 1;
    private int times = 0;

    private static PoolingHttpClientConnectionManager cm = null;
    private static HttpParams params = new BasicHttpParams();
    private static Map<String, String> commHeader;

    private static HttpClient getHttpClient() {
        HttpClient httpclient = null;
        try {
            HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
            HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory();
            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                    requestWriterFactory, responseParserFactory);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
                    .build();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry, connFactory);

            SocketConfig socketConfig = SocketConfig.custom()
                    .setTcpNoDelay(true)
                    .build();
            connManager.setDefaultSocketConfig(socketConfig);
            connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
            // Validate connections after 1 sec of inactivity
            connManager.setValidateAfterInactivity(1000);

            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom()
                    .setMaxHeaderCount(200)
                    .setMaxLineLength(2000)
                    .build();
            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints)
                    .build();
            // Configure the connection manager to use connection configuration either
            // by default or for a specific host.
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);

            // Configure total max or per route limits for persistent connections
            // that can be kept in the pool or leased by the connection manager.
            connManager.setMaxTotal(100);
            connManager.setDefaultMaxPerRoute(10);
            connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);
            // Use custom cookie store if necessary.
            CookieStore cookieStore = new BasicCookieStore();
            // Use custom credentials provider if necessary.
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            // Create global request configuration
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeout)
                    .setConnectionRequestTimeout(requestTimeout)
                    .setCookieSpec(CookieSpecs.DEFAULT)
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                    .build();

            // Create an HttpClient with the given custom dependencies and configuration.
            httpclient = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultCookieStore(cookieStore)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpclient;
    }

    public static String getString(String url) {
        HttpClientUtils client = new HttpClientUtils();
        Map<String, String> header = new HashMap<String, String>();
//        header.put("Accept-Language", "en-US,en;q=0.5");
        header.put("Accept-Language", "zh-CN,zh;q=0.8");
        HttpObject res = client.getHttpObject(url, header, null);
        return res.getHtml();
    }

    HttpObject getHttpObject(String url, Map<String, String> header, String toCode) {
        HttpObject res = get(url, header, toCode);
        while ((this.times < tryTimes) && (res.getCode() > 300)) {
            res = get(url, header, toCode);
            this.times += 1;
        }
        return res;
    }

    private HttpObject get(String url, Map<String, String> header, String toCode) {
        HttpObject result = new HttpObject();
        byte[] data = null;
        HttpClient httpclient = getHttpClient();
        try {
            HttpGet httpget = new HttpGet(url);
            if ((header == null) || (header.size() == 0)) {
                header = commHeader;
            } else {
                Iterator iter = header.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    httpget.setHeader(key, val);
                }
            }
            HttpResponse response = null;
            response = httpclient.execute(httpget);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                int code = response.getStatusLine().getStatusCode();
                result.setCode(code);
                Header contentType = httpEntity.getContentType();
                if (contentType != null) {
                    String encode = null;
                    encode = contentType.getValue();
                    String key = "charset=";
                    int len = key.length();
                    if (encode.contains("charset="))
                        encode = encode.substring(encode.indexOf(key) + len);
                    else {
                        encode = null;
                    }
                    if (encode != null) {
                        toCode = encode;
                    }
                }
                result.setCharSet(toCode);
                data = EntityUtils.toByteArray(httpEntity);
                httpEntity.consumeContent();
                result.setData(data);
                Header[] head = response.getAllHeaders();
                for (Header h : head) {
                    result.getHeader().put(h.getName(), h.getValue());
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return result;
    }

    public static String postString(String url, Map<String, Object> map) {
        HttpClientUtils client = new HttpClientUtils();
        Map<String, String> header = new HashMap<String, String>();
//        header.put("Accept-Language", "en-US,en;q=0.5");
        header.put("Accept-Language", "zh-CN,zh;q=0.8");
        HttpObject res = client.postObject(url, map, header, "utf-8");
        return res.getHtml();
    }

    private HttpObject postObject(String url, Map<String, Object> map, Map<String, String> header, String encode) {
        HttpObject res = post(url, map, header, encode);
        while ((this.times < tryTimes) && (res.getCode() > 300)) {
            res = post(url, map, header, encode);
            this.times += 1;
        }
        return res;
    }

    private HttpObject post(String url, Map<String, Object> body, Map<String, String> header, String encode) {
        HttpObject result = new HttpObject();
        byte[] data = null;
        HttpResponse response = null;
        HttpClient httpclient = getHttpClient();
        List nvps = new ArrayList();
        Iterator<Map.Entry<String, Object>> it = body.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) it.next();
            String key = StringUtils.defaultString(entry.getKey());
            Object val = entry.getValue();
            if (StringUtils.isNotBlank(key) && val != null) {
                if (val.getClass().isArray()) {
                    Object[] values = (Object[]) val;
                    for (Object o : values) {
                        String value = String.valueOf(o);
                        NameValuePair nvp = new BasicNameValuePair(key, value);
                        nvps.add(nvp);
                    }
                } else {
                    String value = String.valueOf(val);
                    NameValuePair nvp = new BasicNameValuePair(key, value);
                    nvps.add(nvp);
                }
            }
        }
        HttpPost httppost = new HttpPost(url);
        if ((header == null) || (header.size() == 0)) {
            header = commHeader;
        } else {
            Iterator iter = header.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                httppost.setHeader(key, val);
            }
        }
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nvps, encode));
            response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                int code = response.getStatusLine().getStatusCode();
                result.setCode(code);
                Header contentType = httpEntity.getContentType();
                if (contentType != null) {
                    String _encode = null;
                    _encode = contentType.getValue();
                    String key = "charset=";
                    int len = key.length();
                    if (_encode.contains("charset="))
                        _encode = _encode.substring(_encode.indexOf(key) + len);
                    else {
                        _encode = null;
                    }
                    if (_encode != null) {
                        encode = _encode;
                    }
                }
                result.setCharSet(encode);
                data = EntityUtils.toByteArray(httpEntity);
                httpEntity.consumeContent();
                result.setData(data);
                Header[] head = response.getAllHeaders();
                for (Header h : head) {
                    result.getHeader().put(h.getName(), h.getValue());
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

}
