package self.adragon.rickandmortywiki.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import self.adragon.rickandmortywiki.R
import self.adragon.rickandmortywiki.ui.adapter.CharacterRVAdapter
import self.adragon.rickandmortywiki.ui.fragments.FilterFragment
import self.adragon.rickandmortywiki.ui.viewmodels.CharacterViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var gson: Gson

    private lateinit var characterNameEditText: EditText
    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var filterImageButton: ImageButton


    private val characterViewModel: CharacterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        characterNameEditText = findViewById(R.id.characterNameEditText)
        characterRecyclerView = findViewById(R.id.characterRecyclerView)
        filterImageButton = findViewById(R.id.filterImageButton)


        characterNameEditText.addTextChangedListener {
            characterViewModel.setSearchQueryName("${characterNameEditText.text}")
        }

        val characterAdapter = CharacterRVAdapter(applicationContext) { character ->
            val i = Intent(this, CharacterDetailsActivity::class.java)
            i.putExtra("clicked_character", gson.toJson(character))
            startActivity(i)
        }

        characterRecyclerView.layoutManager = GridLayoutManager(this, 2)
        characterRecyclerView.adapter = characterAdapter

        lifecycleScope.launch {
            characterViewModel.characters.collectLatest { pagingCharacters ->
                characterAdapter.submitData(pagingCharacters)
            }
        }

        filterImageButton.setOnClickListener {
            FilterFragment().show(supportFragmentManager, "FilterDialog")
        }
    }
}