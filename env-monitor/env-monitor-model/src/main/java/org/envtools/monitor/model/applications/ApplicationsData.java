package org.envtools.monitor.model.applications;

import java.io.Serializable;

/**
 * Created: 10/31/15 1:38 AM
 *
 * @author Yury Yakovlev
 *
 * This is model object for Applications module
 */
public class ApplicationsData implements Serializable {
    private Double cpuLoad;

    public ApplicationsData() {
    }

    public Double getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(Double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }
}
