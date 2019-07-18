package com.sentienz.transporter.ingestor.client.models;

public enum QOS {
  ATMOST_ONCE(0), ATLEAST_ONCE(1), EXACTLY_ONCE(2);

  private int value;

  QOS(int value) {
    this.value = value;
  }
  public int getValue() {
    return value;
  }
  
}
