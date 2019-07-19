# transporter-clients
Clients to communicate with Sentienz Transporter Platform

# transporter-ingestor-client
Client to send messages to Sentienz Transporter Platform over http/https

## Steps to use ingestor-client
1. Add maven dependency to the ingestor client jar

<dependency>
    <groupId>com.sentienz.transporter</groupId>
   <artifactId>transporter-ingestor-client</artifactId>
    <version>1.0</version>
 </dependency>

2. Configuring the ingestor API credentials

Properties config = new Properties();
config.setProperty(TransporterIngestorClient.INGESTOR_HOST, "localhost");
config.setProperty(TransporterIngestorClient.INGESTOR_PORT, "12082");
config.setProperty(TransporterIngestorClient.INGESTOR_PROTOCOL, TransporterIngestorClient.HTTP);

3. Creating the client and the transporter Message Object

TransporterIngestorClient ingestorClient = new TransporterIngestorClient(config);
 TransporterClMessage transporterMessage =
        new TransporterClMessage(UUID.randomUUID().toString(), "device_id", expTime, MessageType.DIRECT,
            QOS.ATLEAST_ONCE, "sample_app", "message payload");

4. Calling the Post Message Function with the transporterMessage object, API Key and API Password

ingestorClient.postMessage(transporterMessage, "apiKey", "apiPass");


