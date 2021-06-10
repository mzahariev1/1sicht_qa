package edu.kit.pse.a1sicht.ui.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Employee
import kotlinx.android.synthetic.main.item_request.view.*
import java.lang.IllegalArgumentException

/**
 * Adapter for displaying requests for employee status in a RecyclerView
 *
 * @author Hristo Klechorov
 */
class RequestsAdapter(var requests: List<Employee>) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private lateinit var mListener: OnItemClickListener

    /**
     * ItemClickListener interface
     */
    interface OnItemClickListener {
        fun onAcceptClick(position: Int)
        fun onDeclineClick(position: Int)
    }

    /**
     * Method for setting an itemClick listener
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflatedView = parent.inflate(R.layout.item_request)
        return RequestsHolder(inflatedView, mListener)
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder) {
            is RequestsHolder -> holder.bind(requests[position])
            else -> throw IllegalArgumentException("View binding failed")
        }
    }

    /**
     * The ViewHolder for the Employee data object (the only data object in this adapter)
     *
     * @author Hristo Klechorov
     */
    class RequestsHolder(v:View, listener: OnItemClickListener) : BaseViewHolder<Employee>(v) {
        private var view: View = v

        init {
            val acceptButton = view.btn_approve
            val declineButton = view.btn_deny

            acceptButton.setOnClickListener {
                listener.onAcceptClick(adapterPosition)
            }

            declineButton.setOnClickListener {
                listener.onDeclineClick(adapterPosition)
            }
        }

        override fun bind(item: Employee) {
            view.tv_name.text = "%s %s".format(item.firstName, item.lastName)
        }
    }

}