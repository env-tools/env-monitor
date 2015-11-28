package org.envtools.monitor.model.applications;

import org.envtools.monitor.model.common.AbstractNamedIdentifiable;

import java.util.List;

/**
 * Created: 11/20/15 11:47 PM
 *
 * @author Yury Yakovlev
 */
public class Application extends AbstractNamedIdentifiable<String> {

    private ApplicationStatus status;
    private List<Application> hostees;

    public Application() {
    }

    public Application(String id, String name, ApplicationStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public List<Application> getHostees() {
        return hostees;
    }

    public void setHostees(List<Application> hostees) {
        this.hostees = hostees;
    }
}
