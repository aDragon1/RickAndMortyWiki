package self.adragon.rickandmortywiki.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import self.adragon.rickandmortywiki.R

class CharacterLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharacterLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_load_state_item, parent, false)
        return LoadStateViewHolder(view, retry)
    }

    class LoadStateViewHolder(
        itemView: View,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        private val retryButton: Button = itemView.findViewById(R.id.retryButton)

        init {
            retryButton.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            retryButton.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
        }
    }
}
