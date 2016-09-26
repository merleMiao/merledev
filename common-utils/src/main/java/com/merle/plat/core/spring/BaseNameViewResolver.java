package com.merle.plat.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

import java.io.Serializable;


public class BaseNameViewResolver extends ResourceBundleViewResolver implements InitializingBean, Serializable {


    private IBaseNamePicker baseNamePicker;

    public IBaseNamePicker getBaseNamePicker() {
        return baseNamePicker;
    }

    public void setBaseNamePicker(IBaseNamePicker baseNamePicker) {
        this.baseNamePicker = baseNamePicker;
    }

    @Override
    public void afterPropertiesSet() throws BeansException {
        super.afterPropertiesSet();
        try {
            super.setBasenames(baseNamePicker.pickBaseName());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
