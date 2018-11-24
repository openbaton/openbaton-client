/*
 * Copyright (c) 2016 Open Baton (http://www.openbaton.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.openbaton.sdk.api.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.Exception;
import java.lang.Throwable;

/**
 * OpenBaton SDK base Exception. Thrown to the caller of the library while using requester's
 * functions.
 */
public class SDKException extends Exception {

  private static final Gson gson = new GsonBuilder().create();

  public SDKException(String message) {
    this.reason = message;
  }

  public StackTraceElement[] getStackTraceElements() {
    return stackTraceElements;
  }

  public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
    this.stackTraceElements = stackTraceElements;
  }

  private StackTraceElement[] stackTraceElements;

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  private String reason;

  /**
   * Creates an SDKException with an Exception message as String.
   *
   * @param message custom message
   * @param stackTraceElements stack trace
   * @param reason reason for the exception
   */
  public SDKException(String message, StackTraceElement[] stackTraceElements, String reason) {
    super(message + ". The reason is: " + reason);
    this.stackTraceElements = stackTraceElements;
    this.reason = reason;
  }

  /**
   * Creates an SDKException with a throwable cause.
   *
   * @param cause the throwable cause
   */
  public SDKException(Throwable cause) {
    super(cause);
  }
}
