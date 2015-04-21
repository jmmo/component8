package org.jmmo.component8;

import org.jmmo.component8.event.InterfaceAvailableEvent;
import org.jmmo.component8.event.InterfaceRevokedEvent;
import org.jmmo.observable.Observable;
import org.jmmo.observable.ObservableContainerBaseCol;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * User: Tomas
 * Date: 24.05.13
 * Time: 12:12
 *
 * <p>Fires events:</p>
 * {@link org.jmmo.component8.event.InterfaceAvailableEvent}<br>
 * {@link org.jmmo.component8.event.InterfaceRevokedEvent}<br>
 */
public class ComponentsContainerImpl extends ObservableContainerBaseCol<Observable> implements ComponentsContainer {
    protected final InterfacesContainerBase interfacesContainer = new InterfacesContainerBase() {
        @Override
        public Set<Class<?>> getInterfaces() {
            return availableInterfaces.keySet();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <I> Stream<I> implementationsFor(Class<I> interfaceClass) {
            return availableInterfaces.getOrDefault(interfaceClass, Collections.emptySet()).stream().map(component -> (I) component);
        }
    };

    @Override
    public Set<Class<?>> getInterfaces() {
        return Collections.unmodifiableSet(interfacesContainer.getInterfaces());
    }

    @Override
    public <I> boolean isInterfaceAvailable(Class<I> interfaceClass) {
        return interfacesContainer.isInterfaceAvailable(interfaceClass);
    }

    @Override
    public <I> void forInterface(Class<I> interfaceClass, Consumer<? super I> consumer) {
        interfacesContainer.forInterface(interfaceClass, consumer);
    }

    @Override
    public <I> void forInterfaces(Class<I> interfaceClass, Consumer<? super I> consumer) {
        interfacesContainer.forInterfaces(interfaceClass, consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I, R> R fromInterface(Class<I> interfaceClass, Function<? super I, R> function) {
        return interfacesContainer.fromInterface(interfaceClass, function);
    }

    @Override
    public <I, R> Stream<R> fromInterfaces(Class<I> interfaceClass, Function<? super I, R> function) {
        return interfacesContainer.fromInterfaces(interfaceClass, function);
    }

    protected final Set<Component> components = new HashSet<>();
    protected final Map<Class<?>, Set<Object>> availableInterfaces = new HashMap<>();

    @Override
    public Stream<Component> getComponents() {
        return components.stream();
    }

    @Override
    public void addComponent(Component component) {
        if (components.contains(component)) {
            throw new IllegalArgumentException("Can't add " + component + " because there is the same one in " + this);
        }

        availableInterfaces.keySet().forEach(component::interfaceAvailable);

        components.add(component);
        component.containerAvailable(this);

        if (component instanceof Observable) {
            addChildObservable((Observable) component);
        }
    }

    @Override
    public void removeComponent(Component component) {
        if (!components.contains(component)) {
            throw new IllegalArgumentException(component + " is not found in " + this);
        }

        component.containerRevoked(this);
        components.remove(component);

        availableInterfaces.keySet().forEach(component::interfaceRevoked);

        if (component instanceof Observable) {
            removeChildObservable((Observable) component);
        }
    }

    @Override
    public void becomeAvailable(Class<?> availableInterface, Stream<?> implementations) {
        boolean wasAvailable = isInterfaceAvailable(availableInterface);

        final Set<Object> implementationsSet = implementations.collect(Collectors.toCollection(HashSet<Object>::new));
        availableInterfaces.merge(availableInterface, implementationsSet, (components1, components2) -> {
            if (!Collections.disjoint(components1, components2)) {
                throw new IllegalArgumentException("Reimplementing of " + availableInterface + " in " + this + " by " + implementationsSet);
            }
            components1.addAll(components2);
            return components1;
        });

        if (!wasAvailable && isInterfaceAvailable(availableInterface)) {
            components.forEach(component -> component.interfaceAvailable(availableInterface));
            fireObservableEvent(new InterfaceAvailableEvent(this, availableInterface));
        }
    }

    @Override
    public void becomeRevoked(Class<?> revokedInterface, Stream<?> implementations) {
        boolean wasAvailable = isInterfaceAvailable(revokedInterface);

        availableInterfaces.compute(revokedInterface, (aClass, components) -> {
            if (components == null || components.size() < 2) {
                return null;
            }

            components.removeAll(implementations.collect(Collectors.toCollection(HashSet<Object>::new)));
            return components;
        });

        if (wasAvailable && !isInterfaceAvailable(revokedInterface)) {
            components.forEach(component -> component.interfaceRevoked(revokedInterface));
            fireObservableEvent(new InterfaceRevokedEvent(this, revokedInterface));
        }
    }

    @Override
    protected void doFireAddedObservableEvent(Observable source, Observable involved) {
        //not fire this event
    }

    @Override
    protected void doFireRemovedObservableEvent(Observable source, Observable involved) {
        //not fire this event
    }
}
