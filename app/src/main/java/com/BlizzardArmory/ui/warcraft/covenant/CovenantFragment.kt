package com.BlizzardArmory.ui.warcraft.covenant

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.BlizzardArmory.R
import com.BlizzardArmory.databinding.WowCovenantFragmentBinding
import com.BlizzardArmory.model.warcraft.covenant.character.soulbind.SoulbindInformation
import com.BlizzardArmory.model.warcraft.covenant.character.soulbind.Soulbinds
import com.BlizzardArmory.model.warcraft.covenant.covenant.custom.CovenantSpells
import com.BlizzardArmory.model.warcraft.covenant.techtalent.TechTalentWithIcon
import com.BlizzardArmory.network.oauth.BattlenetConstants
import com.BlizzardArmory.network.oauth.BattlenetOAuth2Helper
import com.BlizzardArmory.ui.warcraft.navigation.WoWNavFragment
import com.BlizzardArmory.util.MetricConversion
import com.BlizzardArmory.util.events.ClassEvent
import com.bumptech.glide.Glide
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val CHARACTER = "character"
private const val REALM = "realm"
private const val REGION = "region"

class CovenantFragment : Fragment() {

    private var characterClass = 0

    private var _binding: WowCovenantFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CovenantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.character = it.getString(CHARACTER)!!
            viewModel.realm = it.getString(REALM)!!
            viewModel.region = it.getString(REGION)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = WowCovenantFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        viewModel.getBnetParams().value = activity?.intent?.extras?.getParcelable(BattlenetConstants.BUNDLE_BNPARAMS)
    }

    private fun setObservers() {
        viewModel.getBnetParams().observe(viewLifecycleOwner, {
            viewModel.battlenetOAuth2Helper = BattlenetOAuth2Helper(it)
            viewModel.downloadCharacterSoulbinds()
        })

        viewModel.getSoulbinds().observe(viewLifecycleOwner, {
            setHeader(it)
            setAvatars(it)
            viewModel.downloadCovenantSpell(it.chosenCovenant.id)
            viewModel.downloadCovenantClassSpell(characterClass)
            it.soulbinds.forEach { soulbind -> viewModel.downloadTechTalents(soulbind.soulbind.id) }

        })

        viewModel.getcovenantClassSpells().observe(viewLifecycleOwner, {
            Glide.with(requireContext()).load(it.find { covenantSpell -> covenantSpell.covenant_id == viewModel.getSoulbinds().value?.chosenCovenant?.id }?.icon).into(binding.covenantClassSpell)
            setOnSpellTouched(binding.covenantClassSpell, it.find { covenantSpell -> covenantSpell.covenant_id == viewModel.getSoulbinds().value?.chosenCovenant?.id }!!)

        })
        viewModel.getTechTalents().observe(viewLifecycleOwner, {
            Log.i("COV UP", "updated")

            if (it.size == viewModel.getSoulbinds().value?.soulbinds?.size) {
                it.forEach { map ->
                    viewModel.downloadTechTree(map.value[0].treeId, map.key)
                }
            }
        })

        viewModel.getTechTrees().observe(viewLifecycleOwner, {
            if (it.size == viewModel.getSoulbinds().value?.soulbinds?.size) {
                setOnAvatarClickListeners(viewModel.getTechTalents().value!!)
            }
        })

        viewModel.getcovenantSpell().observe(viewLifecycleOwner, {
            Glide.with(requireContext()).load(it[0].icon).into(binding.covenantSpell)
            setOnSpellTouched(binding.covenantSpell, it[0])
        })

        viewModel.getErrorCode().observe(viewLifecycleOwner, {
        })
    }

    private fun setOnAvatarClickListeners(mutableMap: MutableMap<Int, List<TechTalentWithIcon>>) {

        mutableMap.forEach { map ->
            val soulbinds = viewModel.getSoulbinds().value?.soulbinds
            val index = soulbinds!!.indexOf(soulbinds.find { sb -> sb.soulbind.id == map.key })
            val tree = viewModel.getTechTrees().value!![soulbinds[index].soulbind.id]!!.talents
                    .sortedBy { talent -> soulbinds[index].traits.find { talent.id == it.trait.id }?.tier }
                    .groupBy { talent -> soulbinds[index].traits.find { talent.id == it.trait.id }?.tier }.map { it.value }
            when (index) {
                0 -> {
                    binding.avatar1.setOnClickListener {

                        setGreyShade(binding.avatar1, 1f)
                        setGreyShade(binding.avatar2, 0f)
                        setGreyShade(binding.avatar3, 0f)
                        binding.conduitRecycler.apply {
                            adapter = SoulbindAdapter(tree, soulbinds[index].traits, map.value, requireContext())
                        }
                    }
                }

                1 -> {
                    binding.avatar2.setOnClickListener {
                        setGreyShade(binding.avatar2, 1f)
                        setGreyShade(binding.avatar1, 0f)
                        setGreyShade(binding.avatar3, 0f)
                        binding.conduitRecycler.apply {
                            adapter = SoulbindAdapter(tree, soulbinds[index].traits, map.value, requireContext())
                        }
                    }
                }

                2 -> {
                    binding.avatar3.setOnClickListener {
                        setGreyShade(binding.avatar3, 1f)
                        setGreyShade(binding.avatar1, 0f)
                        setGreyShade(binding.avatar2, 0f)
                        binding.conduitRecycler.apply {
                            adapter = SoulbindAdapter(tree, soulbinds[index].traits, map.value, requireContext())
                        }
                    }
                }
            }
        }
    }

    private fun setHeader(it: SoulbindInformation) {
        when (it.chosenCovenant.id) {
            1 -> binding.renownCircle.setImageResource(R.drawable.renown_level_circle_kyrian)
            2 -> binding.renownCircle.setImageResource(R.drawable.renown_level_circle_venthyr)
            3 -> binding.renownCircle.setImageResource(R.drawable.renown_level_circle_nightfae)
            4 -> binding.renownCircle.setImageResource(R.drawable.renown_level_circle_necrolord)
        }
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

    private fun setAvatars(soulbindInformation: SoulbindInformation) {
        when (soulbindInformation.soulbinds.size) {
            1 -> setAvatar(binding.avatar1, binding.border1, soulbindInformation.soulbinds[0])
            2 -> {
                setAvatar(binding.avatar1, binding.border1, soulbindInformation.soulbinds[0])
                setAvatar(binding.avatar2, binding.border2, soulbindInformation.soulbinds[1])
            }
            3 -> {
                setAvatar(binding.avatar1, binding.border1, soulbindInformation.soulbinds[0])
                setAvatar(binding.avatar2, binding.border2, soulbindInformation.soulbinds[1])
                setAvatar(binding.avatar3, binding.border3, soulbindInformation.soulbinds[2])
            }
        }

        binding.renowmLevel.text = soulbindInformation.renownLevel.toString()
    }

    private fun setAvatar(avatar: ImageView, border: ImageView, soulbinds: Soulbinds) {
        when (soulbinds.soulbind.id) {
            1 -> avatar.setImageResource(R.drawable.niya)
            2 -> avatar.setImageResource(R.drawable.dreamweaver)
            3 -> avatar.setImageResource(R.drawable.general_draven)
            4 -> avatar.setImageResource(R.drawable.plague_deviser_marileth)
            5 -> avatar.setImageResource(R.drawable.emeni)
            6 -> avatar.setImageResource(R.drawable.korayn)
            7 -> avatar.setImageResource(R.drawable.pelagos)
            8 -> avatar.setImageResource(R.drawable.nadjia_the_mistblade)
            9 -> avatar.setImageResource(R.drawable.theotar_the_mad_duke)
            10 -> avatar.setImageResource(R.drawable.bonesmith_heirmir)
            13 -> avatar.setImageResource(R.drawable.kleia)
            18 -> avatar.setImageResource(R.drawable.forgelite_prime_mikanikos)
        }
        if(soulbinds.active){
            border.setImageResource(R.drawable.soulbinds_portrait_selected)
            val params = ConstraintLayout.LayoutParams(MetricConversion.getDPMetric(120, requireContext()), MetricConversion.getDPMetric(120, requireContext()))
            border.layoutParams = params
        }else{
            setGreyShade(avatar, 0f)
        }
    }

    private fun setGreyShade(avatar: ImageView , saturation: Float) {
        val matrix = ColorMatrix()
        matrix.setSaturation(saturation)
        avatar.colorFilter = ColorMatrixColorFilter(matrix)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun classEventReceived(classEvent: ClassEvent) {
        Log.i("class", "received")
        characterClass = classEvent.data
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnSpellTouched(spell: ImageView, covenantSpells: CovenantSpells) {
        spell.setOnTouchListener { view, motionEvent ->
            view.performClick()
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.spellTooltip.visibility = View.VISIBLE
                    if (covenantSpells.powerCost == null || covenantSpells.powerCost == "") {
                        binding.spellCost.visibility = View.GONE
                    } else {
                        binding.spellCost.visibility = View.VISIBLE
                    }
                    binding.spellName.text = covenantSpells.name
                    binding.spellCost.text = covenantSpells.powerCost
                    binding.spellCast.text = covenantSpells.castTime
                    binding.spellCd.text = covenantSpells.cooldown
                    binding.spellDescription.text = covenantSpells.description
                }
                MotionEvent.ACTION_UP -> {
                    binding.spellTooltip.visibility = View.GONE
                }
            }
            return@setOnTouchListener true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(character: String, realm: String, region: String) =
                WoWNavFragment().apply {
                    arguments = Bundle().apply {
                        putString(CHARACTER, character)
                        putString(REALM, realm)
                        putString(REGION, region)
                    }
                }
    }
}