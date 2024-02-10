package org.cathttp.base.app;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.base.servlet.ServletProxy;

import javax.servlet.ServletContext;
import java.util.Map;

public class ServletAppWeb  implements LifeCycle {
    ServletContext servletContext;

    Map<String, ServletProxy> servletProxyMap;
    
    @Override
    public void init() {

    }

    @Override
    public void pause() {

    }
}
