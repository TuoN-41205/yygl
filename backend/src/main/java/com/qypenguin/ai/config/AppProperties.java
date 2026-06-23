package com.qypenguin.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private final Ai ai = new Ai();
  private final Policy policy = new Policy();

  public Ai getAi() {
    return ai;
  }

  public Policy getPolicy() {
    return policy;
  }

  public static class Ai {
    private boolean enabled;
    private String baseUrl;
    private String apiKey;
    private String model;
    private String chatPath;
    private int timeoutSeconds = 30;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    public String getApiKey() {
      return apiKey;
    }

    public void setApiKey(String apiKey) {
      this.apiKey = apiKey;
    }

    public String getModel() {
      return model;
    }

    public void setModel(String model) {
      this.model = model;
    }

    public String getChatPath() {
      return chatPath;
    }

    public void setChatPath(String chatPath) {
      this.chatPath = chatPath;
    }

    public int getTimeoutSeconds() {
      return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
      this.timeoutSeconds = timeoutSeconds;
    }
  }

  public static class Policy {
    private boolean highRiskEnabled = true;

    public boolean isHighRiskEnabled() {
      return highRiskEnabled;
    }

    public void setHighRiskEnabled(boolean highRiskEnabled) {
      this.highRiskEnabled = highRiskEnabled;
    }
  }
}
