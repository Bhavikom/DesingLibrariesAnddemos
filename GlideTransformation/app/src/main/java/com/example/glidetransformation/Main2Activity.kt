package com.example.glidetransformation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.example.glide.MainAdapter

class Main2Activity : androidx.appcompat.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(context, mutableListOf(
                    MainAdapter.Type.Mask, MainAdapter.Type.NinePatchMask, MainAdapter.Type.CropTop, MainAdapter.Type.CropCenter, MainAdapter.Type.CropBottom, MainAdapter.Type.CropSquare, MainAdapter.Type.CropCircle, MainAdapter.Type.Grayscale,
                    MainAdapter.Type.Blur, MainAdapter.Type.SupportRSBlur, MainAdapter.Type.Toon, MainAdapter.Type.Sepia, MainAdapter.Type.Contrast, MainAdapter.Type.Invert, MainAdapter.Type.Pixel, MainAdapter.Type.Sketch, MainAdapter.Type.Swirl, MainAdapter.Type.Brightness,
                    MainAdapter.Type.Kuawahara, MainAdapter.Type.Vignette
            ))
        }
    }
}
