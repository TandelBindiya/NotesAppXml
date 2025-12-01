package com.bindiya.notesappxml.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bindiya.notesappxml.databinding.ActivityMainBinding
import com.bindiya.notesappxml.presentation.viewmodel.TagsViewModel
import com.bindiya.notesappxml.presentation.adapters.NotesLoadStateAdapter
import com.bindiya.notesappxml.presentation.adapters.NotesPagingAdapter
import com.bindiya.notesappxml.presentation.viewmodel.NotesViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: NotesViewModel by viewModels()
    private lateinit var adapter: NotesPagingAdapter
    private val tagsViewModel: TagsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.lifecycleOwner = this
        setUpListeners()
        setUpRecyclerView()
        setUpObservers()
    }

    private fun setUpListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                viewModel.setQuery(q); return true
            }

            override fun onQueryTextChange(q: String?): Boolean {
                viewModel.setQuery(q); return true
            }
        })

        binding.fabAdd.setOnClickListener {
            val sheet = AddNoteBottomSheet { title, body, tags ->
                viewModel.insert(title, body, tags)
            }
            sheet.show(supportFragmentManager, "add_note")
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            viewModel.notes.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            tagsViewModel.tags.collectLatest { tags ->
                populateChips(tags)
            }
        }
    }

    private fun setUpRecyclerView() {
        adapter = NotesPagingAdapter(
            onClick = {  },
            onDelete = { note -> viewModel.delete(note.id) }
        )

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = NotesLoadStateAdapter { adapter.retry() },
            footer = NotesLoadStateAdapter { adapter.retry() }
        )
    }

    private fun populateChips(tags: List<String>) {
        val chipGroup = binding.chipGroup
        chipGroup.removeAllViews()
        if (tags.isEmpty())return

        val allChip = Chip(this).apply {
            text = "All"
            isCheckable = true
            isChecked = viewModel.selectedTag.value == null
            setOnClickListener { viewModel.clearSelectedTag() }
        }
        chipGroup.addView(allChip)

        tags.forEach { tag ->
            val chip = Chip(this).apply {
                text = tag
                isCheckable = true
                isChecked = viewModel.selectedTag.value == tag
                setOnClickListener {
                    val cur = viewModel.selectedTag.value
                    if (cur == tag) viewModel.clearSelectedTag() else viewModel.filterByTag(tag)
                }
            }
            chipGroup.addView(chip)
        }
        chipGroup.isSingleSelection = true
    }
}