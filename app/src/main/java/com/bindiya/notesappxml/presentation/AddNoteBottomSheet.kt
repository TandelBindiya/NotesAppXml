package com.bindiya.notesappxml.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bindiya.notesappxml.R
import com.bindiya.notesappxml.databinding.BottomsheetAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNoteBottomSheet(private val onSave: (String,String,String) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomsheetAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            if (title.isBlank()) {
                binding.etTitle.error = getString(R.string.error_title_required)
                return@setOnClickListener
            }
            val body = binding.etBody.text.toString().trim()
            onSave(title,body,binding.etTags.text.toString())
            dismiss()
        }
        binding.btnCancel.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }
}
