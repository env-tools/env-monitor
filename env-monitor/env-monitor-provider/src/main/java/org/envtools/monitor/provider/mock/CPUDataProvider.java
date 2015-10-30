package org.envtools.monitor.provider.mock;

import com.sun.management.OperatingSystemMXBean;
import org.apache.log4j.Logger;

import javax.management.MBeanServerConnection;
import java.lang.management.ManagementFactory;
import java.util.Random;

/**
 * Created: 10/31/15 1:53 AM
 *
 * @author Yury Yakovlev
 */
public class CPUDataProvider {

    private static final Logger LOGGER = Logger.getLogger(CPUDataProvider.class);

    public Double getCPULoad() {

          return new Random().nextDouble() * 100;

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
//             LOGGER.error("CPUDataProvider.getCPULoad - error getting CPU load", t);
//            return 0.0;
//        }
    }
}
