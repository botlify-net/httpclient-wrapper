# HttpClient Wrapper

This wrapper allow you to create a simple HTTP client.
This client try to simulate a navigator. It can be used to send HTTP requests and receive HTTP responses.

## Install the package

You can run this following command to install the package locally:
```bash
# Install the package
make install
# Install the package without the tests
make finstall
```

## Configure proxy

Your client can be used with [brightData](brightdata.com) proxy.
If you want to use this proxy, you have to configure it.

```java
System.setProperty("httpclient.wrapper.proxy.bright-data.host", "YOUR_HOST");
System.setProperty("httpclient.wrapper.proxy.bright-data.port", "YOUR_PORT");
System.setProperty("httpclient.wrapper.proxy.bright-data.username", "YOUR_USERNAME");
System.setProperty("httpclient.wrapper.proxy.bright-data.password", "YOUR_PASSWORD");
```