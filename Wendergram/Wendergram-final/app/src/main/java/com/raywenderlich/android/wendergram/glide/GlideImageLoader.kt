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

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class GlideImageLoader(
    private val mImageView: ImageView?,
    private val mProgressBar: ProgressBar?) { //1

  fun load(url: String?, options: RequestOptions?) { //2
    if (options == null) return

    onConnecting() //3

    DispatchingProgressManager.expect(url, object : UIonProgressListener { //4

      override val granularityPercentage: Float //5
        get() = 1.0f

      override fun onProgress(bytesRead: Long, expectedLength: Long) {
        if (mProgressBar != null) {
          mProgressBar.progress = (100 * bytesRead / expectedLength).toInt() //6
        }
      }
    })

    Glide.with(mImageView!!.context) //7
        .load(url)
        .apply(options) //8
        .listener(object : RequestListener<Drawable> { //9
          override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
              isFirstResource: Boolean): Boolean {
            DispatchingProgressManager.forget(url)
            onFinished()
            return false
          }

          override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
              dataSource: DataSource?, isFirstResource: Boolean): Boolean {

            DispatchingProgressManager.forget(url)
            onFinished()
            return false
          }
        })
        .into(mImageView)
  }


  private fun onConnecting() {
    if (mProgressBar != null) mProgressBar.visibility = View.VISIBLE
  }

  private fun onFinished() {
    if (mProgressBar != null && mImageView != null) {
      mProgressBar.visibility = View.GONE
      mImageView.visibility = View.VISIBLE
    }
  }
}
