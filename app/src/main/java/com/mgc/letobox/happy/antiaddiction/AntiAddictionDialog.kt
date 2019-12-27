package com.mgc.letobox.happy.antiaddiction

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.mgc.letobox.happy.R
import kotlinx.android.synthetic.main.fragment_anti_addiction.*

const val ARG_MODE = "mode"

class AntiAddictionDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_anti_addiction, container, false)
    }

    var currentMode = AntiAddictionDialogMode.MODE_PHONE
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        arguments?.let {
            currentMode = it.getInt(ARG_MODE)
        }
        showMode(currentMode)
    }

    private fun showMode(mode: Int) {
        when (mode) {
            AntiAddictionDialogMode.MODE_PHONE -> {
                itemCertification.visibility = View.GONE
                itemPhone.visibility = View.VISIBLE
                itemButton.visibility = View.GONE
                itemTitleLeft.visibility = View.VISIBLE
                itemTitleCenter.visibility = View.GONE
            }
            AntiAddictionDialogMode.MODE_CERTIFICATION -> {
                itemCertification.visibility = View.VISIBLE
                itemPhone.visibility = View.GONE
                itemButton.visibility = View.GONE
                itemTitleLeft.visibility = View.VISIBLE
                itemTitleCenter.visibility = View.GONE
            }
            AntiAddictionDialogMode.MODE_CONTENT -> {
                itemCertification.visibility = View.GONE
                itemPhone.visibility = View.GONE
                itemButton.visibility = View.VISIBLE
                itemTitleLeft.visibility = View.GONE
                itemTitleCenter.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun show(fragmentManager: FragmentManager, mode: Int) {
            val dialog = AntiAddictionDialog()
            dialog.arguments = Bundle().apply {
                putInt(ARG_MODE, mode)
            }
            dialog.show(fragmentManager, "")
        }

        fun showPhone(fragmentManager: FragmentManager) {
            show(fragmentManager, AntiAddictionDialogMode.MODE_PHONE)
        }

        fun showCertification(fragmentManager: FragmentManager) {
            show(fragmentManager, AntiAddictionDialogMode.MODE_CERTIFICATION)
        }

        fun showContent(fragmentManager: FragmentManager) {
            show(fragmentManager, AntiAddictionDialogMode.MODE_CONTENT)
        }
    }
}

object AntiAddictionDialogMode {
    const val MODE_PHONE = 0
    const val MODE_CERTIFICATION = 1
    const val MODE_CONTENT = 2
}