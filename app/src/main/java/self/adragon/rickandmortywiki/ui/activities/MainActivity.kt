package self.adragon.rickandmortywiki.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import self.adragon.rickandmortywiki.R
import self.adragon.rickandmortywiki.ui.adapter.CharacterLoadStateAdapter
import self.adragon.rickandmortywiki.ui.adapter.CharacterRVAdapter
import self.adragon.rickandmortywiki.ui.fragments.FilterFragment
import self.adragon.rickandmortywiki.ui.viewmodels.CharacterViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var gson: Gson

    private lateinit var mainSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var emptyListTextView: TextView

    private lateinit var characterNameEditText: EditText
    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var filterImageButton: ImageButton

    private val characterViewModel: CharacterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainSwipeRefreshLayout = findViewById(R.id.mainSwipeRefreshLayout)
        emptyListTextView = findViewById(R.id.emptyListTextView)
        characterNameEditText = findViewById(R.id.characterNameEditText)
        characterRecyclerView = findViewById(R.id.characterRecyclerView)
        filterImageButton = findViewById(R.id.filterImageButton)

        mainSwipeRefreshLayout.isRefreshing = true

        characterNameEditText.addTextChangedListener {
            characterViewModel.setSearchQueryName("${characterNameEditText.text}")
        }

        val characterAdapter = CharacterRVAdapter(applicationContext) { character ->
            val i = Intent(this, CharacterDetailsActivity::class.java)
            i.putExtra("clicked_character", gson.toJson(character))
            startActivity(i)
        }.apply {
            withLoadStateFooter(
                footer = CharacterLoadStateAdapter { this.retry() }
            )
            addLoadStateListener { loadState ->
                    val visibility = if (itemCount == 0 && !mainSwipeRefreshLayout.isRefreshing) View.VISIBLE else View.GONE
                emptyListTextView.visibility = visibility
            }
        }

        characterRecyclerView.layoutManager = GridLayoutManager(this, 2)
        characterRecyclerView.adapter = characterAdapter

        lifecycleScope.launch {
            characterViewModel.characters.collectLatest { pagingCharacters ->
                characterAdapter.submitData(pagingCharacters)

                mainSwipeRefreshLayout.isRefreshing = false
            }
        }

        filterImageButton.setOnClickListener {
            FilterFragment().show(supportFragmentManager, "FilterDialog")
        }
        mainSwipeRefreshLayout.setOnRefreshListener {
            Log.d("mytag", "Refresh")
            characterAdapter.refresh()
        }
    }
}