/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.wendergram.glide

import android.os.Handler
import android.os.Looper
import okhttp3.HttpUrl

class DispatchingProgressManager internal constructor() : ResponseProgressListener {

  companion object {
    private val PROGRESSES = HashMap<String?, Long>() //1
    private val LISTENERS = HashMap<String?, UIonProgressListener>() //2

    internal fun expect(url: String?, listener: UIonProgressListener) { //3
      LISTENERS[url] = listener
    }

    internal fun forget(url: String?) { //4
      LISTENERS.remove(url)
      PROGRESSES.remove(url)
    }
  }

  private val handler: Handler = Handler(Looper.getMainLooper()) //5

  override fun update(url: HttpUrl, bytesRead: Long, contentLength: Long) {
    val key = url.toString()
    val listener = LISTENERS[key] ?: return //6
    if (contentLength <= bytesRead) { //7
      forget(key)
    }
    if (needsDispatch(key, bytesRead, contentLength, listener.granularityPercentage)) { //8
      handler.post { listener.onProgress(bytesRead, contentLength) }
    }
  }

  private fun needsDispatch(key: String, current: Long, total: Long, granularity: Float): Boolean {
    if (granularity == 0f || current == 0L || total == current) {
      return true
    }
    val percent = 100f * current / total
    val currentProgress = (percent / granularity).toLong()
    val lastProgress = PROGRESSES[key]
    return if (lastProgress == null || currentProgress != lastProgress) { //9
      PROGRESSES[key] = currentProgress
      true
    } else {
      false
    }
  }
}
