package org.envtools.monitor.model.applications;

import org.envtools.monitor.model.common.AbstractNamedIdentifiable;

import java.util.List;

/**
 * Created: 11/20/15 11:43 PM
 *
 * @author Yury Yakovlev
 */
public class Environment extends AbstractNamedIdentifiable<String> {

    private List<Application> applications;

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
