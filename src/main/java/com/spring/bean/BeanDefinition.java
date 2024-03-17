package com.spring.bean;

/**
 * @Author: ly
 * @Package: com.spring.bean
 * @Project: mySpring
 * @name: BeanDefinition
 * @Date:2024/1/7 22:55
 */
public class BeanDefinition {
    private Class type;
    private String scope;
    private boolean isLazy;

    private String beanName;


    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
