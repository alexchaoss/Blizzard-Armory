package com.BlizzardArmory.ui.navigation

import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.BlizzardArmory.R
import com.BlizzardArmory.databinding.GamesActivityBarBinding
import com.BlizzardArmory.databinding.GamesActivityBinding
import com.BlizzardArmory.model.MenuItem
import com.BlizzardArmory.model.UserInformation
import com.BlizzardArmory.model.news.UserNews
import com.BlizzardArmory.network.ErrorMessages
import com.BlizzardArmory.network.oauth.BattlenetConstants
import com.BlizzardArmory.network.oauth.BattlenetOAuth2Helper
import com.BlizzardArmory.ui.diablo.account.D3Fragment
import com.BlizzardArmory.ui.diablo.favorites.D3FavoriteFragment
import com.BlizzardArmory.ui.diablo.leaderboard.D3LeaderboardFragment
import com.BlizzardArmory.ui.main.MainActivity
import com.BlizzardArmory.ui.news.NewsListFragment
import com.BlizzardArmory.ui.overwatch.OWPlatformChoiceDialog
import com.BlizzardArmory.ui.overwatch.favorites.OWFavoritesFragment
import com.BlizzardArmory.ui.settings.SettingsFragment
import com.BlizzardArmory.ui.starcraft.leaderboard.SC2LeaderboardFragment
import com.BlizzardArmory.ui.starcraft.profile.SC2Fragment
import com.BlizzardArmory.ui.warcraft.account.AccountFragment
import com.BlizzardArmory.ui.warcraft.favorites.WoWFavoritesFragment
import com.BlizzardArmory.ui.warcraft.navigation.WoWNavFragment
import com.BlizzardArmory.util.DialogPrompt
import com.BlizzardArmory.util.events.FilterNewsEvent
import com.BlizzardArmory.util.events.LocaleSelectedEvent
import com.BlizzardArmory.util.events.MenuItemClickEvent
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.discord.panels.OverlappingPanelsLayout
import com.discord.panels.PanelState
import com.discord.panels.PanelsChildGestureRegionObserver
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class GamesActivity : LocalizationActivity(), PanelsChildGestureRegionObserver.GestureRegionsListener {

    private var prefs: SharedPreferences? = null
    private val gson = GsonBuilder().create()

    private var characterClicked: String = ""
    private var characterRealm: String = ""
    private var selectedRegion: String = ""

    private lateinit var errorMessage: ErrorMessages

    private lateinit var barBinding: GamesActivityBarBinding
    private val viewModel: GamesViewModel by viewModels()
    private var viewStateDisposable: Disposable? = null

    private val menuList = arrayListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException(thread, throwable)
        }
        barBinding = GamesActivityBarBinding.inflate(layoutInflater)
        binding = GamesActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fragment.addOnLayoutChangeListener(PanelsChildGestureRegionObserver.Provider.get())

        setOberservers()

        favorite = binding.topBar.favorite

        errorMessage = ErrorMessages(this.resources)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        viewModel.getBnetParams().value = this.intent?.extras?.getParcelable(BattlenetConstants.BUNDLE_BNPARAMS)

        openFirstTimeNewsDialog()
        getUserNewsPreferences()
        setUserNews()
        if (supportFragmentManager.backStackEntryCount == 0) {
            openNewsFragment()
        }

        setNavigation()

        createMenuList()

        binding.menu.apply {
            adapter = MenuAdapter(menuList, context)
            (binding.menu.adapter as MenuAdapter).hideSubMenu(menuList.find { it.title == resources.getString(R.string.world_of_warcraft) }!!)
            (binding.menu.adapter as MenuAdapter).hideSubMenu(menuList.find { it.title == resources.getString(R.string.diablo_3) }!!)
            (binding.menu.adapter as MenuAdapter).hideSubMenu(menuList.find { it.title == resources.getString(R.string.overwatch) }!!)
            (binding.menu.adapter as MenuAdapter).hideSubMenu(menuList.find { it.title == resources.getString(R.string.starcraft_2) }!!)
        }
    }

    private fun createMenuList() {
        menuList.add(MenuItem(true, "", resources.getString(R.string.home), R.drawable.ic_baseline_home_24, false))
        menuList.add(MenuItem(true, "", resources.getString(R.string.world_of_warcraft), R.drawable.wow_icon, false))
        menuList.add(MenuItem(false, resources.getString(R.string.world_of_warcraft), resources.getString(R.string.account), R.drawable.ic_baseline_account_circle_24, false))
        menuList.add(MenuItem(false, resources.getString(R.string.world_of_warcraft), resources.getString(R.string.search_character), R.drawable.rep_search, false))
        menuList.add(MenuItem(false, resources.getString(R.string.world_of_warcraft), resources.getString(R.string.favorites), R.drawable.ic_star_black_24dp, false))
        menuList.add(MenuItem(true, "", resources.getString(R.string.diablo_3), R.drawable.diablo3_icon, false))
        menuList.add(MenuItem(false, resources.getString(R.string.diablo_3), resources.getString(R.string.account), R.drawable.ic_baseline_account_circle_24, false))
        menuList.add(MenuItem(false, resources.getString(R.string.diablo_3), resources.getString(R.string.leaderboards), R.drawable.ic_baseline_leaderboard_24, false))
        menuList.add(MenuItem(false, resources.getString(R.string.diablo_3), resources.getString(R.string.favorites), R.drawable.ic_star_black_24dp, false))
        menuList.add(MenuItem(true, "", resources.getString(R.string.starcraft_2), R.drawable.sc2_icon, false))
        menuList.add(MenuItem(false, resources.getString(R.string.starcraft_2), resources.getString(R.string.account), R.drawable.ic_baseline_account_circle_24, false))
        menuList.add(MenuItem(false, resources.getString(R.string.starcraft_2), resources.getString(R.string.leaderboards), R.drawable.ic_baseline_leaderboard_24, false))
        menuList.add(MenuItem(true, "", resources.getString(R.string.overwatch), R.drawable.overwatch_icon, false))
        menuList.add(MenuItem(false, resources.getString(R.string.overwatch), resources.getString(R.string.account), R.drawable.ic_baseline_account_circle_24, false))
        menuList.add(MenuItem(false, resources.getString(R.string.overwatch), resources.getString(R.string.favorites), R.drawable.ic_star_black_24dp, false))
        menuList.add(MenuItem(true, "", resources.getString(R.string.settingsTitle), R.drawable.settings, false))
    }

    private fun setNavigation() {
        setSupportActionBar(binding.topBar.toolbarMain)
        binding.topBar.toolbarMain.setNavigationOnClickListener {
            if (binding.overlappingPanel.getSelectedPanel() == OverlappingPanelsLayout.Panel.START) {
                binding.overlappingPanel.closePanels()
            } else {
                binding.overlappingPanel.openStartPanel()
            }
        }
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""
    }

    private fun openNewsFragment() {
        val newsListFragment = NewsListFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment, newsListFragment, "news_fragment").commit()
        supportFragmentManager.executePendingTransactions()
    }

    private fun getUserNewsPreferences() {
        val userNewsPrefs = prefs?.getString("user_news", "DEFAULT")
        if (userNewsPrefs == "DEFAULT") {
            userNews = UserNews()
            prefs?.edit()?.putString("user_news", gson.toJson(userNews))?.apply()
        } else {
            userNews = gson.fromJson(userNewsPrefs, UserNews::class.java)
        }
    }

    private fun openFirstTimeNewsDialog() {
        if (!prefs?.contains("news_pulled")!!) {
            val dialog = DialogPrompt(this)
            dialog.addTitle("New Feature!", 20F)
                    .addMessage("Pull from the right side to select which news you want to see here!", 18F)
                    .addButton("Close", 16F, { dialog.dismiss() }).show()
            prefs?.edit()?.putString("news_pulled", "done")?.apply()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()

        binding.overlappingPanel.registerStartPanelStateListeners(object : OverlappingPanelsLayout.PanelStateListener {
            override fun onPanelStateChange(panelState: PanelState) {
                viewModel.onStartPanelStateChange(panelState)
            }
        })

        binding.overlappingPanel.registerEndPanelStateListeners(object : OverlappingPanelsLayout.PanelStateListener {
            override fun onPanelStateChange(panelState: PanelState) {
                viewModel.onEndPanelStateChange(panelState)
            }
        })

        viewStateDisposable = viewModel.observeViewState()
                .subscribe { viewState ->
                    handleViewState(viewState)
                }

        PanelsChildGestureRegionObserver.Provider.get().addGestureRegionsUpdateListener(this)
    }

    override fun onPause() {
        super.onPause()
        viewStateDisposable?.dispose()
        PanelsChildGestureRegionObserver.Provider.get().addGestureRegionsUpdateListener(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("REMOVED GESTURE", "REMOVED")
        PanelsChildGestureRegionObserver.Provider.get().remove(binding.fragment.id)
    }

    private fun setOberservers() {
        viewModel.getBnetParams().observe(this, {
            viewModel.battlenetOAuth2Helper = BattlenetOAuth2Helper(it)
            viewModel.getRealms()
            viewModel.downloadUserInfo()
        })

        viewModel.getUserInformation().observe(this, {
            userInformation = it
            binding.topBar.barTitle.text = userInformation?.battleTag
        })

        viewModel.getErrorCode().observe(this, {
            callErrorAlertDialog(it)
        })

        viewModel.getMedia().observe(this, {
            callWoWCharacterFragment(characterClicked, characterRealm, selectedRegion)
        })
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun localeSelectedReceived(localeSelectedEvent: LocaleSelectedEvent) {
        if (!localeSelectedEvent.locale.contains((getCurrentLanguage().language + "_" + getCurrentLanguage().country))) {
            viewModel.getRealms()
            when (localeSelectedEvent.locale) {
                "en_US" -> setLanguage("en")
                "es_ES" -> setLanguage("es")
                "fr_FR" -> setLanguage("fr")
                "ru_RU" -> setLanguage("ru")
                "de_DE" -> setLanguage("de")
                "pt_BR" -> setLanguage("pt")
                "it_IT" -> setLanguage("it")
                "ko_KR" -> setLanguage("ko")
                "zh_CN" -> setLanguage("zh", "CN")
                "zh_TW" -> setLanguage("zh", "TW")
            }
        }
    }

    private fun handleUncaughtException(thread: Thread?, e: Throwable) {
        Log.e("Crash Prevented", e.message!!, e)
        FirebaseCrashlytics.getInstance().log(e.message!!)
    }

    private fun setUserNews() {
        binding.rightPanelGames.blizzardSwitch.isChecked = userNews?.blizzNews!!
        binding.rightPanelGames.wowSwitch.isChecked = userNews?.wowNews!!
        binding.rightPanelGames.d3Switch.isChecked = userNews?.d3News!!
        binding.rightPanelGames.sc2Switch.isChecked = userNews?.sc2News!!
        binding.rightPanelGames.owSwitch.isChecked = userNews?.owNews!!
        binding.rightPanelGames.hsSwitch.isChecked = userNews?.hsNews!!
        binding.rightPanelGames.hotsSwitch.isChecked = userNews?.hotsNews!!

        binding.rightPanelGames.blizzardSwitch.setOnClickListener {
            userNews?.blizzNews = !userNews?.blizzNews!!
            updateUserNews()
        }
        binding.rightPanelGames.wowSwitch.setOnClickListener {
            userNews?.wowNews = !userNews?.wowNews!!
            updateUserNews()
        }
        binding.rightPanelGames.d3Switch.setOnClickListener {
            userNews?.d3News = !userNews?.d3News!!
            updateUserNews()
        }
        binding.rightPanelGames.sc2Switch.setOnClickListener {
            userNews?.sc2News = !userNews?.sc2News!!
            updateUserNews()
        }
        binding.rightPanelGames.owSwitch.setOnClickListener {
            userNews?.owNews = !userNews?.owNews!!
            updateUserNews()
        }
        binding.rightPanelGames.hsSwitch.setOnClickListener {
            userNews?.hsNews = !userNews?.hsNews!!
            updateUserNews()
        }
        binding.rightPanelGames.hotsSwitch.setOnClickListener {
            userNews?.hotsNews = !userNews?.hotsNews!!
            updateUserNews()
        }
    }

    private fun updateUserNews() {
        prefs?.edit()?.putString("user_news", gson.toJson(userNews))?.apply()
        EventBus.getDefault().post(FilterNewsEvent(true))
    }

    private fun getErrorMessage(responseCode: Int): String {
        return when (responseCode) {
            in 201..499 -> {
                errorMessage.UNEXPECTED
            }
            500 -> {
                errorMessage.BLIZZ_SERVERS_DOWN
            }
            else -> {
                errorMessage.TURN_ON_CONNECTION_MESSAGE
            }
        }
    }

    private fun getErrorTitle(responseCode: Int): String {
        return when (responseCode) {
            in 201..499 -> {
                errorMessage.UNAVAILABLE
            }
            500 -> {
                errorMessage.SERVERS_ERROR
            }
            else -> {
                errorMessage.NO_INTERNET
            }
        }
    }

    private fun callErrorAlertDialog(responseCode: Int) {
        val dialog = DialogPrompt(this)

        if (responseCode == 404 || responseCode == 403 || responseCode == 500) {
            dialog.setOnCancelListener { finish() }
        } else {
            dialog.setOnCancelListener { viewModel.downloadUserInfo() }
        }

        dialog.addTitle(getErrorTitle(responseCode), 20f, "title")
                .addMessage(getErrorMessage(responseCode), 18f, "message")
                .addSideBySideButtons(errorMessage.RETRY, 18f, errorMessage.BACK, 18f,
                        { dialog.dismiss() },
                        {
                            dialog.dismiss()
                            onBackPressed()
                        },
                        "retry", "back").show()
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun menuItemClickedReceived(menuItem: MenuItemClickEvent) {
        val fragment: Fragment
        val searchDialog = DialogPrompt(this)
        when (menuItem.menuItem.title) {
            this.resources.getString(R.string.home) -> {
                hideFavoriteButton()
                resetBackStack()
                favorite!!.visibility = View.GONE
                binding.overlappingPanel.closePanels()
            }
            resources.getString(R.string.diablo_3) -> {
                if (menuItem.menuItem.toggle) {
                    menuList.find { it.title == resources.getString(R.string.diablo_3) }?.icon = R.drawable.diablo3_icon
                    (binding.menu.adapter as MenuAdapter).hideSubMenu(menuItem.menuItem)
                } else {
                    menuList.find { it.title == resources.getString(R.string.diablo_3) }?.icon = R.drawable.diablo3_icon_glow
                    (binding.menu.adapter as MenuAdapter).showSubMenu(menuItem.menuItem)
                }
                menuList.find { it.title == resources.getString(R.string.diablo_3) }?.toggle = !menuItem.menuItem.toggle
            }
            resources.getString(R.string.overwatch) -> {
                if (menuItem.menuItem.toggle) {
                    menuList.find { it.title == resources.getString(R.string.overwatch) }?.icon = R.drawable.overwatch_icon
                    (binding.menu.adapter as MenuAdapter).hideSubMenu(menuItem.menuItem)
                } else {
                    menuList.find { it.title == resources.getString(R.string.overwatch) }?.icon = R.drawable.overwatch_icon_glow
                    (binding.menu.adapter as MenuAdapter).showSubMenu(menuItem.menuItem)
                }
                menuList.find { it.title == resources.getString(R.string.overwatch) }?.toggle = !menuItem.menuItem.toggle
            }
            resources.getString(R.string.world_of_warcraft) -> {
                if (menuItem.menuItem.toggle) {
                    menuList.find { it.title == resources.getString(R.string.world_of_warcraft) }?.icon = R.drawable.wow_icon
                    (binding.menu.adapter as MenuAdapter).hideSubMenu(menuItem.menuItem)
                } else {
                    menuList.find { it.title == resources.getString(R.string.world_of_warcraft) }?.icon = R.drawable.wow_icon_glow
                    (binding.menu.adapter as MenuAdapter).showSubMenu(menuItem.menuItem)
                }
                menuList.find { it.title == resources.getString(R.string.world_of_warcraft) }?.toggle = !menuItem.menuItem.toggle
            }
            resources.getString(R.string.starcraft_2) -> {
                if (menuItem.menuItem.toggle) {
                    menuList.find { it.title == resources.getString(R.string.starcraft_2) }?.icon = R.drawable.sc2_icon
                    (binding.menu.adapter as MenuAdapter).hideSubMenu(menuItem.menuItem)
                } else {
                    menuList.find { it.title == resources.getString(R.string.starcraft_2) }?.icon = R.drawable.sc2_icon_glow
                    (binding.menu.adapter as MenuAdapter).showSubMenu(menuItem.menuItem)
                }
                menuList.find { it.title == resources.getString(R.string.starcraft_2) }?.toggle = !menuItem.menuItem.toggle
            }
            resources.getString(R.string.account) -> {
                when (menuItem.menuItem.parent) {
                    resources.getString(R.string.world_of_warcraft) -> {
                        favorite!!.visibility = View.GONE
                        fragment = AccountFragment()
                        resetBackStack()
                        supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "wowfragment").addToBackStack("wow_account").commit()
                        supportFragmentManager.executePendingTransactions()
                    }
                    resources.getString(R.string.diablo_3) -> {
                        searchDialog.addTitle("Enter a BattleTag", 18F, "battletag")
                                .addEditText("btag_field")
                                .addSpinner(resources.getStringArray(R.array.regions), "region_spinner")
                                .addSideBySideButtons("Search", 16F, "My Profile", 16F, { searchD3Profile(searchDialog) },
                                        {
                                            callD3Activity(userInformation?.battleTag!!, MainActivity.selectedRegion)
                                            searchDialog.dismiss()
                                        }, "search_button", "myprofile_button").show()
                    }
                    resources.getString(R.string.overwatch) -> {
                        OWPlatformChoiceDialog.overwatchPrompt(this, supportFragmentManager)
                    }
                    resources.getString(R.string.starcraft_2) -> {
                        favorite!!.visibility = View.GONE
                        fragment = SC2Fragment()
                        resetBackStack()
                        supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "sc2fragment").addToBackStack("sc2").commit()
                        binding.overlappingPanel.closePanels()
                    }
                }
                binding.overlappingPanel.closePanels()
            }
            resources.getString(R.string.search_character) -> {
                searchDialog.addTitle("Character Name", 18F, "character_label")
                        .addEditText("character_field")
                        .addMessage("Realm", 18F, "realm_label")
                        .addAutoCompleteEditText("realm_field", viewModel.getWowRealms().value!!.values.flatMap { it.realms }.map { it.name }.distinct())
                        .addSpinner(resources.getStringArray(R.array.regions), "region_spinner")
                        .addButton("GO", 16F, { openSearchedWoWCharacter(searchDialog) }, "search_button").show()
                binding.overlappingPanel.closePanels()
            }
            resources.getString(R.string.favorites) -> {
                resetBackStack()
                when (menuItem.menuItem.parent) {
                    resources.getString(R.string.world_of_warcraft) -> {
                        favorite!!.visibility = View.GONE
                        fragment = WoWFavoritesFragment()
                        supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "wowfavorites").addToBackStack("wow_fav").commit()
                        supportFragmentManager.executePendingTransactions()
                    }
                    resources.getString(R.string.diablo_3) -> {
                        favorite!!.visibility = View.GONE
                        fragment = D3FavoriteFragment()
                        supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "d3favorites").addToBackStack("d3_fav").commit()
                        supportFragmentManager.executePendingTransactions()
                    }
                    resources.getString(R.string.overwatch) -> {
                        favorite!!.visibility = View.GONE
                        fragment = OWFavoritesFragment()
                        supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "owfavorites").addToBackStack("ow_fav").commit()
                        supportFragmentManager.executePendingTransactions()
                    }
                }
                binding.overlappingPanel.closePanels()
            }
            resources.getString(R.string.leaderboards) -> {
                resetBackStack()
                when (menuItem.menuItem.parent) {
                    resources.getString(R.string.diablo_3) -> {
                        favorite!!.visibility = View.GONE
                        fragment = D3LeaderboardFragment()
                        resetBackStack()
                        supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "d3leaderboard").addToBackStack("d3_leaderboard").commit()
                        supportFragmentManager.executePendingTransactions()
                    }
                    resources.getString(R.string.starcraft_2) -> {
                        favorite!!.visibility = View.GONE
                        fragment = SC2LeaderboardFragment()
                        resetBackStack()
                        supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "sc2leaderboard").addToBackStack("sc2_leaderboard").commit()
                        supportFragmentManager.executePendingTransactions()
                    }
                }
                binding.overlappingPanel.closePanels()
            }
            resources.getString(R.string.settingsTitle) -> {
                favorite!!.visibility = View.GONE
                fragment = SettingsFragment()
                supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "settingsfragment").addToBackStack("settings").commit()
                supportFragmentManager.executePendingTransactions()
                binding.overlappingPanel.closePanels()
            }
        }
    }

    private fun resetBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        if (supportFragmentManager.findFragmentById(R.id.fragment) != null) {
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentById(R.id.fragment)!!).commit()
        }
        openNewsFragment()
        supportFragmentManager.executePendingTransactions()
    }

    private fun searchD3Profile(dialog: DialogPrompt) {
        val btagRegex = Regex(".*#[0-9]*")
        when {
            (dialog.tagMap["btag_field"] as EditText).text.toString().matches(btagRegex) -> {
                Toast.makeText(dialog.context, "Please enter a BattleTag", Toast.LENGTH_SHORT).show()
            }
            (dialog.tagMap["region_spinner"] as Spinner).selectedItem.toString().equals("Select Region", ignoreCase = true) -> {
                Toast.makeText(dialog.context, "Please enter the region", Toast.LENGTH_SHORT).show()
            }
            else -> {
                dialog.dismiss()
                callD3Activity((dialog.tagMap["btag_field"] as EditText).text.toString().toLowerCase(Locale.ROOT), (dialog.tagMap["region_spinner"] as Spinner).selectedItem.toString())
            }
        }
    }

    private fun openSearchedWoWCharacter(dialog: DialogPrompt) {
        when {
            (dialog.tagMap["character_field"] as EditText).text.toString() == "" -> {
                Toast.makeText(this, "Please enter the character name", Toast.LENGTH_SHORT).show()
            }
            (dialog.tagMap["realm_field"] as AutoCompleteTextView).text.toString() == "" -> {
                Toast.makeText(this, "Please enter the realm", Toast.LENGTH_SHORT).show()
            }
            (dialog.tagMap["region_spinner"] as Spinner).selectedItem.toString().equals("Select Region", ignoreCase = true) -> {
                Toast.makeText(this, "Please enter the region", Toast.LENGTH_SHORT).show()
            }
            else -> {
                characterClicked = (dialog.tagMap["character_field"] as EditText).text.toString().toLowerCase(Locale.ROOT)
                characterRealm = viewModel.getWowRealms().value!![(dialog.tagMap["region_spinner"] as Spinner).selectedItem.toString()]!!.realms.find { it.name == (dialog.tagMap["realm_field"] as AutoCompleteTextView).text.toString() }?.slug!!
                selectedRegion = (dialog.tagMap["region_spinner"] as Spinner).selectedItem.toString()
                viewModel.downloadMedia(characterClicked, characterRealm, selectedRegion)
                dialog.dismiss()
            }
        }
    }

    private fun callD3Activity(battletag: String, region: String) {
        val fragment: Fragment = D3Fragment()
        val bundle = Bundle()
        bundle.putString("battletag", battletag)
        bundle.putString("region", region)
        bundle.putParcelable(BattlenetConstants.BUNDLE_BNPARAMS, viewModel.getBnetParams().value)
        fragment.arguments = bundle
        resetBackStack()
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.fragment, fragment, "d3fragment")
                .addToBackStack("d3_account").commit()
        supportFragmentManager.executePendingTransactions()
    }

    private fun callWoWCharacterFragment(characterClicked: String, characterRealm: String, selectedRegion: String) {
        if (supportFragmentManager.primaryNavigationFragment != null && supportFragmentManager.primaryNavigationFragment!!.isVisible) {
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.primaryNavigationFragment!!).commit()
        }
        val mediaString = Gson().toJson(viewModel.getMedia().value)
        val woWNavFragment = WoWNavFragment.newInstance(characterClicked, characterRealm, mediaString, selectedRegion)
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
                .add(R.id.fragment, woWNavFragment, "NAV_FRAGMENT")
                .addToBackStack("wow_nav").commit()
        supportFragmentManager.executePendingTransactions()
    }


    companion object {
        var userInformation: UserInformation? = null
        var userNews: UserNews? = null
        var favorite: ImageView? = null
        lateinit var binding: GamesActivityBinding

        fun hideFavoriteButton() {
            favorite!!.visibility = View.GONE
            favorite!!.setImageResource(R.drawable.ic_star_border_black_24dp)
            favorite!!.tag = R.drawable.ic_star_border_black_24dp
        }
    }

    override fun onGestureRegionsUpdate(gestureRegions: List<Rect>) {
        Log.i("rect size", gestureRegions.size.toString())

        when (supportFragmentManager.fragments.last().tag) {
            "NAV_FRAGMENT",
            "overwatchfragment" -> {
                Log.i("GESTURE UPDATE", "UPDATED")
                for (rect in gestureRegions) {
                    rect.set((resources.displayMetrics.widthPixels * 0.25).toInt(), rect.top,
                            (resources.displayMetrics.widthPixels * 0.75).toInt(), rect.bottom)
                }
            }
            else -> {
                for (rect in gestureRegions) {
                    rect.set(resources.displayMetrics.widthPixels, rect.top, 0, rect.bottom)
                }
            }
        }

        binding.overlappingPanel.setChildGestureRegions(gestureRegions)
    }


    private fun handleViewState(viewState: GamesViewModel.ViewState) {
        binding.overlappingPanel.handleStartPanelState(viewState.startPanelState)
        binding.overlappingPanel.handleEndPanelState(viewState.endPanelState)
    }
}