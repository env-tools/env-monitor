package org.envtools.monitor.provider.applications.configurable.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Map;

/**
 * Created: 19.12.16 0:35
 *
 * @author Yury Yakovlev
 */
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class WebResourceBasedVersionLookupXml extends VersionLookupXml {

    private String url;
    private Map<String, String> headers;
    private String regexp;

    public WebResourceBasedVersionLookupXml() {
    }

    public WebResourceBasedVersionLookupXml(String url, Map<String, String> headers, String regexp) {
        this.url = url;
        this.headers = headers;
        this.regexp = regexp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("url", url).
                append("headers", headers).
                append("regexp", regexp).
                toString();
    }
}
