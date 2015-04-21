package org.jmmo.component8;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface InterfacesContainer {

    Set<Class<?>> getInterfaces();

    <I> boolean isInterfaceAvailable(Class<I> interfaceClass);

    <I> void forInterface(Class<I> interfaceClass, Consumer<? super I> consumer);

    <I> void forInterfaces(Class<I> interfaceClass, Consumer<? super I> consumer);

    <I, R> R fromInterface(Class<I> interfaceClass, Function<? super I, R> function);

    <I, R> Stream<R> fromInterfaces(Class<I> interfaceClass, Function<? super I, R> function);
}
