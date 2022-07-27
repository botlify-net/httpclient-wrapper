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

You client can be used with [brightData](brightdata.com) proxy.
If you want to use this proxy, you have to configure it.

```java
System.setProperty("httpClientWrapperBrightDataHost", "YOUR_HOST");
System.setProperty("httpClientWrapperBrightDataPort", "YOUR_PORT");
System.setProperty("httpClientWrapperBrightDataUsername", "YOUR_USERNAME");
System.setProperty("httpClientWrapperBrightDataPassword", "YOUR_PASSWORD");
```