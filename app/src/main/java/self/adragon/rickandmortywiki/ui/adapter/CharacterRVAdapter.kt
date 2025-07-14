package self.adragon.rickandmortywiki.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import self.adragon.rickandmortywiki.R
import self.adragon.rickandmortywiki.data.model.Character

class CharacterRVAdapter(
    private val context: Context,
    private val onClick: (Character) -> Unit
) :
    PagingDataAdapter<Character, CharacterRVAdapter.CharactersVH>(DiffCallback) {

    inner class CharactersVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemCardView: CardView = itemView.findViewById(R.id.itemCardView)

        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val genderTextView: TextView = itemView.findViewById(R.id.genderTextView)
        val speciesTextView: TextView = itemView.findViewById(R.id.speciesTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val statusIconImageView: ImageView = itemView.findViewById(R.id.statusIconImageView)
        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_list_item, parent, false)
        return CharactersVH(itemView)
    }

    override fun onBindViewHolder(holder: CharactersVH, position: Int) {
        try {
            val item = getItem(position) as Character
            holder.itemCardView.setOnClickListener { onClick(item) }

            holder.apply {
                nameTextView.text = item.name
                genderTextView.text = item.gender
                speciesTextView.text = item.species
                statusTextView.text = item.status

                val statusImage = when (item.status) {
                    "Alive" -> R.drawable.green_circle_shape
                    "Dead" -> R.drawable.red_circle_shape
                    else -> R.drawable.yellow_circle_shape // "Unknown" or garbage from API
                }
                statusIconImageView.setBackgroundResource(statusImage)
                Picasso.with(context).load(item.image).into(avatarImageView)
            }
        } catch (e: Exception) {
            Log.e("mytag", "CharactersVH.onBindViewHolder:\n position = $position\n\n ${e.message}")

        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.name == newItem.name &&
                oldItem.status == newItem.status &&
                oldItem.species == newItem.species &&
                oldItem.type == newItem.type &&
                oldItem.gender == newItem.gender &&
                oldItem.origin == newItem.origin &&
                oldItem.location == newItem.location &&
                oldItem.image == newItem.image &&
//                oldItem.episodes.containsAll(newItem.episodes) && // TODO u
                oldItem.url == newItem.url &&
                oldItem.created == newItem.created
}