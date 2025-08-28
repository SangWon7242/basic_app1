package com.sbs.tutorial.app1.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class RsData<T> {
  private final String resultCode; // 결과 코드(S-1, F-1
  private final String msg; // 메시지
  private final T body; // 실제 반환 데이터

  public boolean isSuccess() {
    return resultCode.startsWith("S-");
  }

  public boolean isFail() {
    return !isSuccess();
  }
}
