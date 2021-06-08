package com.cd.rdf.cache.redis;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

public class JedisConnectionFactoryEx extends JedisConnectionFactory{
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
    if (url!=null) {
      String[] hostAndPort = url.split(":");
      String hostName = hostAndPort[0];
      int port = 6379;
      if (hostAndPort.length>1) {
        port = Integer.parseInt(hostAndPort[1]);
      }
      this.setHostName(hostName);
      this.setPort(port);
    }
  }
  
  
}
