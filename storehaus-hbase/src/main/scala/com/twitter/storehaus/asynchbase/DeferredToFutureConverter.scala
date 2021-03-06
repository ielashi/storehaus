/*
 * Copyright 2016 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twitter.storehaus.asynchbase

import com.stumbleupon.async.{Callback, Deferred}
import com.twitter.util.{Throw, Return, Promise, Future}

private[asynchbase] object DeferredToFutureConverter {
  def toFuture[T](d: Deferred[T]): Future[T] = {
    val p = new Promise[T]()
    d.addCallbacks(new Callback[Unit, T] {
      override def call(t: T): Unit = p.updateIfEmpty(Return(t))
    }, new Callback[Unit, Exception] {
      override def call(e: Exception): Unit = p.updateIfEmpty(Throw(e))
    })
    p
  }
}
