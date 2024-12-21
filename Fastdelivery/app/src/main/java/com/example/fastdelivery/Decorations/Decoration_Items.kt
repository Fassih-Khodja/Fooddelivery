package com.example.fastdelivery.Decorations

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Decoration_Items(private val spaceHeight: Int, private val recyclerview:RecyclerView) :RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        // Get the total number of items
        val itemCount = state.itemCount

        // Apply bottom space to every item except the last one
        if (position != itemCount - 1) {
            if ((recyclerview.layoutManager as LinearLayoutManager).orientation == LinearLayout.HORIZONTAL)
            outRect.right = spaceHeight
            else
                outRect.bottom = spaceHeight
        }
    }
}