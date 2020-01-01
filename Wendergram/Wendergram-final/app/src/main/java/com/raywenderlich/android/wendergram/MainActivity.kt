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

package com.raywenderlich.android.wendergram

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.raywenderlich.android.wendergram.photo.PhotoAdapter
import com.raywenderlich.android.wendergram.provider.PhotoProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private val photoProvider = PhotoProvider()
  private var photoAdapter: PhotoAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    photoAdapter = PhotoAdapter(photoProvider.photos)
    photoAdapter?.setItemClickListener { it ->
      startActivity(EditPhotoActivity.newIntent(this@MainActivity, it))
    }

    rvPhotos.layoutManager = GridLayoutManager(this, 3)
    rvPhotos.adapter = photoAdapter

    val profilePicUrl = "https://source.unsplash.com/random"
    loadProfilePic(profilePicUrl)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when {
      item?.itemId == R.id.clear_cache -> {
        clearCache()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun loadProfilePic(profilePicUrl: String) {
    Glide.with(this) //1
        .load(profilePicUrl)
        .placeholder(R.drawable.ic_profile_placeholder)
        .error(R.drawable.ic_profile_placeholder)
        .skipMemoryCache(true) //2
        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
        .transform(CircleCrop()) //4
        .into(ivProfile)
  }

  private fun clearCache() {
    Thread(Runnable {
      Glide.get(this).clearDiskCache()
    }).start()
    Glide.get(this).clearMemory()
  }
}
