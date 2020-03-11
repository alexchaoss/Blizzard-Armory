package com.BlizzardArmory.ui.ui_warcraft.progress


import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.R
import com.BlizzardArmory.URLConstants
import com.BlizzardArmory.ui.IOnBackPressed
import com.BlizzardArmory.ui.ui_warcraft.ClassEvent
import com.BlizzardArmory.ui.ui_warcraft.WoWNavFragment
import com.BlizzardArmory.warcraft.encounters.EncountersInformation
import com.BlizzardArmory.warcraft.encounters.Expansions
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dementh.lib.battlenet_oauth2.BnConstants
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Helper
import com.dementh.lib.battlenet_oauth2.connections.BnOAuth2Params
import com.google.common.base.Ascii.toLowerCase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.wow_progress_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


private const val CHARACTER = "character"
private const val REALM = "realm"
private const val MEDIA = "media"
private const val REGION = "region"


class ProgressFragment : Fragment(), IOnBackPressed {

    private var character: String? = null
    private var realm: String? = null
    private var media: String? = null
    private var region: String? = null
    private var bnOAuth2Helper: BnOAuth2Helper? = null
    private var bnOAuth2Params: BnOAuth2Params? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            character = it.getString(CHARACTER)
            realm = it.getString(REALM)
            media = it.getString(MEDIA)
            region = it.getString(REGION)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.wow_progress_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(view.context)
        bnOAuth2Params = activity?.intent?.extras?.getParcelable(BnConstants.BUNDLE_BNPARAMS)
        bnOAuth2Helper = BnOAuth2Helper(prefs, bnOAuth2Params)

        downloadEncounterInformation(view)
    }

    private fun downloadEncounterInformation(view: View) {
        val requestQueue = Volley.newRequestQueue(view.context)

        val url = URLConstants.getBaseURLforAPI(region) +
                URLConstants.WOW_ENCOUNTERS.replace("zone", toLowerCase(region))
                        .replace("realm", toLowerCase(realm!!)).replace("characterName", toLowerCase(character!!)) + bnOAuth2Helper!!.accessToken

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    val encounters = Gson().fromJson(response.toString(), EncountersInformation::class.java)
                    setRecyclerViewForEachExpansion(encounters)
                }, Response.ErrorListener {
            showOutdatedTextView()
        })
        requestQueue.add(jsonObjectRequest)
    }

    private fun setRecyclerViewForEachExpansion(encounters: EncountersInformation) {
        var expansions: List<Expansions>? = null
        if (!encounters.expansions.isNullOrEmpty()) {
            expansions = encounters.expansions.reversed();
        }
        if (expansions != null) {
            for (expansion in expansions) run {
                val paramsButton: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val button = TextView(context)
                button.setBackgroundResource(R.drawable.progress_collapse_header)
                button.setTextColor(Color.WHITE)
                button.text = "+ " + expansion.expansion.name
                button.textSize = 18F
                button.layoutParams = paramsButton
                progress_container.addView(button)

                var expand = false
                val paramsRecyclerView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val recyclerView = context?.let { RecyclerView(it) }
                recyclerView?.layoutParams = paramsRecyclerView
                progress_container.addView(recyclerView)

                recyclerView?.apply {
                    val raidLevel = getRaidLevel(expansion)
                    layoutManager = LinearLayoutManager(activity)
                    adapter = EncounterAdapter(expansion.instances, raidLevel, context, expansion.expansion)
                }
                recyclerView?.visibility = View.GONE

                button.setOnClickListener {
                    if (!expand) {
                        expand = true
                        recyclerView?.visibility = View.VISIBLE
                        button.text = "- " + expansion.expansion.name
                    } else {
                        expand = false
                        recyclerView?.visibility = View.GONE
                        button.text = "+ " + expansion.expansion.name
                    }
                }
            }
        } else {
            showOutdatedTextView()
        }
    }

    private fun showOutdatedTextView() {
        val outdatedInfo = TextView(context)
        outdatedInfo.text = "Outdated information\nPlease login in game to refresh data"
        outdatedInfo.setTextColor(Color.WHITE)
        outdatedInfo.gravity = Gravity.CENTER
        outdatedInfo.textSize = 20F
        outdatedInfo.setPadding(0, 50, 0, 0)
        progress_container.addView(outdatedInfo)
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

    private fun getRaidLevel(expansion: Expansions): String {
        when (expansion.expansion.name) {
            "Burning Crusade" -> return "Level 70"
            "Wrath of the Lich King" -> return "Level 80"
            "Cataclysm" -> return "Level 85"
            "Mists of Pandaria" -> return "Level 90"
            "Warlords of Draenor" -> return "Level 100"
            "Legion" -> return "Level 110"
            "Battle for Azeroth" -> return "Level 120"
            "Classic" -> return "Level 60"
            else -> return ""
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun classEventReceived(classEvent: ClassEvent) {
        when (classEvent.data) {
            "Death Knight" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#080812"))
                background_progress.setBackgroundResource(R.drawable.dk_bg)
            }
            "Demon Hunter" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#000900"))
                background_progress.setBackgroundResource(R.drawable.dh_bg)
            }
            "Druid" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#04100a"))
                background_progress.setBackgroundResource(R.drawable.druid_bg)
            }
            "Hunter" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#0f091b"))
                background_progress.setBackgroundResource(R.drawable.hunter_bg)
            }
            "Mage" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#110617"))
                background_progress.setBackgroundResource(R.drawable.mage_bg)
            }
            "Monk" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#040b17"))
                background_progress.setBackgroundResource(R.drawable.monk_bg)
            }
            "Paladin" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#13040a"))
                background_progress.setBackgroundResource(R.drawable.paladin_bg)
            }
            "Priest" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#15060e"))
                background_progress.setBackgroundResource(R.drawable.priest_bg)
            }
            "Rogue" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#160720"))
                background_progress.setBackgroundResource(R.drawable.rogue_bg)
            }
            "Shaman" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#050414"))
                background_progress.setBackgroundResource(R.drawable.shaman_bg)
            }
            "Warlock" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#080516"))
                background_progress.setBackgroundResource(R.drawable.warlock_bg)
            }
            "Warrior" -> {
                progress_layout.setBackgroundColor(Color.parseColor("#1a0407"))
                background_progress.setBackgroundResource(R.drawable.warrior_bg)
            }
        }
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}