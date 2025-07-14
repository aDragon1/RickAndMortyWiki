package self.adragon.rickandmortywiki.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import self.adragon.rickandmortywiki.R
import self.adragon.rickandmortywiki.data.model.Character
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var gson: Gson

    private lateinit var backImageButton: ImageButton
    private lateinit var characterImageView: ImageView

    private lateinit var nameTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var originTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var createdTextView: TextView
    private lateinit var episodesTextView: TextView


    /*
    TODO (#4):
        Better use RecyclerView instead of ListView, but LV don't require to create an
            adapter and just string isn't so hard for LV, I guess
     */
    private lateinit var episodesListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        // TODO (#2): Better pass `id` instead of serialized structure
        //              Look TODO (#1) for an a explanation
        val characterExtra = intent.getStringExtra("clicked_character")
        try {
            val character = gson.fromJson(characterExtra, Character::class.java)
            initViews()
            fillViews(character)
        } catch (e: Exception) {
            Log.e("mytag", "Exception in CharacterDetailsActivity:\n ${e.message}")
            finish()
            return
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fillViews(character: Character?) {
        if (character == null) {
            nameTextView.text = "Error"
            return
        }

        character.apply {
            nameTextView.text = name
            statusTextView.text = "$status · $gender  · $species"
            typeTextView.text = "Type: $type"
            originTextView.text = "Origin: ${origin.name}"
            locationTextView.text = "Location: ${location.name}"
            createdTextView.text = "Created at: ${created.split('T').first()}"
            episodesTextView.text = "Episodes count ${episode.size}:"

            Picasso.with(this@CharacterDetailsActivity).load(image).into(characterImageView)
        }

        val episodesNumber = character.episode.map {
            val n = it.split('/').last()
            "Episode №$n"
        }
        val episodesAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, episodesNumber)
        episodesListView.adapter = episodesAdapter
    }

    private fun initViews() {
        backImageButton = findViewById(R.id.backImageButton)
        characterImageView = findViewById(R.id.characterImageView)
        nameTextView = findViewById(R.id.nameTextView)
        statusTextView = findViewById(R.id.statusTextView)
        typeTextView = findViewById(R.id.typeTextView)
        originTextView = findViewById(R.id.originTextView)
        locationTextView = findViewById(R.id.locationTextView)
        createdTextView = findViewById(R.id.createdTextView)
        episodesTextView = findViewById(R.id.episodesTextView)
        episodesListView = findViewById(R.id.episodesListView)

        backImageButton.setOnClickListener { finish() }
    }
}