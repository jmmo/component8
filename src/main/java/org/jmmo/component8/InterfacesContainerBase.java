package org.jmmo.component8;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class InterfacesContainerBase implements InterfacesContainer {

    @Override
    public <I> boolean isInterfaceAvailable(Class<I> interfaceClass) {
        return getInterfaces().contains(interfaceClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I> void forInterface(Class<I> interfaceClass, Consumer<? super I> consumer) {
        consumer.accept(implementationsFor(interfaceClass).reduce((aClass, aClass2) -> {
            throw new IllegalStateException("There are too many implementations of " + interfaceClass);
        }).orElseThrow(() -> new IllegalArgumentException("There is no implementation of " + interfaceClass)));
    }

    @Override
    public <I> void forInterfaces(Class<I> interfaceClass, Consumer<? super I> consumer) {
        implementationsFor(interfaceClass).forEach(consumer::accept);
    }

    @Override
    public <I, R> R fromInterface(Class<I> interfaceClass, Function<? super I, R> function) {
        return implementationsFor(interfaceClass).reduce((aClass, aClass2) -> {
            throw new IllegalStateException("There are too many implementations of " + interfaceClass);
        }).map(function::apply).orElseThrow(() -> new IllegalArgumentException("There is no implementation of " + interfaceClass));
    }

    @Override
    public <I, R> Stream<R> fromInterfaces(Class<I> interfaceClass, Function<? super I, R> function) {
        return implementationsFor(interfaceClass).map(function::apply);
    }

    abstract protected <I> Stream<? extends I> implementationsFor(Class<I> interfaceClass);
}
