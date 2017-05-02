package com.chinasofti.ark.bdadp.util.resource;

/**
 * Created by wumin on 2016/9/21.
 */
public class SimpleClassLoader {

//    public static boolean loadClass(final ClassLoader cl, String fileName){
//        if (fileName.endsWith(".class")) {
//            fileName = fileName.replace('/', '.');
//            fileName = fileName.replace('\\', '.');
//            fileName = fileName.substring(0, fileName.length() - 6);
//            if (fileName.startsWith(".")) {
//                fileName = fileName.substring(1);
//            }
//            Class<?> clz = null;
//            try {
//                clz = cl.loadClass(fileName);
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e.getMessage());
//            }
//            SystemContext.setComponentClass(clz.getName(), clz);
//        }
//    }
//
//    public static boolean loadJar(final File file) {
//        loadJar(file,Thread.currentThread().getContextClassLoader());
//    }
//
//    public static boolean loadJar(final File file, final ClassLoader cl){
//        JarFile jar = null;
//        try {
//            jar = new JarFile(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Enumeration<?> list = jar.entries(); list.hasMoreElements(); ) {
//            ZipEntry entry = (ZipEntry) list.nextElement();
//            if (!entry.isDirectory()) {
//                loadClass(cl, entry.getName());
//            }
//        }
//    }
//
//    public static boolean loadJars(final File dir){
//        File[] jars = dir.listFiles(new FilenameFilter() {
//            @Override public boolean accept(File dir, String name) {
//                return false;
//            }
//        });
//
//        for (File jar : jars) {
//            if (!loadJar(jar))
//               throw new RuntimeException("Load jar failed, jar file is "+jar.getAbsolutePath());
//        }
//        return true;
//    }
}
