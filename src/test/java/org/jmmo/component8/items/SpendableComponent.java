package org.jmmo.component8.items;

import org.jmmo.component8.DependentComponentBase;

import java.util.function.IntSupplier;
import java.util.stream.Stream;

/**
 * User: Tomas
 * Date: 30.05.13
 * Time: 19:14
 */
public class SpendableComponent extends DependentComponentBase implements Spendable {
    private float charge = 1f;

    @Override
    protected Stream<Class<?>> componentInterfaces() {
        return Stream.of(Spendable.class, Runnable.class, IntSupplier.class);
    }

    @Override
    protected Stream<Class<?>> require() {
        return Stream.of(Consumable.class);
    }

    @Override
    public float getCharge() {
        return charge;
    }

    @Override
    public void setCharge(float charge) {
        this.charge = charge;
        if (charge <= 0f) {
            getContainer().forInterface(Consumable.class, consumable -> {
                int count = consumable.getCount();
                if (count > 0) {
                    consumable.setCount(count - 1);
                }
            });
        }
    }

    @Override
    public void run() {
        //do nothing
    }

    @Override
    public int getAsInt() {
        return 2;
    }
}
