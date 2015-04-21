package org.jmmo.component8;

import org.jmmo.observable.Observable;

import java.util.stream.Stream;

/**
 * User: Tomas
 * Date: 16.03.13
 * Time: 10:54
 */
public interface ComponentsContainer extends Observable, InterfacesContainer {

    Stream<Component> getComponents();

    void addComponent(Component component);

    void removeComponent(Component component);

    void becomeAvailable(Class<?> availableInterface, Stream<?> implementations);

    void becomeRevoked(Class<?> revokedInterface, Stream<?> implementations);
}
