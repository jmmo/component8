package org.jmmo.component8.items;

import java.util.function.IntSupplier;

/**
 * User: Tomas
 * Date: 25.03.13
 * Time: 9:59
 */
public interface Spendable extends Runnable, IntSupplier {

    /**
     * Returns item balance what can change in the battle from 1 to 0.
     * 1 value means item is fully charged.
     * 0 value means item is empty.
     * If item become empty it count will be decreased by 1 and item will be fully charged.
     * At the end of the battle all items with balance less than 1 will be decreased by 1.
     */
    float getCharge();
    void setCharge(float charge);
}
