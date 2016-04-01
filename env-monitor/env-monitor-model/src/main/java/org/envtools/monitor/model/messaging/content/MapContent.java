package org.envtools.monitor.model.messaging.content;

import java.util.Map;

/**
 * Created: 01.04.16 22:22
 *
 * @author Yury Yakovlev
 */
public class MapContent extends AbstractContent <Map<?,?>> {

   public static MapContent of(Map<?, ?> value) {
       MapContent mc = new MapContent();
       mc.setValue(value);
       return mc;
   }

}

