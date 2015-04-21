package org.jmmo.component8;

import org.jmmo.observable.Observable;
import org.jmmo.observable.ObservableTransparentContainerBaseCol;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: Tomas
 * Date: 24.05.13
 * Time: 12:00
 */
public abstract class ComponentBase extends ObservableTransparentContainerBaseCol<Observable> implements Component {
    protected final Set<Class<?>> interfaces = componentInterfaces().collect(Collectors.toSet());
    protected Optional<ComponentsContainer> containerOpt = Optional.empty();

    abstract protected Stream<Class<?>> componentInterfaces();

    protected boolean isAvailable() {
        return containerOpt.isPresent();
    }

    protected ComponentsContainer getContainer() {
        return containerOpt.get();
    }

    @Override
    public void containerAvailable(ComponentsContainer container) {
        this.containerOpt = Optional.of(container);
        onContainerAvailable();
    }

    protected void onContainerAvailable() {
        onBecomeAvailable();
    }

    protected void onBecomeAvailable() {
        interfaces.forEach(aClass -> getContainer().becomeAvailable(aClass, Stream.of(this)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void containerRevoked(ComponentsContainer container) {
        onContainerRevoked();
        containerOpt = Optional.empty();
    }

    protected void onContainerRevoked() {
        onBecomeRevoked();
    }

    protected void onBecomeRevoked() {
        interfaces.forEach(aClass -> getContainer().becomeRevoked(aClass, Stream.of(this)));
    }

    @Override
    public <I> void interfaceAvailable(Class<I> interfaceClass) {
    }

    @Override
    public <I> void interfaceRevoked(Class<I> interfaceClass) {
    }
}
