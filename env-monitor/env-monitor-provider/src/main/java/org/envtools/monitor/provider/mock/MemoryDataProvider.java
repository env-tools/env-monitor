package org.envtools.monitor.provider.mock;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Created: 10/31/15 1:53 AM
 *
 * @author Yury Yakovlev
 */
public class MemoryDataProvider {

    private static final Logger LOGGER = Logger.getLogger(MemoryDataProvider.class);

    public Long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();


//        try {
//            MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
//
//            OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(
//                    mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
//
//            long nanoBefore = System.nanoTime();
//            long cpuBefore = osMBean.getProcessCpuTime();
//
//            Thread.sleep(200);
//
//            long cpuAfter = osMBean.getProcessCpuTime();
//            long nanoAfter = System.nanoTime();
//
//            long percent;
//            if (nanoAfter > nanoBefore)
//                percent = ((cpuAfter - cpuBefore) * 100L) /
//                        (nanoAfter - nanoBefore);
//            else percent = 0;
//
//            LOGGER.info("Cpu usage: " + percent + "%");
//
//            return Double.valueOf(percent);
//        } catch (Throwable t) {
//             LOGGER.error("MemoryDataProvider.getFreeMemory - error getting CPU load", t);
//            return 0.0;
//        }
    }
}
