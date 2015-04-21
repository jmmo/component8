package org.jmmo.component8.items;


import org.jmmo.component8.ComponentBase;

import java.util.stream.Stream;

/**
 * User: Tomas
 * Date: 30.05.13
 * Time: 19:11
 */
public class ConsumableComponent extends ComponentBase implements Consumable {
    private int count;

    @Override
    protected Stream<Class<?>> componentInterfaces() {
        return Stream.of(Consumable.class);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getStackSize() {
        return 10;
    }
}
