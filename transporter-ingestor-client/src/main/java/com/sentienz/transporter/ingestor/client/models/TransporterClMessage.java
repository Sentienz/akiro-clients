package com.sentienz.transporter.ingestor.client.models;

public class TransporterClMessage {

  private String protocolVersion = "d1.0";

  private String messageId;

  private String sourceId;

  private String destinationId;

  // only for version d1.0
  private String appName = "";

  private String payload;

  private MessageType messageType;

  private Long expiryTime;

  private Long createdAt;

  private int qos;

  private Boolean isRetain = false;

  public TransporterClMessage(String messageId, String destinationId, Long expiryTime,
      MessageType messageType, QOS qos, String appName, String payload) {
    this.messageId = messageId;
    this.destinationId = destinationId;
    this.expiryTime = expiryTime;
    this.messageType = messageType;
    this.payload = payload;
    this.qos = qos.getValue();
    this.appName = appName;
  }

  public String getProtocolVersion() {
    return protocolVersion;
  }

  public void setProtocolVersion(String protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public Long getExpiryTime() {
    return expiryTime;
  }

  public void setExpiryTime(Long expiryTime) {
    this.expiryTime = expiryTime;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public String getDestinationId() {
    return destinationId;
  }

  public void setDestinationId(String destinationId) {
    this.destinationId = destinationId;
  }

  public int getQos() {
    return qos;
  }

  public void setQos(int qos) {
    this.qos = qos;
  }

  public Boolean getIsRetain() {
    return isRetain;
  }

  public void setIsRetain(Boolean isRetain) {
    this.isRetain = isRetain;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

}
