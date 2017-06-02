//package com.chinasofti.ark.bdadp.util.common;
//
//import java.util.Properties;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
//
///**
// * Created by water on 2017.5.11.
// */
//public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
//
//  private static Properties localProperties;
//
//  /**
//   * 重写父类方法，解密指定属性名对应的属性值
//   */
//  @Override
//  protected String convertProperty(String propertyName, String propertyValue) {
//    if (isEncryptPropertyVal(propertyName)) {
//      return DESUtil.getDecryptString(propertyValue);//调用解密方法
//    } else {
//      return propertyValue;
//    }
//  }
//
//
//  /**
//   * 判断属性值是否需要解密
//   */
//  private boolean isEncryptPropertyVal(String propertyName) {
//    if (propertyName.contains("username") || propertyName.contains("password")) {
//      return true;
//    } else {
//      return false;
//    }
//  }
//
//
//  @Override
//  protected void processProperties(
//      ConfigurableListableBeanFactory beanFactoryToProcess,
//      Properties props) throws BeansException {
//    super.processProperties(beanFactoryToProcess, props);
//    localProperties = props;
//  }
//
//  public static Properties getProperties() {
//    return localProperties;
//  }
//}
//
