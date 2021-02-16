package com.BlizzardArmory.ui.warcraft.mythicplusleaderboards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.BlizzardArmory.databinding.MythicPlusLeaderboardsFragmentBinding
import com.BlizzardArmory.network.oauth.BattlenetConstants
import org.greenrobot.eventbus.EventBus

class MPlusLeaderboards : Fragment() {

    private var _binding: MythicPlusLeaderboardsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MPlusLeaderboardsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MythicPlusLeaderboardsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        viewModel.getBnetParams().value =
            activity?.intent?.extras?.getParcelable(BattlenetConstants.BUNDLE_BNPARAMS)
    }


    private fun setObservers() {

    }
}