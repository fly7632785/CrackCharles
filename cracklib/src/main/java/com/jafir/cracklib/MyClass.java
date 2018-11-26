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
