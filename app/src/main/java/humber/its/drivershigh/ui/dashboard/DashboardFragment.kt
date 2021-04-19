package humber.its.drivershigh.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import humber.its.drivershigh.data.localdata.HistoryAndRoute
import humber.its.drivershigh.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        initList()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initList() {
        _binding?.historyList?.layoutManager = LinearLayoutManager(requireContext())
        _binding?.historyList?.adapter = DashboardAdapter()

        dashboardViewModel.histories.observe(requireActivity(), {
            if (it != null && _binding?.historyList?.adapter != null) {
                (_binding?.historyList?.adapter as DashboardAdapter).setHistories(it)
            }
        })
    }
}