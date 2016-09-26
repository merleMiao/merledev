package com.merle.plat.core.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.ArrayList;
import java.util.List;


public class BaseNamePickerImpl implements InitializingBean, IBaseNamePicker {

    private String[] configLocations;

    public String[] getConfigLocations() {
        return configLocations;
    }

    public void setConfigLocations(String[] configLocations) {
        this.configLocations = configLocations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public String[] pickBaseName() throws Exception {
        if (this.configLocations == null || this.configLocations.length < 1) {
            throw new RuntimeException("configLocations property of IBaseNameResolverImpl is require!");
        }
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        String rootPath = this.getClass().getClassLoader().getResource("//").toString();
        List<String> baseNameList = new ArrayList<String>();

        for (int i = 0; i < this.configLocations.length; i++) {
            Resource[] resources = pathResolver.getResources(configLocations[i].trim());
            if (resources != null) {
                for (int j = 0; j < resources.length; j++) {
                    String baseName = resources[j].getURI().toString().replaceAll(rootPath, "");

                    if (baseName.indexOf(".") != -1) {
                        baseName = baseName.substring(0, baseName.indexOf("."));
                    }
                    baseNameList.add(baseName);
                }
            }
        }
        return baseNameList.toArray(new String[]{});
    }
}
