package com.speakupcambridge.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class BeanUtils {

  private static class NullAwareBeanUtilsBean extends BeanUtilsBean {
    @Override
    public void copyProperty(Object dest, String name, Object value)
        throws IllegalAccessException, InvocationTargetException {
      if (Objects.nonNull(value)) {
        super.copyProperty(dest, name, value);
      }
    }
  }

  public static void copyNonNullProperties(Object dest, Object orig) {
    BeanUtilsBean nullAwareBeanUtilsBean = new NullAwareBeanUtilsBean();
    try {
      nullAwareBeanUtilsBean.copyProperties(dest, orig);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Could not copy properties", e);
    }
  }
}
