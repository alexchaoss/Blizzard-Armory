package com.BlizzardArmory.ui.warcraft.covenant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.R
import com.BlizzardArmory.model.warcraft.covenant.character.soulbind.Traits
import com.BlizzardArmory.model.warcraft.covenant.techtalent.TechTalentWithIcon
import com.BlizzardArmory.model.warcraft.covenant.techtalenttree.Talents

class SoulbindViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val context: Context) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.wow_conduit_row, parent, false)) {

    private var node1: ConstraintLayout? = null
    private var node2: ConstraintLayout? = null
    private var node3: ConstraintLayout? = null

    private var ring1: ImageView? = null
    private var ring2: ImageView? = null
    private var ring3: ImageView? = null

    private var icon1: ImageView? = null
    private var icon2: ImageView? = null
    private var icon3: ImageView? = null

    private var bg1: ImageView? = null
    private var bg2: ImageView? = null
    private var bg3: ImageView? = null

    init {
        node1 = itemView.findViewById(R.id.node1)
        node2 = itemView.findViewById(R.id.node2)
        node3 = itemView.findViewById(R.id.node3)

        ring1 = node1!!.findViewById(R.id.ring)
        ring2 = node2!!.findViewById(R.id.ring)
        ring3 = node3!!.findViewById(R.id.ring)

        icon1 = node1!!.findViewById(R.id.bottom_icon)
        icon2 = node2!!.findViewById(R.id.bottom_icon)
        icon3 = node3!!.findViewById(R.id.bottom_icon)

        bg1 = node1!!.findViewById(R.id.bg)
        bg2 = node2!!.findViewById(R.id.bg)
        bg3 = node3!!.findViewById(R.id.bg)
    }

    fun bind(talents: List<Talents>, trait: Traits, icons: List<TechTalentWithIcon>) {
        /*val techTalents = talents.filter { it.tier == trait.tier }
        val talentIcons = icons.filter { it.tier == trait.tier }

        Log.i("TALENT SIZE", techTalents.size.toString())
        when (techTalents.size) {
            1 -> {
                node1?.visibility = View.INVISIBLE
                node3?.visibility = View.INVISIBLE
                when(techTalents[0].name){
                    "Endurance Conduit" -> icon2?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_protect)
                    "Finesse Conduit" -> icon2?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_attack)
                    "Potency Conduit" -> icon2?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_utility)
                }
            }
            2 -> {
                node2?.visibility = View.INVISIBLE
                techTalents.forEachIndexed { index, talents ->
                    if(index == 0){
                        when(techTalents[index].name){
                            "Endurance Conduit" -> icon1?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_protect)
                            "Finesse Conduit" -> icon1?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_attack)
                            "Potency Conduit" -> icon1?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_utility)
                        }
                    }else{
                        when(techTalents[index].name){
                            "Endurance Conduit" -> icon3?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_protect)
                            "Finesse Conduit" -> icon3?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_attack)
                            "Potency Conduit" -> icon3?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_utility)
                        }
                    }

                }
            }
            else ->{
                techTalents.forEachIndexed { index, talents ->
                    when (index) {
                        0 -> {
                            when(techTalents[index].name){
                                "Endurance Conduit" -> icon1?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_protect)
                                "Finesse Conduit" -> icon1?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_utility)
                                "Potency Conduit" -> icon1?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_attack)
                            }
                        }
                        1 -> {
                            when(techTalents[index].name){
                                "Endurance Conduit" -> icon2?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_protect)
                                "Finesse Conduit" -> icon2?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_utility)
                                "Potency Conduit" -> icon2?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_attack)
                            }
                        }
                        else -> {
                            when(techTalents[index].name){
                                "Endurance Conduit" -> icon3?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_protect)
                                "Finesse Conduit" -> icon3?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_utility)
                                "Potency Conduit" -> icon3?.setImageResource(R.drawable.soulbinds_tree_conduit_icon_attack)
                            }
                        }
                    }
                }
            }
        }

        if (techTalents.any { it.name.contains("Conduit") }) {
            ring1?.setImageResource(R.drawable.soulbinds_tree_conduit_ring_disabled)
            ring2?.setImageResource(R.drawable.soulbinds_tree_conduit_ring_disabled)
            ring3?.setImageResource(R.drawable.soulbinds_tree_conduit_ring_disabled)
            when(trait.display_order){
                0 -> ring1?.setImageResource(R.drawable.soulbinds_tree_conduit_ring)
                1 -> ring2?.setImageResource(R.drawable.soulbinds_tree_conduit_ring)
                2 -> ring3?.setImageResource(R.drawable.soulbinds_tree_conduit_ring)
            }
        } else {
            ring1?.setImageResource(R.drawable.soulbinds_tree_ring_disabled)
            ring2?.setImageResource(R.drawable.soulbinds_tree_ring_disabled)
            ring3?.setImageResource(R.drawable.soulbinds_tree_ring_disabled)
            icon1?.visibility = View.INVISIBLE
            icon2?.visibility = View.INVISIBLE
            icon3?.visibility = View.INVISIBLE
            when(trait.display_order){
                0 -> {
                    ring1?.setImageResource(R.drawable.soulbinds_tree_ring)
                    Glide.with(itemView).load(talentIcons.find { it.display_order == 0 }?.icon).circleCrop().into(bg1!!)
                }
                1 -> {
                    ring2?.setImageResource(R.drawable.soulbinds_tree_ring)
                    Glide.with(itemView).load(talentIcons.find { it.display_order == 1 }?.icon).circleCrop().into(bg2!!)
                }
                2 -> {
                    ring3?.setImageResource(R.drawable.soulbinds_tree_ring)
                    Glide.with(itemView).load(talentIcons.find { it.display_order == 2 }?.icon).circleCrop().into(bg3!!)
                }
            }
        }*/

    }
}