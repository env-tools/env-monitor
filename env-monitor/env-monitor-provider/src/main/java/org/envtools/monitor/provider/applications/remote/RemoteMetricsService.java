package org.envtools.monitor.provider.applications.remote;

import org.envtools.monitor.common.ssh.SshBatch;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.provider.applications.configurable.model.ScriptBasedVersionLookupXml;
import org.envtools.monitor.provider.applications.configurable.model.VersionedApplicationXml;
import org.envtools.monitor.provider.applications.configurable.model.LinkBasedVersionLookupXml;
import org.envtools.monitor.provider.applications.configurable.model.TagBasedProcessLookupXml;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by michal on 08/07/16.
 */
public interface RemoteMetricsService {

    Optional<ApplicationStatus> getProcessStatus(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);

    void getProcessStatusUsingSshBatch(
            SshBatch sshBatch, Consumer<Optional<ApplicationStatus>> resultHandler,
            VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);

    Optional<String> getApplicationVersion(VersionedApplicationXml application, LinkBasedVersionLookupXml versionLookup);

    Optional<String> getApplicationVersion(VersionedApplicationXml application, ScriptBasedVersionLookupXml versionLookup);

    Optional<Double> getProcessMemoryInMb(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);
}
