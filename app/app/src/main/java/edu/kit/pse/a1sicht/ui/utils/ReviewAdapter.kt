package edu.kit.pse.a1sicht.ui.utils


import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Review
import kotlinx.android.synthetic.main.item_review.view.*



/**
 * The adapter for review recycle view
 *
 * @author Hristo Klechorov
 */
class ReviewAdapter(var reviews: List<Review>, private val HomeActivity: Boolean): RecyclerView.Adapter<BaseViewHolder<*>>() {

    private lateinit var mListener: OnItemClickListener

    /**
     * ItemClickListener interface
     */
    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
        fun onItemClick(position: Int, v:View)
    }

    /**
     * Method for setting an itemClick listener
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    /**
     * Assigns the item layout to a view holder when one is created.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
            if (HomeActivity) {
                val inflatedView = parent.inflate(R.layout.item_review, false)
                return ReviewHolderAdmin(inflatedView, mListener)
            } else {
                val inflatedView = parent.inflate(R.layout.item_review_student_unsigned, false)
                return ReviewHolderStudent(inflatedView, mListener)
            }
    }

    override fun getItemCount(): Int {
       return reviews.size
    }

    /**
     * When an existing view holder is being reused it recieves a new
     * review name to display
     */
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder) {
            is ReviewHolderAdmin -> holder.bind(reviews[position])
            is ReviewHolderStudent -> holder.bind(reviews[position])
        }
    }

    /**
     * The review Holder for the admin screen with all reviews and the home student
     * screen with the reviews, for which he has already signed up
     * @author Hristo Klechorov
     */
    class ReviewHolderAdmin(v: View, listener: OnItemClickListener) : BaseViewHolder<Review>(v){

        private var view: View = v

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition, view)
            }
            val deleteButton = view.findViewById<ImageButton>(R.id.delete_btn)

            deleteButton.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }
        }

        override fun bind(item: Review) {
            view.reviewName.text = item.name
        }
    }

    /**
     * The review Holder for the student screen with all reviews
     * @author Hristo Klechorov
     */
    class ReviewHolderStudent(v: View, listener: OnItemClickListener) : BaseViewHolder<Review>(v){

        private var view: View = v

        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition, view)
            }
        }

        override fun bind(item: Review) {
            view.reviewName.text = item.name
        }
    }
}

