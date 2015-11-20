package org.envtools.monitor.model.applications;

import org.envtools.monitor.model.common.AbstractNamedIdentifiable;

import java.util.List;

/**
 * Created: 11/20/15 11:23 PM
 *
 * @author Yury Yakovlev
 */
public class Platform extends AbstractNamedIdentifiable<String>{

    private List<Environment> environments;

    public List<Environment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(List<Environment> environments) {
        this.environments = environments;
    }
}
