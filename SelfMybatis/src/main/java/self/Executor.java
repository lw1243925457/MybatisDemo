/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package self;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author liuwei
 */
public class Executor {

    private static final StatementHandler statementHandler = new StatementHandler();
    private static final ResultHandler resultHandler = new ResultHandler();

    public static Object executor(SelfConfiguration config, Object proxy, Method method, Object[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println("executor");
        try (Connection conn = config.getDataSource().getConnection()) {
            final String classPath = method.getDeclaringClass().getPackageName();
            final String className = method.getDeclaringClass().getName();
            final String methodName = method.getName();
            final String id = StringUtils.joinWith(".", classPath, className, methodName);
            ResultSet resultSet = statementHandler.prepare(id, conn, args, config);
            return resultHandler.parse(id, resultSet, config);
        }
    }
}
