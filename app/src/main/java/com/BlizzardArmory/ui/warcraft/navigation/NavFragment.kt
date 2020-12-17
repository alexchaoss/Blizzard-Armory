package com.BlizzardArmory.ui.warcraft.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.BlizzardArmory.databinding.WowNavbarFragmentBinding
import com.BlizzardArmory.ui.navigation.GamesActivity
import com.BlizzardArmory.ui.warcraft.account.AccountFragment
import com.BlizzardArmory.util.events.NetworkEvent
import com.discord.panels.PanelsChildGestureRegionObserver
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


private const val CHARACTER = "character"
private const val REALM = "realm"
private const val MEDIA = "media"
private const val REGION = "region"


class WoWNavFragment : Fragment() {

    private var character: String? = null
    private var realm: String? = null
    private var media: String? = null
    private var region: String? = null

    private var _binding: WowNavbarFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            character = it.getString(CHARACTER)
            realm = it.getString(REALM)
            media = it.getString(MEDIA)
            region = it.getString(REGION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = WowNavbarFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = Bundle()
        bundle.putString("character", character)
        bundle.putString("realm", realm)
        bundle.putString("media", media)
        bundle.putString("region", region)

        binding.wowPager.offscreenPageLimit = 5
        val adapter = NavAdapter(childFragmentManager, binding.navBar.tabCount, bundle)
        binding.wowPager.adapter = adapter
        binding.wowPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.navBar))

        binding.navBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.wowPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PanelsChildGestureRegionObserver.Provider.get().remove(binding.wowPager.id)
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun networkEventReceived(networkEvent: NetworkEvent) {
        if (networkEvent.data) {
            AccountFragment.addOnBackPressCallback(activity as GamesActivity)
            GamesActivity.hideFavoriteButton()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(character: String, realm: String, media: String, region: String) =
                WoWNavFragment().apply {
                    arguments = Bundle().apply {
                        putString(CHARACTER, character)
                        putString(REALM, realm)
                        putString(MEDIA, media)
                        putString(REGION, region)
                    }
                }
    }
}
