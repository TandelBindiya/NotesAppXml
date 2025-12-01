package com.bindiya.notesappxml.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bindiya.notesappxml.core.model.Note
import com.bindiya.notesappxml.databinding.ItemNoteBinding
import com.google.android.material.chip.Chip

class NotesPagingAdapter(
    private val onClick: (Note) -> Unit,
    private val onDelete: (Note) -> Unit
) : PagingDataAdapter<Note, NotesPagingAdapter.VH>(DIFF) {

    interface ItemClickHandler {
        fun onDelete(note: Note)
    }

    private val clickHandler = object : ItemClickHandler {
        override fun onDelete(note: Note) {
            onDelete(note)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    inner class VH(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note?) {
            if (note == null) {
                binding.note = null
                binding.clickHandler = null
                binding.executePendingBindings()
                binding.root.setOnClickListener(null)
            } else {
                binding.note = note
                binding.tagsGroup.removeAllViews()
                note.tags.forEach { tag ->
                    val chip = Chip(binding.root.context).apply {
                        text = tag
                        isCheckable = false
                    }
                    binding.tagsGroup.addView(chip)
                }
                binding.clickHandler = clickHandler
                binding.executePendingBindings()
                binding.root.setOnClickListener { onClick(note) }
            }
        }
    }
}
