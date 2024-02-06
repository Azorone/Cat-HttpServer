package org.cathttp.base.net.inter;

public interface DistributionCenter<T> {

    boolean input(T t);
    boolean distribution() throws InterruptedException;

}
