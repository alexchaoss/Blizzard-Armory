package com.BlizzardArmory.ui.warcraft.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.BlizzardArmory.ui.warcraft.achievements.CategoriesFragment
import com.BlizzardArmory.ui.warcraft.character.WoWCharacterFragment
import com.BlizzardArmory.ui.warcraft.covenant.CovenantFragment
import com.BlizzardArmory.ui.warcraft.progress.ProgressFragment
import com.BlizzardArmory.ui.warcraft.pvp.PvPFragment
import com.BlizzardArmory.ui.warcraft.reputations.ReputationsFragment

class NavAdapter(fm: FragmentManager, private var totalTabs: Int, internal var bundle: Bundle) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                val wowCharacterFragment = WoWCharacterFragment()
                wowCharacterFragment.arguments = bundle
                return wowCharacterFragment
            }
            1 -> {
                val covenantFragment = CovenantFragment()
                covenantFragment.arguments = bundle
                return covenantFragment
            }
            2 -> {
                val reputationsFragment = ReputationsFragment()
                reputationsFragment.arguments = bundle
                return reputationsFragment
            }
            3 -> {
                val progressFragment = ProgressFragment()
                progressFragment.arguments = bundle
                return progressFragment
            }
            4 -> {
                val pvpFragment = PvPFragment()
                pvpFragment.arguments = bundle
                return pvpFragment
            }
            5 -> {
                val categoriesFragment = CategoriesFragment()
                categoriesFragment.arguments = bundle
                return categoriesFragment
            }
            else -> return WoWCharacterFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}