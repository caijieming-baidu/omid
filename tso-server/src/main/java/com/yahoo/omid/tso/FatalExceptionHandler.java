/**
 * Copyright 2011-2015 Yahoo Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yahoo.omid.tso;

import com.lmax.disruptor.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FatalExceptionHandler implements ExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FatalExceptionHandler.class);

    Panicker panicker;

    FatalExceptionHandler(Panicker panicker) {
        this.panicker = panicker;
    }

    @Override
    public void handleEventException(Throwable ex,
                                     long sequence,
                                     Object event) {
        LOG.error("Uncaught exception throws for sequence {}, event {}",
                  new Object[] { sequence, event, ex });
        panicker.panic("Uncaught exception in disruptor thread", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        LOG.warn("Uncaught exception shutting down", ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        panicker.panic("Uncaught exception starting up", ex);
    }
}
