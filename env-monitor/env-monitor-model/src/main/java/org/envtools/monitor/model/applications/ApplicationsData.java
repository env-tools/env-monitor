package org.envtools.monitor.model.applications;

import java.io.Serializable;
import java.util.List;

/**
 * Created: 10/31/15 1:38 AM
 *
 * @author Yury Yakovlev
 *         <p>
 *         This is model object for Applications module
 */
public class ApplicationsData implements Serializable {
    private List<Platform> platforms;

    public ApplicationsData() {
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }
}
