package com.r0adkll.deckbuilder.arch.ui.features.settings

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceViewHolder
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.ftinc.kit.kotlin.extensions.color
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R


class ProfilePreference : Preference {

    var avatarUrl: Uri? = null
        set(value) {
            field = value
            notifyChanged()
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        layoutResource = R.layout.layout_profile_preference
    }


    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val avatar = holder.findViewById(R.id.avatar) as ImageView
        if (avatarUrl != null) {
            GlideApp.with(avatar)
                    .load(avatarUrl)
                    .placeholder(ColorDrawable(context.color(R.color.grey_400)))
                    .transition(withCrossFade())
                    .into(avatar)
        } else {
            avatar.setImageResource(R.drawable.dr_avatar_offline)
        }
    }
}