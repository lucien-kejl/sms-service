package com.gykj.sms.exception;

import com.gykj.sms.entity.ResponseBean;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/14 10:10
 */
public class CommonJsonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResponseBean responseBean;

  public CommonJsonException(String retCode,String retMsg){
        responseBean = new ResponseBean(retCode,retMsg);
  }
}
