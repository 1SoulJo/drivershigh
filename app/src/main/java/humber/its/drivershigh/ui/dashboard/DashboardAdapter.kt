package humber.its.drivershigh.ui.dashboard

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import humber.its.drivershigh.R
import humber.its.drivershigh.data.localdata.HistoryAndRoute

class DashboardAdapter: RecyclerView.Adapter<DashboardAdapter.HistoryViewHolder>() {
    private var histories: List<HistoryAndRoute> = listOf()

    class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.history_route_name)
        val date: TextView = itemView.findViewById(R.id.history_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_list_item, parent, false)

        return HistoryViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val h = histories[position]

        holder.name.text = h.route.name
        holder.date.text = h.history.date
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    fun setHistories(data: List<HistoryAndRoute>) {
        histories = data
        notifyDataSetChanged()
    }
}