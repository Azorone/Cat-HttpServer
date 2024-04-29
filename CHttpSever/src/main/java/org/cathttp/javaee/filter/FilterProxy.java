package org.cathttp.javaee.filter;

import org.cathttp.base.net.inter.LifeCycle;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//过滤器顺序有关
public class FilterProxy implements LifeCycle {
    @Override
    public void init() {

    }

    @Override
    public void pause() {

    }

    Filter curFilter;
    String description;
    String displayName;
    WebInitParam[] initParams;
    String filterName;
    String smallIcon;
    String largeIcon;
    String[] servletNames;
    String[] value;

    Set<String> urlPatterns;
    DispatcherType[] dispatcherTypes;
    boolean asyncSupported;

    public FilterProxy(WebFilter webFilter){

       this.filterName= webFilter.filterName();
       this.description = webFilter.description();
       this.displayName = webFilter.displayName();
       this.smallIcon = webFilter.smallIcon();
       this.largeIcon = webFilter.largeIcon();
       this.urlPatterns = new HashSet<>(List.of(webFilter.urlPatterns()));
       this.servletNames = webFilter.servletNames();
       this.value = webFilter.value();
       this.initParams = webFilter.initParams();
       dispatcherTypes = webFilter.dispatcherTypes();
       this.asyncSupported = webFilter.asyncSupported();
    }
    public FilterProxy(WebFilter webFilter,Filter filter){

        this.filterName= webFilter.filterName();
        this.description = webFilter.description();
        this.displayName = webFilter.displayName();
        this.smallIcon = webFilter.smallIcon();
        this.largeIcon = webFilter.largeIcon();
        this.urlPatterns = new HashSet<>(List.of(webFilter.urlPatterns()));
        this.servletNames = webFilter.servletNames();
        this.value = webFilter.value();
        this.initParams = webFilter.initParams();
        dispatcherTypes = webFilter.dispatcherTypes();
        this.asyncSupported = webFilter.asyncSupported();
        this.curFilter = filter;
    }
    public FilterProxy(){};
    boolean isIntercept(String path){

        return urlPatterns.contains(path);
    }

    public void setCurFilter(Filter filter){
        this.curFilter = filter;
    }
    public Filter getCurFilter() {
        return curFilter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public WebInitParam[] getInitParams() {
        return initParams;
    }

    public void setInitParams(WebInitParam[] initParams) {
        this.initParams = initParams;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String[] getServletNames() {
        return servletNames;
    }

    public void setServletNames(String[] servletNames) {
        this.servletNames = servletNames;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public Set<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(Set<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public DispatcherType[] getDispatcherTypes() {
        return dispatcherTypes;
    }

    public void setDispatcherTypes(DispatcherType[] dispatcherTypes) {
        this.dispatcherTypes = dispatcherTypes;
    }

    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }
}
