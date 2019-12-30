package com.mgc.letobox.happy.fcm

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.leto.game.base.bean.LoginResultBean
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.fcm.timer.FcmTryPlayCountDownTimer
import com.mgc.letobox.happy.killProcess
import com.mgc.letobox.happy.model.Certification
import com.mgc.letobox.happy.model.IdCard
import com.mgc.letobox.happy.util.LeBoxSpUtil
import kotlinx.android.synthetic.main.fragment_anti_addiction.*

const val ARG_MODE = "mode"
const val ARG_TITLE = "title"
const val ARG_BUTTON_TEXT = "button_text"
const val ARG_CONTENT = "content"

class AntiAddictionDialog : DialogFragment(), LoginView, IDCardView {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            !isCancelable
        }
        isCancelable = false
        return inflater.inflate(R.layout.fragment_anti_addiction, container, false)
    }

    var currentMode = AntiAddictionDialogMode.MODE_PHONE
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            currentMode = it.getInt(ARG_MODE)
            val content = it.getString(ARG_CONTENT)
            val buttonText = it.getString(ARG_BUTTON_TEXT)
            val title = it.getString(ARG_TITLE)
            if (!TextUtils.isEmpty(content)) itemContent.text = content
            if (!TextUtils.isEmpty(title)) itemTitleCenter.text = title
            if (!TextUtils.isEmpty(buttonText)) itemButton.text = buttonText
        }
        if (currentMode == AntiAddictionDialogMode.MODE_PHONE) {
            if (loginPresenter.isSingedIn(activity)) {
                currentMode = AntiAddictionDialogMode.MODE_CERTIFICATION
                idCardPresenter.isHaveIdCard(loginPresenter.getMobile(activity), this)
            }
        }
        showMode(currentMode)
        initViews(view)
    }

    val loginPresenter = LoginPresenter()
    val idCardPresenter = IdCardPresenter()
    private fun initViews(view: View) {
        itemObtainPhoneCode.setOnClickListener {
            val mobile = itemPhone.text.toString()
            itemObtainPhoneCode.isEnabled = false
            loginPresenter.sendSms(activity, mobile, this)
        }
        itemLogin.setOnClickListener {
            val mobile = itemPhone.text.toString()
            val smscode = itemPhoneCode.text.toString()
            loginPresenter.login(activity, mobile, smscode, this)
        }
        itemSubmit.setOnClickListener {
            val name = itemIdName.text.toString()
            val idno = itemIdNumber.text.toString()
            if (TextUtils.isEmpty(name)) {
                toast(R.string.idname_not_valid)
                return@setOnClickListener
            }
            if (idCardPresenter.isIdCardValid(idno)) {
                idCardPresenter.submitIdCard(activity, name, idno, this)
            } else {
                toast(R.string.idno_not_valid)
            }
        }
        itemButton.setOnClickListener {
            killProcess(activity)
        }
        itemTryPlay.setOnClickListener {
            if (LeBoxSpUtil.tryPlayTime() < FcmTryPlayCountDownTimer.TOTAL_TIME) {
                dismiss()
            } else {
                toast(R.string.try_time_used_up)
            }
        }
    }

    private fun showMode(mode: Int) {
        when (mode) {
            AntiAddictionDialogMode.MODE_PHONE -> {
                itemCertification.visibility = View.GONE
                itemRegister.visibility = View.VISIBLE
                itemButton.visibility = View.GONE
                itemTitleLeft.visibility = View.VISIBLE
                itemTitleCenter.visibility = View.GONE
            }
            AntiAddictionDialogMode.MODE_CERTIFICATION -> {
                itemCertification.visibility = View.VISIBLE
                itemRegister.visibility = View.GONE
                itemButton.visibility = View.GONE
                itemTitleLeft.visibility = View.VISIBLE
                itemTitleCenter.visibility = View.GONE
                isCancelable = false
            }
            AntiAddictionDialogMode.MODE_CONTENT -> {
                itemCertification.visibility = View.GONE
                itemRegister.visibility = View.GONE
                itemButton.visibility = View.VISIBLE
                itemTitleLeft.visibility = View.GONE
                itemTitleCenter.visibility = View.VISIBLE
            }
        }
    }

    var onIdCardVerified: () -> Unit = {}

    companion object {
        private var isShown = false
        fun show(fragmentManager: FragmentManager, mode: Int, args: Bundle = Bundle()): AntiAddictionDialog? {
            if (isShown) return null
            val dialog = AntiAddictionDialog()
            dialog.arguments = args.apply {
                putInt(ARG_MODE, mode)
            }
            dialog.show(fragmentManager, "")
            isShown = true
            return dialog
        }

        fun showPhone(fragmentManager: FragmentManager): AntiAddictionDialog? {
            return show(fragmentManager, AntiAddictionDialogMode.MODE_PHONE)
        }

        fun showCertification(fragmentManager: FragmentManager): AntiAddictionDialog? {
            return show(fragmentManager, AntiAddictionDialogMode.MODE_CERTIFICATION)
        }

        fun showContent(fragmentManager: FragmentManager, title: String, content: String, buttonText: String): AntiAddictionDialog? {
            return show(fragmentManager, AntiAddictionDialogMode.MODE_CONTENT, Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_CONTENT, content)
                putString(ARG_BUTTON_TEXT, buttonText)
            })
        }

        private const val TAG = "AntiAddictionDialog"
    }

    override fun onDetach() {
        super.onDetach()
        countDownTimer?.cancel()
    }

    var countDownTimer: CountDownTimer? = null
    override fun onSmsSent() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60 * 1000, 1000) {
            override fun onFinish() {
                Log.i(TAG, "countDownTimber finished")
                view?.post {
                    Log.i(TAG, "countDownTimber finished 1")
                    itemObtainPhoneCode.setText(R.string.obtain_sms_code)
                    itemObtainPhoneCode.isEnabled = true
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                itemObtainPhoneCode.text = resources.getString(R.string.format_sms_code_count_down,
                        millisUntilFinished / 1000)
            }
        }
        countDownTimer?.start()
    }

    override fun onSmsSendFailed(errCode: String, errMsg: String) {
        Log.i(TAG, "onSmsSendFailed")
        view?.post {
            Log.i(TAG, "onSmsSendFailed 1")
            itemObtainPhoneCode.isEnabled = true
        }
        toast(errMsg)
    }

    override fun onIdCardBindSuccess(result: IdCard?) {
        // 如果成功，进入下一步
        // 否则，继续
        toast("idcard 验证成功")
        onIdCardVerified()
        dismiss()
    }

    override fun onIdCardBindFailed(code: String, message: String) {
        toast("idcard 验证失败 $message")
    }

    override fun onLogon(p0: LoginResultBean) {
        idCardPresenter.isHaveIdCard(p0.mobile, this)
    }

    override fun onLoginFailed(errCode: String, errMsg: String) {
        toast(errMsg)
    }

    override fun onHaveIdCard(result: Certification?) {
        toast(R.string.idcard_ok)
        dismiss()
    }

    override fun onIdCardNotBind(result: Certification?) {
        currentMode = AntiAddictionDialogMode.MODE_CERTIFICATION
        showMode(currentMode)
    }

    private fun toast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    private fun toast(resId: Int) {
        Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
    }
}

object AntiAddictionDialogMode {
    const val MODE_PHONE = 0
    const val MODE_CERTIFICATION = 1
    const val MODE_CONTENT = 2
}