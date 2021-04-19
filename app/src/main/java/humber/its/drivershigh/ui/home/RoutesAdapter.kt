package humber.its.drivershigh.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import humber.its.drivershigh.R
import humber.its.drivershigh.data.localdata.Route

class RoutesAdapter(private val clickListener: (data: Route) -> Unit)
    : RecyclerView.Adapter<RoutesAdapter.RouteViewHolder>() {
    private var routes: List<Route> = listOf()

    class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var routeName: TextView = itemView.findViewById(R.id.route_name)
        var duration: TextView = itemView.findViewById(R.id.route_duration)
        var length: TextView = itemView.findViewById(R.id.route_length)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.route_list_item, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val data = routes[position]

        holder.itemView.setOnClickListener {
            clickListener(data)
        }

        holder.routeName.text = data.name
        holder.duration.text = "${data.duration} min"
        holder.length.text = "${data.length} km"
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    fun setData(data: List<Route>) {
        routes = data
        notifyDataSetChanged()
    }
}