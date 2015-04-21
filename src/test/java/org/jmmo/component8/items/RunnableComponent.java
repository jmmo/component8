package org.jmmo.component8.items;

import org.jmmo.component8.ComponentBase;

import java.util.stream.Stream;

public class RunnableComponent extends ComponentBase implements Runnable {
    @Override
    protected Stream<Class<?>> componentInterfaces() {
        return Stream.of(Runnable.class);
    }

    @Override
    public void run() {
        //do nothing
    }
}
