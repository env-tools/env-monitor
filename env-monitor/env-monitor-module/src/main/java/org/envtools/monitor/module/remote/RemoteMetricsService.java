package org.envtools.monitor.module.remote;

import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationXml;
import org.envtools.monitor.provider.applications.configurable.model.LinkBasedVersionLookupXml;
import org.envtools.monitor.provider.applications.configurable.model.TagBasedProcessLookupXml;

import java.util.Optional;

/**
 * Created by michal on 08/07/16.
 */
public interface RemoteMetricsService {
    Optional<ApplicationStatus> getProcessStatus(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);

    Optional<String> getApplicationVersion(VersionedApplicationXml application, LinkBasedVersionLookupXml versionLookup);

    Optional<Double> getProcessMemoryInMb(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);
}
