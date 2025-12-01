package com.bindiya.notesappxml.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bindiya.notesappxml.databinding.ItemLoadStateBinding

class NotesLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<NotesLoadStateAdapter.LoadStateVH>() {

    override fun onBindViewHolder(holder: LoadStateVH, loadState: LoadState) = holder.bind(loadState)
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateVH {
        val binding = ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateVH(binding, retry)
    }

    class LoadStateVH(private val binding: ItemLoadStateBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init { binding.btnRetry.setOnClickListener { retry() } }
        fun bind(loadState: LoadState) {
            binding.progress.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            binding.btnRetry.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            if (loadState is LoadState.Error) binding.tvError.text = loadState.error.localizedMessage ?: "Error"
        }
    }
}
