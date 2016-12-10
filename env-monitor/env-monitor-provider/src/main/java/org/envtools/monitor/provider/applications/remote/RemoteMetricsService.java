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

    // Get Process Status

    Optional<ApplicationStatus> getProcessStatus(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);

    void getProcessStatusUsingSshBatch(
            SshBatch sshBatch, Consumer<Optional<ApplicationStatus>> resultHandler,
            VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);

    // Get Application Version using link target

    Optional<String> getApplicationVersion(VersionedApplicationXml application, LinkBasedVersionLookupXml versionLookup);

    void getApplicationVersionUsingSshBatch(
            SshBatch sshBatch, Consumer<Optional<String>> resultHandler,
            VersionedApplicationXml application, LinkBasedVersionLookupXml versionLookup);

    //Not yet used
    Optional<String> getApplicationVersion(VersionedApplicationXml application, ScriptBasedVersionLookupXml versionLookup);

    // Get Process Memory

    Optional<Double> getProcessMemoryInMb(VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);

    void getProcessMemoryInMbUsingSshBatch(SshBatch sshBatch, Consumer<Optional<Double>> resultHandler,
                                           VersionedApplicationXml application, TagBasedProcessLookupXml tagBasedProcessLookup);

}

