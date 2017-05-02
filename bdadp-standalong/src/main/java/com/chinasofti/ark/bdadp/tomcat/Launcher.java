package com.chinasofti.ark.bdadp.tomcat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.StandardJarScanFilter;

public class Launcher {

  private static File getRootFolder() {
    File root;
    String runningJarPath =
        Launcher.class.getClassLoader().getResource("").getPath().replaceAll("\\\\", "/");
    int lastIndexOf = runningJarPath.lastIndexOf("/target/classes");
    if (lastIndexOf < 0) { // product environment
      root = new File("");
    } else { // development environment
      root = new File(runningJarPath.substring(0, lastIndexOf));
    }
    System.out.println("application resolved root folder: " + root.getAbsolutePath());
    return root;
  }

  public static void main(String[] args) throws Exception {

    File root = getRootFolder();
    System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
    Tomcat tomcat = new Tomcat();
    Path tempPath = Files.createTempDirectory("tomcat-base-dir");
    tomcat.setBaseDir(tempPath.toString());

    Integer webPort = 8080;

    // Setup port
    if (System.getenv("BDADP_PORT") != null) {
      webPort = Integer.valueOf(System.getenv("BDADP_PORT"));
    } else if (args.length > 0) {
      if (!checkArgs(args)) {
        System.exit(1);
      } else {
        webPort = Integer.valueOf(args[0]);
      }
    }
    tomcat.setPort(webPort);

    // Setup webapp directory
    final String webapp = "webapp";
    File webappDir = new File(root.getAbsolutePath() + "/../" + webapp); // product environment
    if (!webappDir.exists()) { // development environment
      webappDir = new File(root.getAbsolutePath() + "/../bdadp-web/src/main/" + webapp);
      if (!webappDir.exists()) {
        throw new RuntimeException(
            "Can not found webapp in development environment, please check your project layout or name, default the webapp placed in the bdadp-web/src/main");
      }
    }

    StandardContext ctx = (StandardContext) tomcat.addWebapp("", webappDir.getAbsolutePath());
    ctx.setParentClassLoader(Launcher.class.getClassLoader());

    // Disable TLD scanning by default
    if (System.getProperty(Constants.SKIP_JARS_PROPERTY) == null
        && System.getProperty(Constants.SKIP_JARS_PROPERTY) == null) {
      System.out.println("disabling TLD scanning");
      StandardJarScanFilter jarScanFilter =
          (StandardJarScanFilter) ctx.getJarScanner().getJarScanFilter();
      jarScanFilter.setTldSkip("*");
    }

    System.out.println("configuring app with basedir: " + webappDir.getAbsolutePath());

    tomcat.start();
    tomcat.getServer().await();
  }

  private static boolean checkArgs(String[] args) {
    if (args.length > 1) {
      System.out.println(
          "more than one parameter, only accept one parameter for web port, default it 8080");
      return false;
    }
    if (args.length == 1) {
      try {
        return Integer.MIN_VALUE <= Integer.valueOf(args[0])
            && Integer.MAX_VALUE >= Integer.valueOf(args[0]);
      } catch (NumberFormatException e) {
        System.out.println("Invalid parameter, please input the web port, default is 8080.");
        return false;
      }
    }
    return true;
  }

}
