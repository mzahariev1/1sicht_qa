package edu.kit.pse.a1sicht.ui.utils

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import edu.kit.pse.a1sicht.R
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Student
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * The adapter for the recycle view that holds the users
 *
 * @author Hristo Klechorov
 */
class UsersAdapter(var users: List<Any>): RecyclerView.Adapter<BaseViewHolder<*>>() {

    private lateinit var mListener: OnItemClickListener

    companion object {
        private const val TYPE_STUDENT = 0
        private const val TYPE_EMPLOYEE = 1
    }

    /**
     * ItemClickListener interface
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    /**
     * Method for setting an itemClick listener
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_STUDENT -> {
                val inflatedView = parent.inflate(R.layout.item_user, false)
                StudentsHolder(inflatedView, mListener)
            }
            TYPE_EMPLOYEE -> {
                val inflatedView = parent.inflate(R.layout.item_user, false)
                EmployeesHolder(inflatedView, mListener)
            }
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = users[position]
        when (holder) {
            is StudentsHolder -> holder.bind(element as Student)
            is EmployeesHolder -> holder.bind(element as Employee)
            else -> throw IllegalArgumentException("View binding failed")
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = users[position]
        return when (comparable) {
            is Student -> TYPE_STUDENT
            is Employee -> TYPE_EMPLOYEE
            else -> throw IllegalArgumentException("Invalid data source at %d".format(position))
        }
    }

    /**
     * The ViewHolder for the Student data object
     * @author Hristo Klechorov
     */
    class StudentsHolder(v: View, listener: OnItemClickListener) : BaseViewHolder<Student>(v) {

        private var view: View = v

        init{
            val deleteButton = view.findViewById<ImageButton>(R.id.btn_delete)

            deleteButton.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        override fun bind(item: Student) {
            view.tv_name.text = "%s %s".format(item.firstName, item.lastName)
        }

    }

    /**
     * The ViewHolder for the Employee data object
     *
     * @author Hristo Klechorov
     */
    class EmployeesHolder(v: View, listener: OnItemClickListener) : BaseViewHolder<Employee>(v) {

        private var view: View = v

        //get the delete button and set a onItemClick listener that will be implemented in the activity
        init {
            val deleteButton = view.findViewById<ImageButton>(R.id.btn_delete)

            deleteButton.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        override fun bind(item: Employee) {
            view.tv_name.text = "%s %s".format(item.firstName, item.lastName)
        }
    }

}

