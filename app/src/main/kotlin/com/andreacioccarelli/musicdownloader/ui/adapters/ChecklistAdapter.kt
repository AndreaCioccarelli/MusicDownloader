package com.andreacioccarelli.musicdownloader.ui.adapters

import android.app.Activity
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.andreacioccarelli.musicdownloader.App.Companion.checklist
import com.andreacioccarelli.musicdownloader.R
import com.andreacioccarelli.musicdownloader.ui.activities.MainActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.find

/**
 *  Designed and developed by Andrea Cioccarelli
 */

class ChecklistAdapter(private val activity: Activity) : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {

    val data = checklist
            .getAll()
            .toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        Glide.with(activity)
                .load(data[holder.adapterPosition].thumbnailLink)
                .thumbnail(0.1F)
                .into(holder.icon)
        
        holder.title.text = data[holder.adapterPosition].title

        with(holder.card) {
            setOnClickListener {
                val ref = (activity as MainActivity)

                val search = ref.find<TextView>(R.id.search)
                val fab = ref.find<FloatingActionButton>(R.id.fab)
                val rv = ref.find<RecyclerView>(R.id.resultsRecyclerView)

                search.text = data[holder.adapterPosition].title
                fab.performClick()
                rv.smoothScrollToPosition(0)
                ref.checklistDialog.dismiss()
            }

            setOnLongClickListener {
                checklist.remove(data[holder.adapterPosition])
                data.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)

                if (data.isEmpty()) {
                    // If the last remaining item is removed, safely close the dialog
                    val ref = (activity as? MainActivity)
                    ref?.checklistDialog?.dismiss()
                }
                true
            }
        }

        Handler().post {
            // Maybe a title has just 1 line of text, and so we should give it
            // a normal dimension, if compared to longer ones
            if (holder.title.lineCount == 1) {
                holder.title.height = activity.resources.getDimension(R.dimen.result_thumb_width).toInt()
            }

            holder.titleLayout.visibility = View.VISIBLE
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var icon: ImageView = v.findViewById(R.id.icon)
        var titleLayout: RelativeLayout = v.findViewById(R.id.titleLayout)
        var title: TextView = v.findViewById(R.id.title)
        var card: CardView = v.findViewById(R.id.card)
    }
}