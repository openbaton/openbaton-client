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

package org.openbaton.cli.model;

import java.lang.reflect.Method;

/**
 * Created by lto on 14/07/15.
 *
 * <p>This class represents a user's command from the cli.
 */
public class Command {

  private Class clazz;
  private Object instance;
  private Method method;
  private Class[] params;

  public Command(Object instance, Method method, Class[] params, Class clazz) {
    this.instance = instance;
    this.method = method;
    this.params = params;
    this.clazz = clazz;
  }

  public Class getClazz() {
    return clazz;
  }

  public void setClazz(Class clazz) {
    this.clazz = clazz;
  }

  public Object getInstance() {
    return instance;
  }

  public void setInstance(Object instance) {
    this.instance = instance;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Class[] getParams() {
    return params;
  }

  public void setParams(Class[] params) {
    this.params = params;
  }
}
