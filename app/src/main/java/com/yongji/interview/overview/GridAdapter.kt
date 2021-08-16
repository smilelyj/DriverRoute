package com.yongji.interview.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yongji.interview.databinding.GridViewItemBinding
import com.yongji.interview.network.RouteData

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class GridAdapter( val onClickListener: OnClickListener ) :
    ListAdapter<RouteData, GridAdapter.BusinessViewHolder>(DiffCallback) {

    class BusinessViewHolder(private var binding: GridViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(routeData: RouteData) {
            binding.routeData = routeData
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RouteData>() {
        override fun areItemsTheSame(oldItem: RouteData, newItem: RouteData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RouteData, newItem: RouteData): Boolean {
            return oldItem.driver == newItem.driver
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BusinessViewHolder {
        return BusinessViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val routeData = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(routeData)
            println(routeData.toString())
        }
        holder.bind(routeData)
    }

    class OnClickListener(val clickListener: (routeData: RouteData) -> Unit) {
        fun onClick(routeData: RouteData) = clickListener(routeData)
    }
}