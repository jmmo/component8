package org.jmmo.component8;

import java.util.stream.Stream;

/**
 * User: Tomas
 * Date: 30.05.13
 * Time: 21:13
 */
public abstract class DependentComponentBase extends ComponentBase {
    protected boolean wasAvailable;

    @Override
    public boolean isAvailable() {
        return containerOpt.map(container -> require().allMatch(container::isInterfaceAvailable)).orElse(false);
    }

    @Override
    protected void onContainerAvailable() {
        if (isAvailable()) {
            onBecomeAvailable();
        }
    }

    @Override
    public <I> void interfaceAvailable(Class<I> interfaceClass) {
        if (!wasAvailable && containerOpt.isPresent() && require().filter(interfaceClass::equals).findAny().isPresent() && isAvailable()) {
            onBecomeAvailable();
        }
    }

    @Override
    public <I> void interfaceRevoked(Class<I> interfaceClass) {
        if (wasAvailable && containerOpt.isPresent() && require().filter(interfaceClass::equals).findAny().isPresent()) {
            onBecomeRevoked();
        }
    }

    @Override
    protected void onBecomeAvailable() {
        wasAvailable = true;
        super.onBecomeAvailable();
    }

    @Override
    protected void onBecomeRevoked() {
        wasAvailable = false;
        super.onBecomeRevoked();
    }

    protected abstract Stream<Class<?>> require();
}
