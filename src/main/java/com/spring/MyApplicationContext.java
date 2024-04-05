package com.spring;


import com.spring.annotation.*;
import com.spring.bean.BeanDefinition;
import com.spring.lifeStyle.BeanPostProcessor;
import com.spring.lifeStyle.InitializingBean;
import com.spring.utils.AnnotationUtil;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String,BeanDefinition>();

    /**
     * 存放实现BeanPostProcessor接口的类
     */
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MyApplicationContext(Class configClass) {
        System.out.println("MyApplicationContext init");
        scan(configClass);
//        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
//            String beanName = entry.getKey();
//            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
//            if("singleton".equals(beanDefinition.getScope())){
//                Object beanInstance = createBean(entry.getValue(),beanName);
//                singletonBean.put(beanName,beanInstance);
//            }
//        }
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
            //获取类加载器对象，以获取classpath下的路径
            ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
            //通过classpath目录，扫描该路径下的class文件
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
//                    Component componentAnnotation = classFile.getDeclaredAnnotation(Component.class);//获取Componetn注解
                    if(componentAnnotation != null){
                        //Introspector.decapitalize(classFile.getSimpleName()),将class文件的类型首字母转为小写给beanName
                        beanName = "".equals(componentAnnotation.value()) ? Introspector.decapitalize(classFile.getSimpleName()) : componentAnnotation.value();
                    }
                    //如果不为空，则说明是需要容器实例化的
                    if(!"".equals(beanName)){
                        //判断该类是否实现了BeanPostProcessor接口，如果实现就将其放到beanPostProcessorList列表中
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
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

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
            //拿到该类的所有属性，通过getBean对其进行赋值
            for (Field field : classFile.getDeclaredFields()) {
                if(field.isAnnotationPresent(Autowired.class)){
                    Class<?> type = field.getType();
                    Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                    String fieldBeanName = "";
                    if(qualifierAnnotation==null){
                        fieldBeanName = Introspector.decapitalize(type.getSimpleName());
                    }else{
                        fieldBeanName = qualifierAnnotation.value();
                    }
                    field.setAccessible(true);
                    field.set(classInstance,getBean(fieldBeanName));
                }
            }
            //实例化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                classInstance = beanPostProcessor.postProcessBeforeInitialization(classInstance,beanName);
            }
            //如果实现InitializingBean该接口，就执行方法
            if(classInstance instanceof InitializingBean){
                ((InitializingBean) classInstance).afterPropertiesSet();
            }
            //实例化后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                classInstance = beanPostProcessor.postProcessAfterInitialization(classInstance,beanName);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classInstance;
    }


}
