package org.jmmo.component8;

import org.jmmo.component8.items.Consumable;
import org.jmmo.component8.items.ConsumableComponent;
import org.jmmo.component8.items.Rechargeable;
import org.jmmo.component8.items.RechargeableComponent;
import org.jmmo.component8.items.RunnableComponent;
import org.jmmo.component8.items.Spendable;
import org.jmmo.component8.items.SpendableComponent;
import org.jmmo.component8.event.InterfaceAvailableEvent;
import org.jmmo.component8.event.InterfaceRevokedEvent;
import org.jmmo.observable.MockListenerHelper;
import org.jmmo.observable.Observable;
import org.jmmo.observable.event.ObservableEvent;
import org.jmmo.observable.event.ObservableListener;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * User: Tomas
 * Date: 30.05.13
 * Time: 18:59
 */
public class ComponentsContainerImplTest {

    private ComponentsContainerImpl container = new ComponentsContainerImpl();

    private RechargeableComponent rechargeableComponent = new RechargeableComponent();
    private ConsumableComponent consumableComponent = new ConsumableComponent();
    private SpendableComponent spendableComponent = new SpendableComponent();
    private RunnableComponent runnableComponent = new RunnableComponent();

    @Test
    public void testGetComponents() throws Exception {
        assertEquals(Collections.<Component>emptySet(), container.getComponents().collect(toSet()));

        container.addComponent(rechargeableComponent);
        assertEquals(Collections.<Component>singleton(rechargeableComponent), container.getComponents().collect(toSet()));

        container.addComponent(spendableComponent);
        assertEquals(new HashSet<Component>(Arrays.asList(rechargeableComponent, spendableComponent)), container.getComponents().collect(toSet()));

        container.removeComponent(rechargeableComponent);
        assertEquals(Collections.<Component>singleton(spendableComponent), container.getComponents().collect(toSet()));

        container.removeComponent(spendableComponent);
        assertEquals(Collections.<Component>emptySet(), container.getComponents().collect(toSet()));
    }

    @Test
    public void testGetInterfaces() throws Exception {
        assertEquals(Collections.<Class<?>>emptySet(), container.getInterfaces());

        container.addComponent(rechargeableComponent);
        assertEquals(new HashSet<>(Arrays.asList(Rechargeable.class, IntSupplier.class)), container.getInterfaces());

        container.addComponent(spendableComponent);
        assertEquals(new HashSet<>(Arrays.asList(Rechargeable.class, IntSupplier.class)), container.getInterfaces());

        container.addComponent(consumableComponent);
        assertEquals(new HashSet<>(Arrays.asList(Rechargeable.class, IntSupplier.class, Spendable.class, Runnable.class, Consumable.class)), container.getInterfaces());

        container.removeComponent(consumableComponent);
        assertEquals(new HashSet<>(Arrays.asList(Rechargeable.class, IntSupplier.class)), container.getInterfaces());

        container.removeComponent(rechargeableComponent);
        assertEquals(Collections.<Class<?>>emptySet(), container.getInterfaces());
    }

