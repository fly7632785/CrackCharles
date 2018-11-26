package com.jafir.cracklib;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * 破解charles:
 * 1、官网下载dmg安装，然后打开包内容，然后找到charles.jar包
 * 2、jd-GUI打开jar包，分析，通过字符串文字来找到对应的类、方法等，一般都是混淆的要耐心寻找
 * 3、使用javassit框架来进行jar包的class文件修改（javassit主要就是可以修改字节码文件），一般都是一个变量来控制的，修改其方法的返回值为true即可
 * 4、然后重新生成class文件流，然后写入到对应的包名对应的文件目录下
 * 5、然后调用jar uvf xxx.jar xxx/xxx.class来修改jar包下面的class类，然后再替换到原来的app的包内容下即可
 */
public class MyClass {
    public static final String PKG_NAME = "com.xk72.charles";
    public static final String CLASS_NAME = "kKPk";
    public static final String JAR_PATH = "/Users/jafir/Documents/ASworkspace/CrackCharles/";
    public static final String JAR_NAME = "charles.jar";

    public static void main(String[] a) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(JAR_PATH + JAR_NAME);
            }
        }).map(new Function<String, byte[]>() {
            @Override
            public byte[] apply(String jarPath) throws Exception {
                ClassPool classPool = ClassPool.getDefault();
                classPool.insertClassPath(jarPath);

                CtClass ctClass = classPool.get(PKG_NAME + "." + CLASS_NAME);
                CtMethod ctMethod = ctClass.getDeclaredMethod("lcJx", null);
                ctMethod.setBody("{return true;}");
                CtMethod ctMethod1 = ctClass.getDeclaredMethod("JZlU", null);
                ctMethod1.setBody("{return \"Cracked by Jafir\";}");
                return ctClass.toBytecode();
            }
        }).map(new Function<byte[], String>() {
            @Override
            public String apply(byte[] bytes) throws Exception {
                String classPath = PKG_NAME.replace(".", "/") + "/";
                File dirFir = new File(JAR_PATH + classPath + CLASS_NAME + ".class");
                if (!dirFir.getParentFile().exists()) {
                    dirFir.getParentFile().mkdirs();
                }

                FileOutputStream fileOutputStream = new FileOutputStream(dirFir);
                fileOutputStream.write(bytes);
                return dirFir.getAbsolutePath();
            }
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                String classPath = PKG_NAME.replace(".", "/") + "/" + CLASS_NAME + ".class";
                Process process = Runtime.getRuntime().exec("jar uvf " + JAR_NAME + " " + classPath);
                int status = process.waitFor();
                return status;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("status:" + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("throwable:" + throwable.getMessage());
            }
        });
    }
}
