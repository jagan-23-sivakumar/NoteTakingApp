package com.mastercoding.notetakingapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import com.mastercoding.notetakingapp.model.Note
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.mastercoding.notetakingapp.MainActivity
import com.mastercoding.notetakingapp.R
import com.mastercoding.notetakingapp.adapter.NoteAdapter
import com.mastercoding.notetakingapp.databinding.FragmentHomeBinding
import com.mastercoding.notetakingapp.databinding.FragmentUpdateNoteBinding
import com.mastercoding.notetakingapp.viewmodel.NoteViewModel


class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding : FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel : NoteViewModel

    private lateinit var currentNote : Note

    // Since the Update Note Fragment contains arguments in nav_graph
    private val args: UpdateNoteFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)
        binding.etNoteBodyUpdate.setText(currentNote.noteBody)

        // if the user update the note
        binding.fabDone.setOnClickListener{
            val title = binding.etNoteTitleUpdate.text.toString().trim()
            val body = binding.etNoteBodyUpdate.text.toString().trim()

            if (title.isNotEmpty()){
                val note = Note(currentNote.id,title, body)
                notesViewModel.updateNote(note)
                view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
            }else{
                Toast.makeText(
                    context,
                    "Please enter note Title",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteNote(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("You want to delete this Note?")
            setPositiveButton("Delete"){_,_ ->
                notesViewModel.deleteNote(currentNote)

                view?.findNavController()?.navigate(
                    R.id.action_updateNoteFragment_to_homeFragment
                )

            }
            setNegativeButton("Cancel", null)
        }.create().show()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete -> {
                deleteNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}