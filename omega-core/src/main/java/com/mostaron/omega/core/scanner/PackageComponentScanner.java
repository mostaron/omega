package com.mostaron.omega.core.scanner;

import com.mostaron.omega.core.annotation.Component;
import com.mostaron.omega.core.exception.OmegaCommonException;
import com.mostaron.omega.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Based on package path, iterate all classes using {@code @Component} annotation.<br>
 * description: PackageComponentScanner <br>
 * date: 2022/5/7 11:55 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class PackageComponentScanner {

    /**
     * 资源URL协议类型-jar
     */
    private static final String PROTOCOL_JAR = "jar";
    /**
     * 资源URL协议类型-file
     */
    private static final String PROTOCOL_FILE = "file";

    private static final Logger logger = LoggerFactory.getLogger(PackageComponentScanner.class);

    /**
     * Class文件后缀
     */
    private static final String CLASS_SUFFIX = ".class";
    //Patterns to filter innerClass

    private static final Pattern INNER_PATTERN = java.util.regex.Pattern.compile("\\$(\\d+).", java.util.regex.Pattern.CASE_INSENSITIVE);
    private static final int MAX_RECURSION = 5;

    /**
     * description: doScan <br>
     * version: 0.1 <br>
     * date: 2022/5/7 12:02 <br>
     * author: Neil <br>
     *
     * @param basePackages package paths for scan
     * @return all classes
     */
    public static Set<Class<?>> doScan(String[] basePackages) {

        Assert.notNull(basePackages, "basePackage cannot be null");

        ClassLoader classLoader = PackageComponentScanner.class.getClassLoader();
        Set<Class<?>> classSet;
        for (String basePackage : basePackages) {
            try {
                basePackage = basePackage.replace(".", "/");
                logger.debug("Scanning for basePackage:{}", basePackage);
                Enumeration<URL> classUrlEnum = classLoader.getResources(basePackage);
                logger.debug("Scanned basePackage, founded:{}", classUrlEnum.hasMoreElements());

                //遍历URL，获取Class路径列表
                Set<String> classNameSet = new HashSet<>();
                while (classUrlEnum.hasMoreElements()) {
                    URL classUrl = classUrlEnum.nextElement();
                    logger.debug("scanning classes in package[{}:{}]", classUrl.getProtocol(), classUrl.getFile());
                    if (classUrl.getProtocol().equals(PROTOCOL_JAR)) {

                        //处理Jar类型的Class文件
                        iterateJarFile(basePackage, classUrl, classNameSet);

                    } else if (classUrl.getProtocol().equals(PROTOCOL_FILE)) {

                        //处理File类型的Class文件
                        iterateClassFile(new File(classUrl.getFile()), classNameSet);
                    }

                }

//                classNameSet.forEach(item -> logger.debug("class found:{}", item));

                //遍历Class路径列表， 反射生成Class
                classSet = parseClassSet(classNameSet);

                classSet.forEach(clazz -> logger.debug("Valid class: [{}]", clazz.getName()));
            } catch (IOException e) {
                logger.error("Exception while scanning packages", e);
                System.exit(0);
            }
        }
        return null;
    }

    private static void iterateJarFile(String basePackage, URL url, Set<String> classNameSet) throws IOException {
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        if (null == conn) {
            throw new OmegaCommonException("Cannot open connection to Jar file: " + url.getPath());
        }

        JarFile jarFile = conn.getJarFile();
        Assert.notNull(jarFile, "Cannot open jarFile: " + conn.getEntryName());

        logger.debug("Processing Jar file: [{}]", jarFile.getName());
        // 遍历Jar包中的实体
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            //过滤非Class文件及非basePackage包下的文件
            if (!entryName.endsWith(CLASS_SUFFIX)
                    || !entryName.replace("/", ".").startsWith(basePackage)) {
                continue;
            }
            String className = entryName.replace("/", ".");
            addToClassSet(className, classNameSet);

        }
    }

    /**
     * Iterate all class files contained in package
     * description: iterateClassFile <br>
     * version: 1.0 <br>
     * date: 2022/5/9 14:31 <br>
     * author: Neil <br>
     */
    private static void iterateClassFile(File dir, Set<String> classNameSet) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            Assert.notNull(files, "files cannot be null");
            for (File file : files) {
                iterateClassFile(file, classNameSet);
            }
        } else if (dir.getName().endsWith(CLASS_SUFFIX)) {
            String name = dir.getPath();
            //截取Class文件路径中截止classes目录后的内容
            //原始文件名形如：
            // C:\Users\mosta\IdeaProjects\omega\omega\omega-core\target\classes\com\mostaron\omega\core\ApplicationStart.class
            name = name.substring(name.indexOf("classes") + 8).replace("\\", ".");
            addToClassSet(name, classNameSet);
        }
    }

    /**
     * Add class name to set, meanwhile remove inner class and anonymous class
     * description: addToClassSet <br>
     * version: 1.0 <br>
     * date: 2022/5/9 14:33 <br>
     * author: Neil <br>
     *
     * @param name     className
     * @param classSet result class set
     */
    private static void addToClassSet(String name, Set<String> classSet) {

        //过滤掉匿名内部类
        if (INNER_PATTERN.matcher(name).find()) {
//            logger.debug("anonymous inner class: {}", name);
            return;
        }
//        logger.debug("class:{}", name);
        if (name.indexOf("$") > 0) { //内部类
//            logger.debug("inner class:{}", name);
            return;
        }
        //去除Class文件名后的.class后缀
        classSet.add(name.substring(0, name.lastIndexOf(CLASS_SUFFIX)));
    }


    /**
     * Parse set of className to set of Classes
     * description: parseClassSet <br>
     * version: 1.0 <br>
     * date: 2022/5/9 15:39 <br>
     * author: Neil <br>
     *
     * @param classNameSet Set of class names
     * @return java.util.Set<java.lang.Class < ?>>
     */
    private static Set<Class<?>> parseClassSet(Set<String> classNameSet) {

        return classNameSet.stream()
                .map(PackageComponentScanner::parseClass)
                .filter(PackageComponentScanner::isComponent)
                .collect(Collectors.toSet());
    }

    /**
     * Parse className to Class
     * description: parseClass <br>
     * version: 1.0 <br>
     * date: 2022/5/9 15:39 <br>
     * author: Neil <br>
     *
     * @param className className
     * @return java.lang.Class<?>
     */
    private static Class<?> parseClass(String className) {
//        logger.debug("processing class: [{}]", className);
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.debug("error when load class: {}", className, e);
            throw new OmegaCommonException("Cannot load class: [{}]", className);
        }
    }

    /**
     * check if class is using {@code @Component} annotation, or it's annotation using {@code @Component}<br>
     * 由于会递归检查注解本身是否使用了@Component注解，所以使用递归深度计数器。
     * 虽然方法本身会过滤java包下的所有注解，但做为工具被使用时，若调用方编写了自定义注解，且这些注解也存在相互使用，仍然有栈溢出的风险。
     * 同时如果限定仅检查com.mostaron包，则将严重影响本工具的扩展性，基于这些原因，增加了递归深度计数器，以限定注解递归的深度。
     * description: isComponent <br>
     * version: 1.0 <br>
     * date: 2022/5/9 15:45 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @param recursionCount
     * @return boolean
     */
    private static boolean isComponent(Class<?> clazz, int recursionCount) {

        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations) {
            // 过滤Java自带的注解类
            if (annotation.annotationType().getPackageName().startsWith("java.")) {
                continue;
            }
            // 需要使用Annotation.annotationType()获取注解的Class对象
            // 若使用.getClass，将得到Jdk动态代理的对象，而非类对象
            if (annotation.annotationType() == Component.class) {
                return true;
            }
            if (recursionCount > MAX_RECURSION) {
                return false;
            }
            return isComponent(annotation.annotationType(), recursionCount + 1);

        }

        return false;
    }

    /**
     * 默认检查类，初始化递归计数器
     * description: isComponent <br>
     * version: 1.0 <br>
     * date: 2022/5/9 17:03 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @return boolean
     */
    private static boolean isComponent(Class<?> clazz) {
        return isComponent(clazz, 1);
    }

}
