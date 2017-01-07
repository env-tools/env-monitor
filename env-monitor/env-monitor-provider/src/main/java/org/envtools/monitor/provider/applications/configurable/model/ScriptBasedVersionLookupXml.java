package org.envtools.monitor.provider.applications.configurable.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Created: 7/13/16 2:25 AM
 *
 * @author Yury Yakovlev
 */
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class ScriptBasedVersionLookupXml  extends VersionLookupXml {
    private String scriptPath;
    private String scriptParameters;

    public ScriptBasedVersionLookupXml() {
    }

    public ScriptBasedVersionLookupXml(String scriptPath, String scriptParameters) {
        this.scriptPath = scriptPath;
        this.scriptParameters = scriptParameters;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getScriptParameters() {
        return scriptParameters;
    }

    public void setScriptParameters(String scriptParameters) {
        this.scriptParameters = scriptParameters;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("scriptPath", scriptPath).
                append("scriptParameters", scriptParameters).
                toString();
    }
}
