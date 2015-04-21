package org.jmmo.component8.event;

import org.jmmo.component8.ComponentsContainer;

/**
 * User: Tomas
 * Date: 31.05.13
 * Time: 20:54
 */
public abstract class InterfaceAvailabilityEvent extends ContainerEvent {
    private final Class<?> interfaceClass;

    protected InterfaceAvailabilityEvent(ComponentsContainer container, Class<?> interfaceClass) {
        super(container);
        this.interfaceClass = interfaceClass;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InterfaceAvailabilityEvent that = (InterfaceAvailabilityEvent) o;

        if (!interfaceClass.equals(that.interfaceClass)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + interfaceClass.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "source=" + getSource() +
                ", interfaceClass=" + interfaceClass +
                '}';
    }
}
