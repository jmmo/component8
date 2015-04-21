package org.jmmo.component8;

/**
 * User: Tomas
 * Date: 31.03.13
 * Time: 13:46
 */
public interface Component {

    void containerAvailable(ComponentsContainer container);

    void containerRevoked(ComponentsContainer container);

    <I> void interfaceAvailable(Class<I> interfaceClass);

    <I> void interfaceRevoked(Class<I> interfaceClass);
}
