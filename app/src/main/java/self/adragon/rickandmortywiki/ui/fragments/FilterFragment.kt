package self.adragon.rickandmortywiki.ui.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import self.adragon.rickandmortywiki.R
import self.adragon.rickandmortywiki.data.model.CharacterSearchQuery
import self.adragon.rickandmortywiki.ui.viewmodels.CharacterViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FilterFragment @Inject constructor() : DialogFragment(R.layout.filter_fragment),
    View.OnClickListener {

    val characterViewModel: CharacterViewModel by activityViewModels()

    private lateinit var backImageButton: ImageButton

    private lateinit var statusSpinner: Spinner
    private lateinit var speciesEditText: EditText
    private lateinit var typeEditText: EditText
    private lateinit var genderSpinner: Spinner

    private lateinit var applyFiltersButton: Button
    private lateinit var resetFiltersButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.TransparentDialogTheme)
        dialog.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.5).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setGravity(Gravity.END)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        lifecycleScope.launch {
            characterViewModel.currentQuery.collectLatest { q ->
                setViews(q)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backImageButton -> dismiss()
            R.id.applyFiltersButton -> {
                val statusSelected = "${statusSpinner.selectedItem}"
                val genderSelected = "${genderSpinner.selectedItem}"

                val status = "${(if (statusSelected.isBlank()) null else statusSelected)}"
                val gender = "${(if (genderSelected.isBlank()) null else genderSelected)}"
                val species = "${speciesEditText.text}"
                val type = "${typeEditText.text}"

                characterViewModel.setSearchQueryFilter(status, species, type, gender)
            }

            R.id.resetFiltersButton -> characterViewModel
                .setSearchQueryFilter(null, null, null, null)
        }
    }

    private fun initViews(v: View) {
        backImageButton = v.findViewById(R.id.backImageButton)

        statusSpinner = v.findViewById(R.id.statusSpinner)
        speciesEditText = v.findViewById(R.id.speciesEditText)
        typeEditText = v.findViewById(R.id.typeEditText)
        genderSpinner = v.findViewById(R.id.genderSpinner)

        applyFiltersButton = v.findViewById(R.id.applyFiltersButton)
        resetFiltersButton = v.findViewById(R.id.resetFiltersButton)
    }

    private fun setViews(q: CharacterSearchQuery) {
        // Set buttons click listeners
        backImageButton.setOnClickListener(this)
        applyFiltersButton.setOnClickListener(this)
        resetFiltersButton.setOnClickListener(this)

        // Init spinners
        val statusesAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.statuses,
            android.R.layout.simple_spinner_item
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_item) }

        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.genders,
            android.R.layout.simple_spinner_item
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_item) }
        statusSpinner.adapter = statusesAdapter
        genderSpinner.adapter = genderAdapter

        // Set data
        speciesEditText.setText(q.species ?: "")
        typeEditText.setText(q.type ?: "")

        statusSpinner.setSelection(statusesAdapter.getPosition(q.status))
        genderSpinner.setSelection(genderAdapter.getPosition(q.gender))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}