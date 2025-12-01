package com.bindiya.notesappxml.presentation.ui


import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

@BindingAdapter("tags")
fun setTags(textView: TextView, tags: List<String>?) {
    textView.text = tags?.joinToString(", ") ?: ""
}

@BindingAdapter("tagList", "chipGroupOnClick", requireAll = false)
fun setTagChips(chipGroup: ChipGroup, tags: List<String>?, listener: TagClickListener?) {
    // Remove all but preserve selection behavior if needed
    chipGroup.removeAllViews()
    if (tags.isNullOrEmpty()) return
    val ctx = chipGroup.context
    tags.forEach { tag ->
        val chip = Chip(ctx).apply {
            text = tag
            isCheckable = true
        }
        chip.setOnClickListener { listener?.onTagClicked(tag) }
        chipGroup.addView(chip)
    }
}

interface TagClickListener {
    fun onTagClicked(tag: String)
}