    @Test
    public void testIsInterfaceAvailable() throws Exception {
        container.addComponent(spendableComponent);
        assertFalse(container.isInterfaceAvailable(Spendable.class));
        assertFalse(spendableComponent.isAvailable());
        assertFalse(container.isInterfaceAvailable(Runnable.class));
        assertFalse(container.isInterfaceAvailable(IntSupplier.class));

        container.addComponent(consumableComponent);
        assertTrue(container.isInterfaceAvailable(Consumable.class));
        assertTrue(consumableComponent.isAvailable());
        assertTrue(container.isInterfaceAvailable(Spendable.class));
        assertTrue(spendableComponent.isAvailable());
        assertTrue(container.isInterfaceAvailable(Runnable.class));
        assertTrue(container.isInterfaceAvailable(IntSupplier.class));

        container.removeComponent(spendableComponent);
        assertFalse(container.isInterfaceAvailable(Spendable.class));
        assertFalse(spendableComponent.isAvailable());
        assertFalse(container.isInterfaceAvailable(Runnable.class));
        assertFalse(container.isInterfaceAvailable(IntSupplier.class));

        container.addComponent(spendableComponent);
        assertTrue(container.isInterfaceAvailable(Spendable.class));
        assertTrue(spendableComponent.isAvailable());
        assertTrue(container.isInterfaceAvailable(Runnable.class));
        assertTrue(container.isInterfaceAvailable(IntSupplier.class));

        container.removeComponent(consumableComponent);
        assertFalse(container.isInterfaceAvailable(Consumable.class));
        assertFalse(consumableComponent.isAvailable());
        assertFalse(container.isInterfaceAvailable(Spendable.class));
        assertFalse(spendableComponent.isAvailable());
        assertFalse(container.isInterfaceAvailable(Runnable.class));
        assertFalse(container.isInterfaceAvailable(IntSupplier.class));

        container.removeComponent(spendableComponent);
        container.addComponent(consumableComponent);
        container.addComponent(spendableComponent);
        assertTrue(container.isInterfaceAvailable(Consumable.class));
        assertTrue(consumableComponent.isAvailable());
        assertTrue(container.isInterfaceAvailable(Spendable.class));
        assertTrue(spendableComponent.isAvailable());
        assertTrue(container.isInterfaceAvailable(Runnable.class));
        assertTrue(container.isInterfaceAvailable(IntSupplier.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForInterfacesNoImplementation() throws Exception {
        container.forInterface(Runnable.class, runnable -> System.out.println("It must not be written"));
    }

    @Test(expected = IllegalStateException.class)
    public void testForInterfacesMoreThanOne() throws Exception {
        container.addComponent(consumableComponent);
        container.addComponent(spendableComponent);
        container.addComponent(runnableComponent);
        container.forInterface(Runnable.class, runnable -> System.out.println("It must not be written"));
    }

    @Test
    public void testForInterfaces() throws Exception {
        final int[] counter = new int[] {0};
        final Consumer<Runnable> handler = runnable -> counter[0]++;

        container.addComponent(rechargeableComponent);
        container.forInterfaces(Runnable.class, handler);
        assertEquals(0, counter[0]);

        container.addComponent(spendableComponent);
        container.forInterfaces(Runnable.class, handler);
        assertEquals(0, counter[0]);

        container.addComponent(consumableComponent);
        container.forInterfaces(Runnable.class, handler);
        assertEquals(1, counter[0]);
        counter[0] = 0;
        container.forInterface(Runnable.class, handler);
        assertEquals(1, counter[0]);

        counter[0] = 0;
        container.addComponent(runnableComponent);
        container.forInterfaces(Runnable.class, handler);
        assertEquals(2, counter[0]);

        counter[0] = 0;
        container.removeComponent(runnableComponent);
        container.forInterfaces(Runnable.class, handler);
        assertEquals(1, counter[0]);

        counter[0] = 0;
        container.removeComponent(consumableComponent);
        container.forInterfaces(Runnable.class, handler);
        assertEquals(0, counter[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromInterfacesNoImplementation() throws Exception {
        container.fromInterface(IntSupplier.class, intSupplier -> 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromInterfacesMoreThanOne() throws Exception {
        container.addComponent(consumableComponent);
        container.addComponent(rechargeableComponent);
        container.addComponent(spendableComponent);
        container.fromInterface(IntSupplier.class, intSupplier -> 0);
    }

    @Test
    public void testFromInterfaces() throws Exception {
        final int[] counter = new int[] {0};
        final Function<IntSupplier, Integer> handler = supplier -> {
            counter[0]++;
            return supplier.getAsInt();
        };

        assertEquals(Optional.<Integer>empty(), container.fromInterfaces(IntSupplier.class, handler).findAny());
        assertEquals(0, counter[0]);

        container.addComponent(rechargeableComponent);
        assertEquals(Optional.of(1), container.fromInterfaces(IntSupplier.class, handler).findAny());
        assertEquals(1, counter[0]);
        counter[0] = 0;
        assertEquals(1, (int) container.fromInterface(IntSupplier.class, handler));
        assertEquals(1, counter[0]);

        counter[0] = 0;
        container.addComponent(spendableComponent);
        assertEquals(Collections.singleton(1), container.fromInterfaces(IntSupplier.class, handler).collect(toSet()));
        assertEquals(1, counter[0]);

        counter[0] = 0;
        container.addComponent(consumableComponent);
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), container.fromInterfaces(IntSupplier.class, handler).collect(toSet()));
        assertEquals(2, counter[0]);

        counter[0] = 0;
        container.removeComponent(consumableComponent);
        assertEquals(Collections.singleton(1), container.fromInterfaces(IntSupplier.class, handler).collect(toSet()));
        assertEquals(1, counter[0]);

        counter[0] = 0;
        container.removeComponent(rechargeableComponent);
        assertEquals(Optional.<Integer>empty(), container.fromInterfaces(IntSupplier.class, handler).findAny());
        assertEquals(0, counter[0]);
    }

    @Test
    public void testFireEvents() throws Exception {
        ObservableListener listener = MockListenerHelper.createMockListener();
        container.addObservableListener(listener);

        container.addComponent(spendableComponent);
        verify(listener, never()).handleObservableEvent(any(ObservableEvent.class), eq(Collections.<Observable>emptyList()));

        container.addComponent(consumableComponent);
        verify(listener).handleObservableEvent(new InterfaceAvailableEvent(container, Consumable.class), Collections.<Observable>emptyList());
        verify(listener).handleObservableEvent(new InterfaceAvailableEvent(container, Spendable.class), Collections.<Observable>emptyList());
        verify(listener).handleObservableEvent(new InterfaceAvailableEvent(container, Runnable.class), Collections.<Observable>emptyList());
        verify(listener).handleObservableEvent(new InterfaceAvailableEvent(container, IntSupplier.class), Collections.<Observable>emptyList());

        reset(listener);
        container.removeComponent(consumableComponent);
        verify(listener).handleObservableEvent(new InterfaceRevokedEvent(container, Consumable.class), Collections.<Observable>emptyList());
        verify(listener).handleObservableEvent(new InterfaceRevokedEvent(container, Spendable.class), Collections.<Observable>emptyList());
        verify(listener).handleObservableEvent(new InterfaceRevokedEvent(container, Runnable.class), Collections.<Observable>emptyList());
        verify(listener).handleObservableEvent(new InterfaceRevokedEvent(container, IntSupplier.class), Collections.<Observable>emptyList());

        reset(listener);
        container.removeComponent(spendableComponent);
        verify(listener, never()).handleObservableEvent(any(ObservableEvent.class), eq(Collections.<Observable>emptyList()));
    }

    @Test
    public void testAvailabilityInterfaces() throws Exception {
        ConsumableComponent consumableSpy = spy(consumableComponent);

        container.addComponent(rechargeableComponent);
        container.addComponent(consumableSpy);

        verify(consumableSpy).interfaceAvailable(Rechargeable.class);
        verify(consumableSpy).interfaceAvailable(IntSupplier.class);
        verify(consumableSpy).interfaceAvailable(Consumable.class);

        container.removeComponent(consumableSpy);

        verify(consumableSpy).interfaceRevoked(Rechargeable.class);
        verify(consumableSpy).interfaceRevoked(IntSupplier.class);
        verify(consumableSpy).interfaceRevoked(Consumable.class);
    }
}
