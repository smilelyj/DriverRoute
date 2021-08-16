package com.yongji.interview.overview

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.yongji.interview.R
import com.yongji.interview.addRxTextWatcher
import com.yongji.interview.databinding.FragmentOverviewBinding
import com.yongji.interview.detail.DetailViewModel
import com.yongji.interview.detail.DetailViewModelFactory
import com.yongji.interview.overview.GridAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

open class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        binding.routeGrid.layoutManager = GridLayoutManager(context, 2)

        // Sets the adapter of the routeGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our route item is clicked
        binding.routeGrid.adapter = GridAdapter(GridAdapter.OnClickListener {
            viewModel.displayDetails(it)
        })

        viewModel.navigateToSelectedFactItem.observe(viewLifecycleOwner, Observer {
            //For future use
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayDetailsComplete()
            }
        })
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).window.statusBarColor = getColor(activity as AppCompatActivity,R.color.colorPrimary)

        return binding.root
    }
}