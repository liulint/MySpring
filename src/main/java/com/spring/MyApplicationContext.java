package com.spring;

import com.spring.annotation.*;
import com.spring.bean.BeanDefinition;
import com.spring.lifeStyle.BeanPostProcessor;
import com.spring.utils.AnnotationUtil;
import com.spring.utils.ObjectUtils;
import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * @Author: ly
 * @Package: com.spring
 * @Project: mySpring
 * @name: MyApplicationContext
 * @Date:2024/1/7 16:45
 */
public class MyApplicationContext {
    /**
     * 存放单例bean
     */
    private Map<String, Object> singletonBean = new HashMap<String, Object>();

    /**
     * 将扫描到的bean存放到beanDefinitionMap中，供下方的createBean使用
     */
    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<String,BeanDefinition>();

    /**
     * 存放实现BeanPostProcessor接口的类
     */
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MyApplicationContext() {
    }

    public MyApplicationContext(Class configClass) {
        System.out.println("MyApplicationContext init");
        scan(configClass);
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if("singleton".equals(beanDefinition.getScope())){
                Object beanInstance = createBean(entry.getValue(),beanName);
                singletonBean.put(beanName,beanInstance);
            }
        }
    }

    /**
     * 创建bean
     * @param beanDefinition
     * @param beanName
     * @return
     */
    public Object createBean(BeanDefinition beanDefinition,String beanName){
        Class classFile = beanDefinition.getType();
        Object classInstance = null;
        try {
            classInstance = classFile.getConstructor().newInstance();
            for (Field field : classFile.getDeclaredFields()) {
                if(field.isAnnotationPresent(Autowired.class)){
                    Class<?> type = field.getType();
                    Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                    String fieldBeanName = "";
                    if(ObjectUtils.isEmpty(qualifierAnnotation)){
                        fieldBeanName = Introspector.decapitalize(type.getSimpleName());
                    }else{
                        fieldBeanName = qualifierAnnotation.value();
                    }
                    field.setAccessible(true);
                    field.set(classInstance,getBean(fieldBeanName));
                }
            }
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessBeforeInitialization(classInstance,beanName);
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessAfterInitialization(classInstance,beanName);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return classInstance;
    }

    /**
     * 获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        if(!beanDefinitionMap.containsKey(beanName)){
            throw new NullPointerException();
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if("singleton".equals(beanDefinition.getScope())){
            Object beanInstance = singletonBean.get(beanName);
            if(beanInstance == null){
                beanInstance = createBean(beanDefinition,beanName);
                singletonBean.put(beanName,beanInstance);
            }
            return beanInstance;
        }else{
            return createBean(beanDefinition,beanName);
        }
    }

    /**
     * 扫描配置路径下的所有class对象
     * @param configClass
     */
    void scan(Class configClass){
        //判断该配置类上是否有ComponentScan注解
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String pathValue = componentScanAnnotation.value();
            pathValue = pathValue.replace(".","/");
            //获取MyApplicationContext对象的类加载器对象，为获取其它bean做准备
            ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
            //获取MyApplicationContext对象所在的文件路径
            URL resource = classLoader.getResource(pathValue);
            File folder = new File(resource.getFile());
            //存放每个class文件的绝对路径
            Map<String, String> beansPath = new HashMap<String, String>();
            //递归遍历该文件夹下的所有class文件,存放到beansPath对象中
            getBeansPathByrecursiveFolder(folder,beansPath);
            //给putBeanDefinitionMap对象添加bean
            putBeanDefinitionMap(beansPath,classLoader);
        }

    }

    /**
     * 递归遍历该文件夹下的所有class文件,存放到beansPath对象中
     * @param folder
     */
    void getBeansPathByrecursiveFolder(File folder,Map<String, String> beansPath){
        if(folder.isDirectory()){
            File[] files = folder.listFiles();
            for (File file : files) {
                getBeansPathByrecursiveFolder(file,beansPath);
            }
        }else{
            beansPath.put(folder.getAbsolutePath(),folder.getAbsolutePath());
        }
    }

    /**
     * 给putBeanDefinitionMap对象添加bean
     * @param beansPath
     * @param classLoader
     */
    void putBeanDefinitionMap(Map<String, String> beansPath,ClassLoader classLoader){
        for (String beanPath : beansPath.values()) {
            if(beanPath.contains(".class")){
                String path = beanPath.substring(beanPath.lastIndexOf("classes")+8, beanPath.indexOf(".class"));
                path = path.replace("\\", ".");
                try {
                    //获取到对应Class的内容
                    Class<?> classFile = classLoader.loadClass(path);
                    String beanName = "";
                    Component componentAnnotation = AnnotationUtil.findMergedAnnotation(classFile, Component.class);
                    if(!ObjectUtils.isEmpty(componentAnnotation)){
                        beanName = "".equals(componentAnnotation.value()) ? Introspector.decapitalize(classFile.getSimpleName()) : componentAnnotation.value();
                    }
                    //如果不为空，则说明是需要容器实例化的
                    if(!"".equals(beanName)){
                        if(BeanPostProcessor.class.isAssignableFrom(classFile)){
                            BeanPostProcessor instance = (BeanPostProcessor) classFile.getConstructor().newInstance();
                            beanPostProcessorList.add(instance);
                            continue;
                        }
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setType(classFile);
                        if(classFile.isAnnotationPresent(Scope.class)){
                            Scope scopeAnnotation = classFile.getAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                        }else{
                            beanDefinition.setScope("singleton");
                        }
                        beanDefinitionMap.put(beanName,beanDefinition);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}
