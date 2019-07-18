package com.sentienz.transporter.ingestor.client.examples;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sentienz.transporter.ingestor.client.TransporterIngestorClient;
import com.sentienz.transporter.ingestor.client.models.MessageType;
import com.sentienz.transporter.ingestor.client.models.QOS;
import com.sentienz.transporter.ingestor.client.models.TransporterClMessage;

public class IngestorClientExample {

  private static final Logger log = LoggerFactory.getLogger(IngestorClientExample.class);

  public static void main(String[] args) throws IOException {

    // initialization of the client
    Properties config = new Properties();
    config.setProperty(TransporterIngestorClient.INGESTOR_HOST, "localhost");
    config.setProperty(TransporterIngestorClient.INGESTOR_PORT, "12082");
    config.setProperty(TransporterIngestorClient.INGESTOR_PROTOCOL, TransporterIngestorClient.HTTP);
    
    Long expTime = System.currentTimeMillis() + 10000000L;

    TransporterIngestorClient ingestorClient = new TransporterIngestorClient(config);

    // sending a message
    TransporterClMessage transporterMessage =
        new TransporterClMessage(UUID.randomUUID().toString(), "device_id", expTime, MessageType.DIRECT,
            QOS.ATLEAST_ONCE, "sample_app", "message payload");
    boolean isSuccess = ingestorClient.postMessage(transporterMessage, "apiKey", "apiPass");

    if (isSuccess) {
      log.info("Message delivered successfully");
    } else {
      log.error("Message delivery failed");
    }

    ingestorClient.close();
  }
}
