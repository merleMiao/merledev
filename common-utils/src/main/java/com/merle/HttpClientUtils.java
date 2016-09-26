//package com.merle.http;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.*;
//import org.apache.http.Header;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.CookieStore;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.config.AuthSchemes;
//import org.apache.http.client.config.CookieSpecs;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.config.*;
//import org.apache.http.conn.HttpConnectionFactory;
//import org.apache.http.conn.ManagedHttpClientConnection;
//import org.apache.http.conn.routing.HttpRoute;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.entity.ByteArrayEntity;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.*;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.InputStreamBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
//import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
//import org.apache.http.io.HttpMessageParserFactory;
//import org.apache.http.io.HttpMessageWriterFactory;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.util.EntityUtils;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import javax.servlet.http.HttpUtils;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.nio.charset.Charset;
//import java.nio.charset.CodingErrorAction;
//import java.security.SecureRandom;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.*;
//import java.util.Map.Entry;
//
//public class HttpClientUtils {
//    private static final int connectTimeout = 5000;
//    private static final int socketTimeout = 30 * 1000;
//    private static final int requestTimeout = 30 * 1000;
//    private static final int tryTimes = 1;
//    private int times = 0;
//
//    private static PoolingHttpClientConnectionManager cm = null;
//    private static HttpParams params = new BasicHttpParams();
//    private static Map<String, String> commHeader;
//
//    private static HttpClient getHttpClient() {
//        HttpClient httpclient = null;
//        try {
////            SSLContext ctx = SSLContext.getInstance("TLS");
////            X509TrustManager tm = new X509TrustManager() {
////                public X509Certificate[] getAcceptedIssuers() {
////                    return null;
////                }
////
////                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
////                        throws CertificateException {
////                }
////
////                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
////                        throws CertificateException {
////                }
////            };
////            ctx.init(null, new TrustManager[]{tm}, null);
////            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
////            HttpConnectionParams.setConnectionTimeout(params, 300000);
////            HttpConnectionParams.setSoTimeout(params, 1800000);
////            SchemeRegistry schemeRegistry = new SchemeRegistry();
////            schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
////            schemeRegistry.register(new Scheme("https", 443, ssf));
////            cm = new PoolingHttpClientConnectionManager(schemeRegistry);
////            cm.setMaxTotal(200);
////            cm.setDefaultMaxPerRoute(20);
//
//
//            HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
//            HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory();
//            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
//                    requestWriterFactory, responseParserFactory);
//
//
////            SSLContext sslContext = SSLContexts.createSystemDefault();
//            SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//            }}, new SecureRandom());
//
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("http", PlainConnectionSocketFactory.INSTANCE)
//                    .register("https", new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
//                    .build();
//
//            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
//                    socketFactoryRegistry, connFactory);
//
//
//            // Create socket configuration
//            SocketConfig socketConfig = SocketConfig.custom()
//                    .setTcpNoDelay(true)
//                    .build();
//            // Configure the connection manager to use socket configuration either
//            // by default or for a specific host.
//            connManager.setDefaultSocketConfig(socketConfig);
//            connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
//            // Validate connections after 1 sec of inactivity
//            connManager.setValidateAfterInactivity(1000);
//
//            // Create message constraints
//            MessageConstraints messageConstraints = MessageConstraints.custom()
//                    .setMaxHeaderCount(200)
//                    .setMaxLineLength(2000)
//                    .build();
//            // Create connection configuration
//            ConnectionConfig connectionConfig = ConnectionConfig.custom()
//                    .setMalformedInputAction(CodingErrorAction.IGNORE)
//                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
//                    .setCharset(Consts.UTF_8)
//                    .setMessageConstraints(messageConstraints)
//                    .build();
//            // Configure the connection manager to use connection configuration either
//            // by default or for a specific host.
//            connManager.setDefaultConnectionConfig(connectionConfig);
//            connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);
//
//            // Configure total max or per route limits for persistent connections
//            // that can be kept in the pool or leased by the connection manager.
//            connManager.setMaxTotal(100);
//            connManager.setDefaultMaxPerRoute(10);
//            connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);
//            // Use custom cookie store if necessary.
//            CookieStore cookieStore = new BasicCookieStore();
//            // Use custom credentials provider if necessary.
//            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//            // Create global request configuration
//            RequestConfig defaultRequestConfig = RequestConfig.custom()
//                    .setConnectTimeout(connectTimeout)
//                    .setSocketTimeout(socketTimeout)
//                    .setConnectionRequestTimeout(requestTimeout)
//                    .setCookieSpec(CookieSpecs.DEFAULT)
//                    .setExpectContinueEnabled(true)
//                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
//                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
//                    .build();
//
//            // Create an HttpClient with the given custom dependencies and configuration.
//            httpclient = HttpClients.custom()
//                    .setConnectionManager(connManager)
//                    .setDefaultCookieStore(cookieStore)
//                    .setDefaultCredentialsProvider(credentialsProvider)
////                    .setProxy(new HttpHost("myproxy", 8080))
//                    .setDefaultRequestConfig(defaultRequestConfig)
////                    .setSSLHostnameVerifier(new HostnameVerifier() {
////                        @Override
////                        public boolean verify(String s, SSLSession sslSession) {
////                            return true;
////                        }
////                    })
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return httpclient;
//    }
//
//    private HttpObject get(String url, Map<String, String> header, String toCode) {
//        HttpObject result = new HttpObject();
//        byte[] data = null;
//        HttpClient httpclient = getHttpClient();
//        try {
//            HttpGet httpget = new HttpGet(url);
//            if ((header == null) || (header.size() == 0)) {
//                header = commHeader;
//            } else {
//                Iterator iter = header.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Entry entry = (Entry) iter.next();
//                    String key = (String) entry.getKey();
//                    String val = (String) entry.getValue();
//                    httpget.setHeader(key, val);
//                }
//            }
//            HttpResponse response = null;
//            response = httpclient.execute(httpget);
//            HttpEntity httpEntity = response.getEntity();
//            if (httpEntity != null) {
//                int code = response.getStatusLine().getStatusCode();
//                result.setCode(code);
//                Header contentType = httpEntity.getContentType();
//                if (contentType != null) {
//                    String encode = null;
//                    encode = contentType.getValue();
//                    String key = "charset=";
//                    int len = key.length();
//                    if (encode.contains("charset="))
//                        encode = encode.substring(encode.indexOf(key) + len);
//                    else {
//                        encode = null;
//                    }
//                    if (encode != null) {
//                        toCode = encode;
//                    }
//                }
//                result.setCharSet(toCode);
//                data = EntityUtils.toByteArray(httpEntity);
//                httpEntity.consumeContent();
//                result.setData(data);
//                Header[] head = response.getAllHeaders();
//                for (Header h : head) {
//                    result.getHeader().put(h.getName(), h.getValue());
//                }
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            httpclient.getConnectionManager().shutdown();
//        }
//        return result;
//    }
//
//    HttpObject getHttpObject(String url, Map<String, String> header, String toCode) {
//        HttpObject res = get(url, header, toCode);
//        while ((this.times < tryTimes) && (res.getCode() > 300)) {
//            res = get(url, header, toCode);
//            this.times += 1;
//        }
//        return res;
//    }
//
//    public static HttpObject getObject(String url, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, header, encode);
//        return res;
//    }
//
//    public static HttpObject getObject(String url, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, header, null);
//        return res;
//    }
//
//    public static HttpObject getObject(String url, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, null, encode);
//        return res;
//    }
//
//    public static HttpObject getObject(String url) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, null, null);
//        return res;
//    }
//
//    public static String getString(String url, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, header, encode);
//        return res.getHtml();
//    }
//
//    public static String getString(String url, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, header, null);
//        return res.getHtml();
//    }
//
//    public static String getString(String url, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        Map<String, String> header = new HashMap<String, String>();
////        header.put("Accept-Language", "en-US,en;q=0.5");
//        header.put("Accept-Language", "zh-CN,zh;q=0.8");
//        HttpObject res = client.getHttpObject(url, header, encode);
//        return res.getHtml();
//    }
//
//    public static String getString(String url) {
//        HttpClientUtils client = new HttpClientUtils();
//        Map<String, String> header = new HashMap<String, String>();
////        header.put("Accept-Language", "en-US,en;q=0.5");
//        header.put("Accept-Language", "zh-CN,zh;q=0.8");
//        HttpObject res = client.getHttpObject(url, header, null);
//        return res.getHtml();
//    }
//
//    public static byte[] getByte(String url, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, header, encode);
//        return res.getData();
//    }
//
//    public static byte[] getByte(String url, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, header, null);
//        return res.getData();
//    }
//
//    public static byte[] getByte(String url, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, null, encode);
//        return res.getData();
//    }
//
//    public static byte[] getByte(String url) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, null, null);
//        return res.getData();
//    }
//
//    private HttpObject post(String url, Map<String, Object> body, Map<String, String> header, String encode) {
//        HttpObject result = new HttpObject();
//        byte[] data = null;
//        HttpResponse response = null;
//        HttpClient httpclient = getHttpClient();
//        List nvps = new ArrayList();
//        Iterator<Entry<String, Object>> it = body.entrySet().iterator();
//        while (it.hasNext()) {
//            Entry<String, Object> entry = (Entry) it.next();
//            String key = StringUtils.defaultString(entry.getKey());
//            Object val = entry.getValue();
//            if (StringUtils.isNotBlank(key) && val != null) {
//                if (val.getClass().isArray()) {
//                    Object[] values = (Object[]) val;
//                    for (Object o : values) {
//                        String value = String.valueOf(o);
//                        NameValuePair nvp = new BasicNameValuePair(key, value);
//                        nvps.add(nvp);
//                    }
//                } else {
//                    String value = String.valueOf(val);
//                    NameValuePair nvp = new BasicNameValuePair(key, value);
//                    nvps.add(nvp);
//                }
//            }
//        }
//        HttpPost httppost = new HttpPost(url);
//        if ((header == null) || (header.size() == 0)) {
//            header = commHeader;
//        } else {
//            Iterator iter = header.entrySet().iterator();
//            while (iter.hasNext()) {
//                Entry entry = (Entry) iter.next();
//                String key = (String) entry.getKey();
//                String val = (String) entry.getValue();
//                httppost.setHeader(key, val);
//            }
//        }
//        try {
//            httppost.setEntity(new UrlEncodedFormEntity(nvps, encode));
//            response = httpclient.execute(httppost);
//            HttpEntity httpEntity = response.getEntity();
//            if (httpEntity != null) {
//                int code = response.getStatusLine().getStatusCode();
//                result.setCode(code);
//                Header contentType = httpEntity.getContentType();
//                if (contentType != null) {
//                    String _encode = null;
//                    _encode = contentType.getValue();
//                    String key = "charset=";
//                    int len = key.length();
//                    if (_encode.contains("charset="))
//                        _encode = _encode.substring(_encode.indexOf(key) + len);
//                    else {
//                        _encode = null;
//                    }
//                    if (_encode != null) {
//                        encode = _encode;
//                    }
//                }
//                result.setCharSet(encode);
//                data = EntityUtils.toByteArray(httpEntity);
//                httpEntity.consumeContent();
//                result.setData(data);
//                Header[] head = response.getAllHeaders();
//                for (Header h : head) {
//                    result.getHeader().put(h.getName(), h.getValue());
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//        }
//        return result;
//    }
//
//    private HttpObject postObject(String url, Map<String, Object> map, Map<String, String> header, String encode) {
//        HttpObject res = post(url, map, header, encode);
//        while ((this.times < tryTimes) && (res.getCode() > 300)) {
//            res = post(url, map, header, encode);
//            this.times += 1;
//        }
//        return res;
//    }
//
//    public static HttpObject postString(String url, Map<String, Object> map, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, map, header, encode);
//        return res;
//    }
//
//    public static HttpObject postString(String url, Map<String, Object> map, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, map, header, "utf-8");
//        return res;
//    }
//
//    public static String postString(String url, Map<String, Object> map) {
//        HttpClientUtils client = new HttpClientUtils();
//        Map<String, String> header = new HashMap<String, String>();
////        header.put("Accept-Language", "en-US,en;q=0.5");
//        header.put("Accept-Language", "zh-CN,zh;q=0.8");
//        HttpObject res = client.postObject(url, map, header, "utf-8");
//        return res.getHtml();
//    }
//
//    public static byte[] postByte(String url, Map<String, Object> map, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, map, header, encode);
//        return res.getData();
//    }
//
//    public static byte[] postByte(String url, Map<String, Object> map, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, map, header, "utf-8");
//        return res.getData();
//    }
//
//    public static byte[] postByte(String url, Map<String, Object> map) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, map, null, "utf-8");
//        return res.getData();
//    }
//
//
//    //------------------------------------------------------------------------------------------------------------------
//
//    private HttpObject post(String url, String query, Map<String, String> header, String encode) {
//        HttpObject result = new HttpObject();
//        byte[] data = null;
//        HttpResponse response = null;
//        HttpClient httpclient = getHttpClient();
//        HttpPost httppost = new HttpPost(url);
//        if ((header == null) || (header.size() == 0)) {
//            header = commHeader;
//        } else {
//            Iterator iter = header.entrySet().iterator();
//            while (iter.hasNext()) {
//                Entry entry = (Entry) iter.next();
//                String key = (String) entry.getKey();
//                String val = (String) entry.getValue();
//                httppost.setHeader(key, val);
//            }
//        }
//        try {
//
//            StringEntity entity = new StringEntity(StringUtils.defaultString(query),Consts.UTF_8);
//            httppost.setEntity(entity);
//            response = httpclient.execute(httppost);
//            HttpEntity httpEntity = response.getEntity();
//            if (httpEntity != null) {
//                int code = response.getStatusLine().getStatusCode();
//                result.setCode(code);
//                Header contentType = httpEntity.getContentType();
//                if (contentType != null) {
//                    String _encode = null;
//                    _encode = contentType.getValue();
//                    String key = "charset=";
//                    int len = key.length();
//                    if (_encode.contains("charset="))
//                        _encode = _encode.substring(_encode.indexOf(key) + len);
//                    else {
//                        _encode = null;
//                    }
//                    if (_encode != null) {
//                        encode = _encode;
//                    }
//                }
//                result.setCharSet(encode);
//                data = EntityUtils.toByteArray(httpEntity);
//                httpEntity.consumeContent();
//                result.setData(data);
//                Header[] head = response.getAllHeaders();
//                for (Header h : head) {
//                    result.getHeader().put(h.getName(), h.getValue());
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//        }
//        return result;
//    }
//
//    private HttpObject postObject(String url, String query, Map<String, String> header, String encode) {
//        HttpObject res = post(url, query, header, encode);
//        while ((this.times < tryTimes) && (res.getCode() > 300)) {
//            res = post(url, query, header, encode);
//            this.times += 1;
//        }
//        return res;
//    }
//
//    public static HttpObject postString(String url, String query, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, query, header, encode);
//        return res;
//    }
//
//    public static HttpObject postString(String url, String query, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, query, header, "utf-8");
//        return res;
//    }
//
//    public static String postString(String url, String query) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, query, null, "utf-8");
//        return res.getHtml();
//    }
//
//    private HttpObject post(String url, byte[] postData, Map<String, String> header, String encode) {
//        HttpObject result = new HttpObject();
//        byte[] data = null;
//        HttpResponse response = null;
//        HttpClient httpclient = getHttpClient();
//        HttpPost httppost = new HttpPost(url);
//        if ((header == null) || (header.size() == 0)) {
//            header = commHeader;
//        } else {
//            Iterator iter = header.entrySet().iterator();
//            while (iter.hasNext()) {
//                Entry entry = (Entry) iter.next();
//                String key = (String) entry.getKey();
//                String val = (String) entry.getValue();
//                httppost.setHeader(key, val);
//            }
//        }
//        try {
//            httppost.setEntity(new ByteArrayEntity(postData));
//            response = httpclient.execute(httppost);
//            HttpEntity httpEntity = response.getEntity();
//            if (httpEntity != null) {
//                int code = response.getStatusLine().getStatusCode();
//                result.setCode(code);
//                Header contentType = httpEntity.getContentType();
//                if (contentType != null) {
//                    String _encode = null;
//                    _encode = contentType.getValue();
//                    String key = "charset=";
//                    int len = key.length();
//                    if (_encode.contains("charset="))
//                        _encode = _encode.substring(_encode.indexOf(key) + len);
//                    else {
//                        _encode = null;
//                    }
//                    if (_encode != null) {
//                        encode = _encode;
//                    }
//                }
//                result.setCharSet(encode);
//                data = EntityUtils.toByteArray(httpEntity);
//                httpEntity.consumeContent();
//                result.setData(data);
//                Header[] head = response.getAllHeaders();
//                for (Header h : head) {
//                    result.getHeader().put(h.getName(), h.getValue());
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//        }
//        return result;
//    }
//
//    private HttpObject postObject(String url, byte[] data, Map<String, String> header, String encode) {
//        HttpObject res = post(url, data, header, encode);
//        while ((this.times < tryTimes) && (res.getCode() > 300)) {
//            res = post(url, data, header, encode);
//            this.times += 1;
//        }
//        return res;
//    }
//
//    public static byte[] postByte(String url, byte[] data, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, data, header, encode);
//        return res.getData();
//    }
//
//    public static byte[] postByte(String url, byte[] data, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, data, header, "utf-8");
//        return res.getData();
//    }
//
//    public static byte[] postByte(String url, byte[] data) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postObject(url, data, null, "utf-8");
//        return res.getData();
//    }
//
//    private static HttpObject postFileComm(String url, Map<String, Object> body, Map<String, String> header, String encode) {
//        HttpObject result = new HttpObject();
//        if ((body == null) || (body.size() == 0)) {
//            return result;
//        }
//        byte[] data = null;
//        HttpResponse response = null;
//        HttpClient httpclient = getHttpClient();
//        HttpPost httppost = new HttpPost(url);
//        if ((header == null) || (header.size() == 0)) {
//            header = commHeader;
//        } else {
//            Iterator iter = header.entrySet().iterator();
//            while (iter.hasNext()) {
//                Entry entry = (Entry) iter.next();
//                String key = (String) entry.getKey();
//                String val = (String) entry.getValue();
//                httppost.setHeader(key, val);
//            }
//        }
//        try {
////            MultipartEntity  mu = new MultipartEntity(HttpMultipartMode.STRICT,null,CharsetUtils.get("utf-8"));
//            MultipartEntityBuilder mu = MultipartEntityBuilder.create();
//            mu.setCharset(Consts.UTF_8);//设置请求的编码格式
//            mu.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
//
//            Iterator iter = body.entrySet().iterator();
//            while (iter.hasNext()) {
//                Entry entry = (Entry) iter.next();
//                String key = (String) entry.getKey();
//                Object val = entry.getValue();
//
//                if (val.getClass().isArray()) {
//                    Object[] values = (Object[]) val;
//                    for (Object o : values) {
//                        if ((o instanceof File)) {
//                            FileBody fb = new FileBody((File) o, "utf-8");
//                            mu.addPart(key, fb);
//                        } else if ((o instanceof InputStream)) {
//                            InputStreamBody isb = new InputStreamBody((InputStream) o, key);
//                            mu.addPart(key, isb);
//                        } else if (o.getClass().isPrimitive()) {
//                            StringBody sb = new StringBody(String.valueOf(o), "text/plain", Charset.forName("utf-8"));
//                            mu.addPart(key, sb);
//                        } else {
//                            StringBody sb = new StringBody(String.valueOf(o), "text/plain", Charset.forName("utf-8"));
//                            mu.addPart(key, sb);
//                        }
//                    }
//                } else {
//                    if ((val instanceof File)) {
//                        File file = (File) val;
//                        String name = file.getName();
//
//                        FileBody fb = new FileBody((File) val);
//                        mu.addPart(key, fb);
//                    } else if ((val instanceof InputStream)) {
//                        InputStreamBody isb = new InputStreamBody((InputStream) val, key);
//                        mu.addPart(key, isb);
//                    } else if (val.getClass().isPrimitive()) {
//                        StringBody sb = new StringBody(String.valueOf(val), "text/plain", Charset.forName("utf-8"));
//                        mu.addPart(key, sb);
//                    } else {
//                        StringBody sb = new StringBody(String.valueOf(val), "text/plain", Charset.forName("utf-8"));
//                        mu.addPart(key, sb);
//                    }
//                }
//            }
//            httppost.setEntity(mu.build());
////            if (httppost.getHeaders("Content-Length") != null) {
////                long length = mu.getContentLength();
////                httppost.setHeader("Content-Length", String.valueOf(length));
////            }
//            response = httpclient.execute(httppost);
//            HttpEntity httpEntity = response.getEntity();
//            if (httpEntity != null) {
//                int code = response.getStatusLine().getStatusCode();
//                result.setCode(code);
//                Header contentType = httpEntity.getContentType();
//                if (contentType != null) {
//                    String _encode = null;
//                    _encode = contentType.getValue();
//                    String key = "charset=";
//                    int len = key.length();
//                    if (_encode.contains("charset="))
//                        _encode = _encode.substring(_encode.indexOf(key) + len);
//                    else {
//                        _encode = null;
//                    }
//                    if (_encode != null) {
//                        encode = _encode;
//                    }
//                }
//                result.setCharSet(encode);
//                data = EntityUtils.toByteArray(httpEntity);
//                httpEntity.consumeContent();
//                result.setData(data);
//                Header[] head = response.getAllHeaders();
//                for (Header h : head) {
//                    result.getHeader().put(h.getName(), h.getValue());
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//        }
//        return result;
//    }
//
//    private HttpObject postFileMgr(String url, Map<String, Object> map, Map<String, String> header, String encode) {
//        HttpObject res = postFileComm(url, map, header, encode);
//        while ((this.times < tryTimes) && (res.getCode() > 300)) {
//            res = postFileComm(url, map, header, encode);
//            this.times += 1;
//        }
//        return res;
//    }
//
//    public static HttpObject postFile(String url, Map<String, Object> map, Map<String, String> header, String encode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postFileMgr(url, map, header, encode);
//        return res;
//    }
//
//    public static HttpObject postFile(String url, Map<String, Object> map, Map<String, String> header) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postFileMgr(url, map, header, "utf-8");
//        return res;
//    }
//
//    public static String postFile(String url, Map<String, Object> map) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.postFileMgr(url, map, null, "utf-8");
//        return res.getHtml();
//    }
//
//
//    public static void postFile(File file, String title) {
//        String url = "http://upload.t.qq.com/asyn/uploadpic.php";
//        String cookie = "luin=o0410503562; lskey=00010000b71fe882026ee855b6a9e19b0cdbf46079db91820fed5c2409217e8de5fb76e0e8386c81d556f084; ptui_loginuin=410503562; RK=BQTqFrs2zv; wbilang_410503562=zh_CN; wbilang_10000=zh_CN; ptisp=ctc; wb_regf=%3B0%3Bim%3B%3B0; pt2gguin=o0410503562; uin=o0410503562; skey=@OOUojOxv6; show_id=; mb_reg_from=2; pgv_pvid=3557643809; pgv_info=ssid=s3122993208; o_cookie=410503562";
//        String user_agent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.46 Safari/535.11";
//        String referer = "http://t.qq.com/xeonstar?ptlang=2052";
//        String Origin = "http://t.qq.com";
//
//        HashMap header = new HashMap();
//        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        header.put("User-Agent", user_agent);
//        header.put("Referer", referer);
//        header.put("Connection", "keep-alive");
//        header.put("Cookie ", cookie);
//        header.put("Cache-Control", "max-age=0");
//        header.put("Origin ", Origin);
//        header.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//        header.put("Accept-Language", "zh-CN,zh;q=0.8");
//        header.put("Accept-Encoding", "gzip,deflate,sdch");
//
//        Map body = new HashMap();
//        body.put("pic", file);
//
//        long startTime = System.currentTimeMillis();
//        HttpObject obj = postFile(url, body, header, "utf-8");
//        String result = new String(obj.getData());
//
//
//        url = result.substring(result.indexOf("http://t2.qpic.cn/mblogpic/"));
//        url = url.substring(0, url.indexOf("'"));
//
//
//        long endTime = System.currentTimeMillis();
//        String data = "content=%23ikuer.cn%23%E5%85%B3%E6%B3%A8%E8%BA%AB%E8%BE%B9%E7%9A%84%E7%BE%8E%E5%A5%BD%E4%BA%8B%E7%89%A9&startTime=1344179358382&endTime=1344179397057&countType=&viewModel=&attips=&pic=http%3A%2F%2Ft2.qpic.cn%2Fmblogpic%2F5b634900bd41a7a9245c&source=1720&apiType=4&wqid=null&cIsuse=null&cflag=null&syncQzone=0&syncQQSign=0";
//        data = URLDecoder.decode(data);
//        Hashtable post = new Hashtable();
//        Hashtable<String, String[]> mm = HttpUtils.parseQueryString(data);
//        for (Entry en : mm.entrySet()) {
//            post.put(en.getKey(), en.getValue() == null ? null : ((String[]) en.getValue())[0]);
//        }
//        post.put("pic", url);
//        post.put("content", title);
//        post.put("startTime", String.valueOf(0));
//        post.put("endTime", String.valueOf(endTime));
//
//        referer = "http://api.t.qq.com/proxy.html";
//        String rf = "http://1.t.qq.com/xeonstar";
//
//        header.put("Cookie ", cookie);
//        header.put("rf ", rf);
//        header.put("Referer ", referer);
//
//        url = "http://api.t.qq.com/old/publish.php";
//        postString(url, post, header);
//    }
//
//    public static String tostr(String[] arr) {
//        StringBuffer sb = new StringBuffer();
//        for (String s : arr) {
//            sb.append(s);
//        }
//        return sb.toString();
//    }
//
//    public static void main(String[] args) throws Exception {
//        String result = HttpClientUtils.postString("http://app01.yugusoft.com/fuser/weichat.jsp?signature=f6a671abc5b4de18aa50c07b9ab50f7a5142017d&timestamp=1458872513&nonce=1809159365", "");
//
//    }
//
//    public static String getUrl(String url, String toCode) {
//        HttpClientUtils client = new HttpClientUtils();
//        HttpObject res = client.getHttpObject(url, null, toCode);
//        if (res.getHeader().containsKey("Location")) {
//            url = (String) res.getHeader().get("Location");
//            getUrl(url, toCode);
//        }
//        return url;
//    }
//
////    static {
////        try {
////            SSLContext ctx = SSLContext.getInstance("TLS");
////            X509TrustManager tm = new X509TrustManager() {
////                public X509Certificate[] getAcceptedIssuers() {
////                    return null;
////                }
////
////                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
////                        throws CertificateException {
////                }
////
////                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
////                        throws CertificateException {
////                }
////            };
////            ctx.init(null, new TrustManager[]{tm}, null);
////            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
////            HttpConnectionParams.setConnectionTimeout(params, 300000);
////            HttpConnectionParams.setSoTimeout(params, 1800000);
////            SchemeRegistry schemeRegistry = new SchemeRegistry();
////            schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
////            schemeRegistry.register(new Scheme("https", 443, ssf));
////            cm = new PoolingClientConnectionManager(schemeRegistry);
////            cm.setMaxTotal(200);
////            cm.setDefaultMaxPerRoute(20);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        commHeader = null;
////        commHeader = new HashMap();
////        commHeader.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.16) Gecko/20110319 Firefox/3.6.16 ( .NET CLR 3.5.30729)");
////        commHeader.put("Accept-Encoding", "Accept-Encoding");
////        commHeader.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
////        commHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
////        commHeader.put("Connection", "close");
////        commHeader.put("Accept-Encoding", "gzip, deflate");
////        commHeader.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
////    }
//}