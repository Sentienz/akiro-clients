package com.sentienz.transporter.ingestor.client;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Properties;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.sentienz.transporter.client.utils.JsonUtils;
import com.sentienz.transporter.ingestor.client.models.TransporterClMessage;

public class TransporterIngestorClient {

  private static String AUTH_PREFIX = "Basic %s";
  private static String APPLICATION_JSON = "application/json";
  private static String CONTENT_TYPE = "Content-type";
  private static String AUTHORIZATION = "Authorization";
  private static String INGESTOR_URL_HTTP_TEMPL = "http://%s:%s/transporter/sendMessage";
  private static String INGESTOR_URL_HTTPS_TEMPL = "https://%s:%s/transporter/sendMessage";

  public static final String HTTP = "http";
  public static final String HTTPS = "https";
  public static String INGESTOR_HOST = "host";
  public static String INGESTOR_PORT = "port";
  public static String INGESTOR_PROTOCOL = "ingestorProtocol";

  private static HttpPost httpPost;
  private static CloseableHttpClient client;
  private boolean isClosed = false;

  public TransporterIngestorClient(Properties config) {
    String host = config.getProperty(INGESTOR_HOST);
    String port = config.getProperty(INGESTOR_PORT);
    String ingestorProtocol = config.getProperty(INGESTOR_PROTOCOL);
    String url = "";
    if (ingestorProtocol.equalsIgnoreCase(HTTPS)) {
      url = String.format(INGESTOR_URL_HTTPS_TEMPL, host, port);
    } else if (ingestorProtocol.equalsIgnoreCase(HTTP)) {
      url = String.format(INGESTOR_URL_HTTP_TEMPL, host, port);
    } else {
      System.err.println("Unknown Protocol");
      System.exit(-1);
    }

    httpPost = new HttpPost(url);
    client = HttpClientBuilder.create().build();

  }

  /**
   * API to send the message to the Transporter Ingestor service.
   * 
   * @param transporterMessage The transporter message object
   * @param apiKey
   * @param apiPass
   * @return
   */
  public boolean postMessage(TransporterClMessage transporterMessage, String apiKey,
      String apiPass) {
    
    if(isClosed) {
      throw new IllegalArgumentException("Client already closed.");
    }

    String creds = apiKey + ":" + apiPass;
    Encoder encoder = Base64.getEncoder();
    String encodedString = encoder.encodeToString(creds.getBytes());
    String auth = String.format(AUTH_PREFIX, encodedString);
    
    httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
    httpPost.setHeader(AUTHORIZATION, auth);
    try {
      StringEntity stringEntity = new StringEntity(JsonUtils.toJson(transporterMessage));
      httpPost.getRequestLine();
      httpPost.setEntity(stringEntity);
      client.execute(httpPost);
      return true;
    } catch (Exception e) {
      System.err.println("Error while sending the message: " + e);
      return false;
    }
  }

  public void close() throws IOException {
    isClosed = true;
    client.close();
  }

}
