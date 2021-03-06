package com.crawler.core.util;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/8/24.
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static CloseableHttpClient httpClient;
    private static CookieStore cookieStore = new BasicCookieStore();
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
    private static HttpHost proxy;
    private static RequestConfig requestConfig;

    static {
        init();
    }

    private static void init() {
        try {
            //配置ssl绕过https验证
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext);

            //指定信任密钥存储对象和连接套接字工厂
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslFactory).build();

            //设置连接管理器
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(Constants.TIMEOUT).setTcpNoDelay(true).build();
            connectionManager.setDefaultSocketConfig(socketConfig);


            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setCharset(Consts.UTF_8).build();
            connectionManager.setDefaultConnectionConfig(connectionConfig);
            connectionManager.setMaxTotal(500);
            connectionManager.setDefaultMaxPerRoute(300);

            //Http请求出错后的重试的处理
            HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount > 2) {
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {
                        return true;
                    }
                    if (exception instanceof ConnectTimeoutException) {
                        return true;
                    }
                    if (exception instanceof UnknownHostException) {
                        return true;
                    }
                    if (exception instanceof SSLException) {
                        return true;
                    }
                    HttpRequest request = HttpClientContext.adapt(context).getRequest();
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        return true;
                    }
                    return false;
                }
            };

            HttpClientBuilder httpClientBuilder = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setRetryHandler(retryHandler)
                    .setDefaultCookieStore(new BasicCookieStore()).setUserAgent(userAgent);
            if (proxy != null) {
                httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy)).build();
            }
            httpClient = httpClientBuilder.build();

            requestConfig = RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT).
                            setConnectTimeout(Constants.TIMEOUT).
                            setConnectionRequestTimeout(Constants.TIMEOUT).
                            setCookieSpec(CookieSpecs.STANDARD).build();


        } catch (Exception e) {
            logger.error("初始化httpClient失败", e);
        }
    }
    public static String getWebPage(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        return getWebPage(get,"utf-8");
    }
    public static String getWebPage(HttpRequestBase request) throws IOException {
        return getWebPage(request, "utf-8");
    }
    public static String postRequest(String postUrl, Map<String, String> params) throws IOException {
        HttpPost httpPost = new HttpPost(postUrl);
        setHttpPostParams(httpPost,params);
        return getWebPage(httpPost,"utf-8");
    }

    /**
     *
     * @param request
     * @param encoding 字符编码
     * @return 网页内容
     * @throws IOException
     */
    public static String getWebPage(HttpRequestBase request,String encoding)throws IOException{
        CloseableHttpResponse response = null;
        response = getResponse(request);
        String content = null;
        if(response.getStatusLine().getStatusCode() != 200){
            System.out.println(response.getStatusLine().getStatusCode());
        }
        if(response.getStatusLine().getStatusCode() == 200){
            content = EntityUtils.toString(response.getEntity(), "gbk");
            request.releaseConnection();
        }
        return content;
    }

    public static CloseableHttpResponse getResponse(HttpRequestBase request)throws IOException{
        if(request.getConfig() == null){
            request.setConfig(requestConfig);
        }
        request.setHeader("User-Agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)]);
        HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setCookieStore(cookieStore);
        CloseableHttpResponse response = httpClient.execute(request, httpClientContext);
        return response;
    }

    public static CloseableHttpResponse getResponse(String url)throws IOException{
        HttpGet httpGet = new HttpGet(url);
        return getResponse(httpGet);
    }

    /**
     * 设置request请求参数
     * @param request
     * @param params
     */
    public static void setHttpPostParams(HttpPost request, Map<String,String> params){
        List<NameValuePair> formParams = new ArrayList<>();
        for(String key:params.keySet()){
            formParams.add(new BasicNameValuePair(key,params.get(key)));
        }
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formParams,"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        request.setEntity(entity);
    }

    /**
     * 下载图片
     * @param fileURL 文件地址
     * @param path 保存路径
     * @param saveFileName 文件名，包括后缀名
     * @param isReplaceFile 若存在文件时，是否还需要下载文件
     */
    public static void downloadFile(String fileURL
            , String path
            , String saveFileName
            , Boolean isReplaceFile){
        try {
            CloseableHttpResponse response = getResponse(fileURL);
            logger.info("status:" + response.getStatusLine().getStatusCode());
            File file =new File(path);
            //如果文件夹不存在则创建
            if  (!file .exists()  && !file .isDirectory()){
                file.mkdirs();
            } else{
                logger.info("//目录存在");
            }
            file = new File(path + saveFileName);
            if(!file.exists() || isReplaceFile){
                //如果文件不存在，则下载
                try {
                    OutputStream os = new FileOutputStream(file);
                    InputStream is = response.getEntity().getContent();
                    byte[] buff = new byte[(int) response.getEntity().getContentLength()];
                    while(true) {
                        int readed = is.read(buff);
                        if(readed == -1) {
                            break;
                        }
                        byte[] temp = new byte[readed];
                        System.arraycopy(buff, 0, temp, 0, readed);
                        os.write(temp);
                        logger.info("文件下载中....");
                    }
                    is.close();
                    os.close();
                    logger.info(fileURL + "--文件成功下载至" + path + saveFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                logger.info(path);
                logger.info("该文件存在！");
            }
            response.close();

        }catch(IllegalArgumentException e){
            logger.info("连接超时...");
        } catch(Exception e1){
            e1.printStackTrace();
        }

    }
}
