package org.envtools.monitor.provider.applications.remote;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Optional;

/**
 * Created: 22.12.16 0:18
 *
 * @author Yury Yakovlev
 */
public class BasicUrlServiceImpl implements BasicUrlService {

    private static final Logger LOGGER = Logger.getLogger(BasicUrlServiceImpl.class);

    private int readTimeoutMs;
    private int connectTimeoutMs;

    @Override
    public Optional<String> readResource(String url, Map<String, String> headers) {
        try {
            URLConnection urlConnection = new URL(url)
                    .openConnection();

            if (urlConnection instanceof HttpURLConnection) {
                HttpURLConnection connection = (HttpURLConnection) urlConnection;
                connection.setReadTimeout(readTimeoutMs);
                connection.setConnectTimeout(connectTimeoutMs);

                for (Map.Entry<String, String> header : headers.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    InputStream inputStr = connection.getInputStream();
                    String encoding = connection.getContentEncoding() == null ? "UTF-8"
                            : connection.getContentEncoding();
                    String response = IOUtils.toString(inputStr, encoding);
                    return Optional.ofNullable(response);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("BasicUrlServiceImpl.readResource - could not get resource " + url, e);
        }
        return Optional.empty();
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }
}
