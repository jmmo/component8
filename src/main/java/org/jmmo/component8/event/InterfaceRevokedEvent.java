package org.jmmo.component8.event;

import org.jmmo.component8.ComponentsContainer;

/**
 * User: Tomas
 * Date: 31.05.13
 * Time: 20:58
 */
public class InterfaceRevokedEvent extends InterfaceAvailabilityEvent {

    public InterfaceRevokedEvent(ComponentsContainer container, Class<?> interfaceClass) {
        super(container, interfaceClass);
    }
}
