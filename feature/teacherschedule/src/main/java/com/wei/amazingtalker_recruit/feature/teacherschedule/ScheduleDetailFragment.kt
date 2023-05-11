package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleDetailBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleDetailFragment : BaseFragment<FragmentScheduleDetailBinding>() {

    private val viewModel: ScheduleDetailViewModel by viewModels()
    private val args: ScheduleDetailFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleDetailBinding
        get() = FragmentScheduleDetailBinding::inflate

    override fun setupViews() {
        with(binding) {
            tvName.text = "This is Test Data: $TEST_DATA_TEACHER_NAME"
            tvStart.text = args.intervalScheduleTimeSlot.start.toString()
            tvEnd.text = args.intervalScheduleTimeSlot.end.toString()
            tvState.text = args.intervalScheduleTimeSlot.state.name
            tvDuringDayType.text = args.intervalScheduleTimeSlot.duringDayType.name
            addOnClickListener()
        }
    }

    override fun setupObservers() {
        with(viewLifecycleOwner.lifecycleScope) {
            // launchWhenStarted { observeSomeOneData() }
        }
    }

    private fun FragmentScheduleDetailBinding.addOnClickListener() {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}