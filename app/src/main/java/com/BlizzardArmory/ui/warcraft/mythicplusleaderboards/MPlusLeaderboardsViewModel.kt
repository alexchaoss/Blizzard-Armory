package com.BlizzardArmory.ui.warcraft.mythicplusleaderboards

import com.BlizzardArmory.ui.BaseViewModel
import com.BlizzardArmory.util.events.LocaleSelectedEvent
import org.greenrobot.eventbus.Subscribe

class MPlusLeaderboardsViewModel : BaseViewModel() {

    @Subscribe
    override fun localeSelectedReceived(LocaleSelectedEvent: LocaleSelectedEvent) {
        super.localeSelectedReceived(LocaleSelectedEvent)
    }
}