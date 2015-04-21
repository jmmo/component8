package org.jmmo.component8.event;

import org.jmmo.component8.ComponentsContainer;
import org.jmmo.observable.event.ObservableEvent;

/**
 * User: Tomas
 * Date: 31.05.13
 * Time: 20:50
 */
public class ContainerEvent extends ObservableEvent {

    public ContainerEvent(ComponentsContainer container) {
        super(container);
    }

    @Override
    public ComponentsContainer getSource() {
        return (ComponentsContainer) super.getSource();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContainerEvent that = (ContainerEvent) o;

        if (!getSource().equals(that.getSource())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getSource().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "source=" + getSource() +
                '}';
    }
}
