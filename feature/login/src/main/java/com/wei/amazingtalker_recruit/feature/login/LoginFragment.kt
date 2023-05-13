package com.wei.amazingtalker_recruit.feature.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.extensions.observeEvent
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.navigation.navigate
import com.wei.amazingtalker_recruit.feature.login.databinding.FragmentLoginBinding
import com.wei.amazingtalker_recruit.feature.login.viewmodels.LoginViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun setupViews() {
        with(binding) {
            addOnClickListener()
        }
    }

    override fun setupObservers() {
        with(viewLifecycleOwner.lifecycleScope) {
            launchWhenStarted {
                observeEvent(viewModel.events) { event ->
                    handleEvent(event)
                }
            }
        }
    }

    override fun init() {
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is NavigateEvent.ByDeepLink -> {
                findNavController().popBackStack(R.id.loginFragment, true)
                findNavController().navigate(event.deepLink)
            }
        }
    }

    private fun FragmentLoginBinding.addOnClickListener() {
        btnNavLogin.setOnClickListener {
            viewModel.login()
        }
    }
}