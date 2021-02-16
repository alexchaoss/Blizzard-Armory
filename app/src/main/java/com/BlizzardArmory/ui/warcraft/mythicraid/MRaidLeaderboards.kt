package com.BlizzardArmory.ui.warcraft.mythicraid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.BlizzardArmory.databinding.MythicRaidLeaderboardsFragmentBinding
import com.BlizzardArmory.network.oauth.BattlenetConstants
import org.greenrobot.eventbus.EventBus

class MRaidLeaderboards : Fragment() {

    private val raidList = arrayListOf<String>()
    private val factionList = arrayListOf("Both", "Horde", "Alliance")

    private var _binding: MythicRaidLeaderboardsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MRaidLeaderboardsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addRaidsToList()
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
        _binding = MythicRaidLeaderboardsFragmentBinding.inflate(layoutInflater)
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

    private fun addRaidsToList() {
        raidList.add("castle_nathria")
        raidList.add("uldir")
        raidList.add("battle_of_dazaralor")
        raidList.add("crucible_of_storms")
        raidList.add("the_eternal_palace")
        raidList.add("nyalotha_the_waking_city")
    }
}