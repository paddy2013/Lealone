/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.transaction;

import java.lang.Thread.UncaughtExceptionHandler;

import org.lealone.command.router.Router;
import org.lealone.engine.Constants;
import org.lealone.engine.Session;
import org.lealone.engine.SysProperties;
import org.lealone.engine.SystemDatabase;
import org.lealone.fs.FileUtils;
import org.lealone.mvstore.MVStore;
import org.lealone.mvstore.MVStoreCache;
import org.lealone.mvstore.MVStoreTool;

public class TransactionManager {
    private static MVStore store;
    private static String hostAndPort;

    public static String getHostAndPort() {
        return hostAndPort;
    }

    public static synchronized void init(String baseDir, String host, int port) {
        if (store != null)
            return;

        hostAndPort = host + ":" + port;

        Router r = Session.getRouter();
        if (!(r instanceof TransactionalRouter)) {
            Session.setRouter(new TransactionalRouter(r));
        }

        initStore(baseDir);

        TransactionStatusTable.init(store);
        TimestampServiceTable.init(store);

        if (Session.isClusterMode()) {
            TransactionValidator.getInstance().start();
        }
    }

    public static synchronized void close() {
        if (store == null)
            return;

        store = null;
        hostAndPort = null;
        if (Session.isClusterMode()) {
            TransactionValidator.getInstance().close();
        }
        store.close();
    }

    private static void initStore(String baseDir) {
        if (baseDir == null) {
            baseDir = SysProperties.getBaseDir();

            if (baseDir == null)
                baseDir = ".";
        }

        String fileName = FileUtils.toRealPath(baseDir + "/" + SystemDatabase.NAME) + Constants.SUFFIX_MV_FILE;

        if (MVStoreCache.getMVStore(fileName) != null) {
            store = MVStoreCache.getMVStore(fileName);
            return;
        }
        MVStoreTool.compactCleanUp(fileName);
        MVStore.Builder builder = new MVStore.Builder();
        builder.fileName(fileName);

        // possibly create the directory
        boolean exists = FileUtils.exists(fileName);
        if (exists && !FileUtils.canWrite(fileName)) {
            // read only
        } else {
            String dir = FileUtils.getParent(fileName);
            FileUtils.createDirectories(dir);
        }
        builder.backgroundExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //TODO
            }
        });

        store = builder.open();
    }
}
