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
    private Long freeMemory;

    public ApplicationsData() {
    }

    public Long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(Long freeMemory) {
        this.freeMemory = freeMemory;
    }
}
