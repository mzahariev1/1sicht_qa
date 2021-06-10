package edu.kit.pse.a1sicht.ui.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Abstract ViewHolder class to be used for an adapter
 * with different types of items (different views)
 *
 * @author Hristo Klechorov
 */
abstract class BaseViewHolder<T>(itemView:View) : RecyclerView.ViewHolder(itemView) {

    /**
     * In the bind function will be implemented how the view holder
     * will assign the information from the data objects to the corresponding views
     */
    abstract fun bind(item: T)
}