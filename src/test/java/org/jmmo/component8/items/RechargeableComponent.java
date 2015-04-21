package org.jmmo.component8.items;

import org.jmmo.component8.ComponentBase;

import java.util.function.IntSupplier;
import java.util.stream.Stream;

/**
 * User: Tomas
 * Date: 30.05.13
 * Time: 21:28
 */
public class RechargeableComponent extends ComponentBase implements Rechargeable, Runnable, IntSupplier {

    @Override
    protected Stream<Class<?>> componentInterfaces() {
        return Stream.of(Rechargeable.class, IntSupplier.class);
    }

    @Override
    public int getQuantityInCartridge() {
        return 1;
    }

    @Override
    public void run() {
        //do nothing
    }

    @Override
    public int getAsInt() {
        return 1;
    }
}
