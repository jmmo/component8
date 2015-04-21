package org.jmmo.component8.event;

import org.jmmo.component8.ComponentsContainer;

/**
 * User: Tomas
 * Date: 31.05.13
 * Time: 20:57
 */
public class InterfaceAvailableEvent extends InterfaceAvailabilityEvent {

    public InterfaceAvailableEvent(ComponentsContainer container, Class<?> interfaceClass) {
        super(container, interfaceClass);
    }
}
