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

package com.raywenderlich.android.wendergram.provider

class PhotoProvider {
  val photos: List<String?> = listOf(
      // Load null url
      null,

      // Load error url
      "https://non-existing-url",

      // Load normal url
      "https://images.unsplash.com/photo-1554692918-08fa0fdc9db3",
      "https://images.unsplash.com/photo-1554335562-eb859255c3a6",
      "https://images.unsplash.com/photo-1553285991-4c74211f5097",
      "https://images.unsplash.com/photo-1553926297-57bb350c4f08",
      "https://images.unsplash.com/photo-1553052037-eb1fa8d3b1ce",
      "https://images.unsplash.com/photo-1553575297-0a5fede484ce",
      "https://images.unsplash.com/photo-1554185624-d49be4319189",
      "https://images.unsplash.com/photo-1554624219-4754a2ded336",
      "https://images.unsplash.com/photo-1555113559-b69b1301fc97",
      "https://images.unsplash.com/photo-1554558453-892a1014cf37",
      "https://images.unsplash.com/photo-1554521948-6891dbc1cde7",
      "https://images.unsplash.com/photo-1553991956-4b635766209b",
      "https://images.unsplash.com/photo-1554080714-db03fef5a3bc",
      "https://images.unsplash.com/photo-1552564330-b94011932351",
      "https://images.unsplash.com/photo-1554164648-e0175c054623",
      "https://images.unsplash.com/photo-1555375771-14b2a63968a9",
      "https://images.unsplash.com/photo-1554091780-bb3e99c4b02a",
      "https://images.unsplash.com/photo-1554235748-cfb041928319",
      "https://images.unsplash.com/photo-1553825250-f01a48d7e159",
      "https://images.unsplash.com/photo-1553762816-7bdcb6193d9f",
      "https://images.unsplash.com/photo-1553936359-666f20cd7d9d",
      "https://images.unsplash.com/photo-1554365102-59e02348ec3d",
      "https://images.unsplash.com/photo-1555352820-ff70b8c513ac",
      "https://images.unsplash.com/photo-1555402136-4d41bbec16e4",
      "https://images.unsplash.com/photo-1554857327-ee75df44276c",
      "https://images.unsplash.com/photo-1555089718-0ca5f5e42da7",
      "https://images.unsplash.com/photo-1553061607-537388067042",
      "https://images.unsplash.com/photo-1555387706-054c078fcb6d"
  )
}
