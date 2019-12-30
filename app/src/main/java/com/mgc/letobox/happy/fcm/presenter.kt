package com.mgc.letobox.happy.fcm

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.google.gson.Gson
import com.kymjs.rxvolley.RxVolley
import com.ledong.lib.leto.LetoConst
import com.leto.game.base.bean.LoginMobileRequestBean
import com.leto.game.base.bean.LoginResultBean
import com.leto.game.base.bean.SmsSendRequestBean
import com.leto.game.base.bean.SmsSendResultBean
import com.leto.game.base.event.GetCoinEvent
import com.leto.game.base.http.HttpCallbackDecode
import com.leto.game.base.http.HttpParamsBuild
import com.leto.game.base.http.SdkApi
import com.leto.game.base.login.LoginManager
import com.leto.game.base.util.BaseAppUtil
import com.leto.game.base.util.MResource
import com.mgc.letobox.happy.model.Certification
import com.mgc.letobox.happy.model.DataCenter
import com.mgc.letobox.happy.model.IdCard
import com.mgc.letobox.happy.util.LeBoxConstant
import com.mgc.letobox.happy.util.LeBoxSpUtil
import org.greenrobot.eventbus.EventBus
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.concurrent.thread


interface LoginView {
    fun onSmsSent()
    fun onSmsSendFailed(errCode: String, errMsg: String)
    fun onLogon(p0: LoginResultBean)
    fun onLoginFailed(errCode: String, errMsg: String)
}

class LoginPresenter {
    private fun sendSmsInternal(context: Context, account: String, loginView: LoginView) {
        val smsSendRequestBean = SmsSendRequestBean()

        val userToken: String? = LoginManager.getUserToken(context)
        smsSendRequestBean.user_token = userToken
        smsSendRequestBean.setMobile(account)
        smsSendRequestBean.setSmstype(SmsSendRequestBean.TYPE_LOGIN)
        val httpParamsBuild = HttpParamsBuild(Gson().toJson(smsSendRequestBean))
        val httpCallbackDecode: HttpCallbackDecode<*> = object : HttpCallbackDecode<SmsSendResultBean?>(context, httpParamsBuild.authkey) {
            override fun onDataSuccess(data: SmsSendResultBean?) {
                //开始计时控件
                if (data != null) {
                    loginView.onSmsSent()
                }
            }

            override fun onFailure(code: String, message: String) {
                loginView.onSmsSendFailed(code, message)
            }
        }
        httpCallbackDecode.isShowTs = true
        httpCallbackDecode.isLoadingCancel = false
        httpCallbackDecode.isShowLoading = true
        httpCallbackDecode.loadMsg = context.getResources().getString(MResource.getIdByName(context, "R.string.loading"))
        RxVolley.post(SdkApi.getSendCode(), httpParamsBuild.httpParams, httpCallbackDecode)
    }

    fun sendSms(context: Context, account: String, loginView: LoginView) {
        sendSmsInternal(context, account, loginView)
/*
        SendSmsInteract.sendSMS(context, account, object : SendSmsListener {
            override fun onSuccess() {
                loginView.onSmsSent()
            }

            override fun onFail(errCode: String, errMsg: String) {
                loginView.onSmsSendFailed(errCode, errMsg)
            }
        })
*/
    }

    private fun loginInternal(context: Context, account: String, smscode: String, loginView: LoginView) {
        val loginMobileRequestBean = LoginMobileRequestBean()
        //mgc_mobile 改成必传参数
        val userId: String? = LoginManager.getUserId(context)
        loginMobileRequestBean.mgc_mobile = userId
        loginMobileRequestBean.mobile = account
        loginMobileRequestBean.smscode = smscode
        loginMobileRequestBean.smstype = SmsSendRequestBean.TYPE_LOGIN

        val userToken: String? = LoginManager.getUserToken(context)
        loginMobileRequestBean.user_token = userToken

        val httpParamsBuild = HttpParamsBuild(Gson().toJson(loginMobileRequestBean))
        val httpCallbackDecode: HttpCallbackDecode<*> = object : HttpCallbackDecode<LoginResultBean?>(context, httpParamsBuild.authkey) {
            override fun onDataSuccess(data: LoginResultBean?) {
                if (data != null) {
                    //保存登录信息
                    LoginManager.saveLoginInfo(context, data)
                    EventBus.getDefault().post(GetCoinEvent())
                    loginView.onLogon(data)
                }
            }

            override fun onFailure(code: String, message: String) {
                loginView.onLoginFailed(code, message)
            }
        }
        httpCallbackDecode.isShowTs = true
        httpCallbackDecode.isLoadingCancel = false
        httpCallbackDecode.isShowLoading = true
        httpCallbackDecode.loadMsg = context.getResources().getString(MResource.getIdByName(context, "R.string.loading_login"))
        RxVolley.post(SdkApi.getLoginRegister(), httpParamsBuild.httpParams, httpCallbackDecode)
    }

    fun login(context: Context, account: String, smscode: String, loginView: LoginView) {
        loginInternal(context, account, smscode, loginView)
/*
        LoginInteract.submitLogin(context, account, smscode, object : LoginInteract.LoginListener {
            override fun onSuccess(p0: LoginResultBean) {
                LoginManager.saveLoginInfo(context, p0)
                loginView.onLogon(p0)
            }

            override fun onFail(errCode: String, msg: String) {
                loginView.onLoginFailed(errCode, msg)
            }
        })
*/
    }

    fun isSingedIn(context: Context): Boolean {
        return LoginManager.isSignedIn(context)
    }

    fun getMobile(context: Context): String {
        return LoginManager.getMobile(context)
    }

    fun isPhoneValid(account: String): Boolean {
        return isMobileNumber(account)
    }

    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     * @return
     */
    fun isMobileNumber(mobiles: String?): Boolean {
        val p: Pattern = Pattern.compile("^((1[0-9][0-9]))\\d{8}$")
        val m: Matcher = p.matcher(mobiles)
        return m.matches()
    }
}

interface IDCardView {
    fun onIdCardBindSuccess(result: IdCard?)
    fun onHaveIdCard(result: Certification?)
    fun onIdCardNotBind(result: Certification?)
    fun onIdCardBindFailed(code: String, message: String)
}

class IdCardPresenter {
    val uiThread = Handler(Looper.getMainLooper())
    fun isHaveIdCard(mobile: String, idCardView: IDCardView) {
        thread {
            val result = DataCenter.requestCertification(mobile, LetoConst.SDK_OPEN_TOKEN)
            uiThread.post {
                if (result != null && result.is_have_idcard == 1) {
                    if (!TextUtils.isEmpty(result.idcard)) {
                        LeBoxSpUtil.saveIdCard(mobile, result.idcard)
                    }
                    idCardView.onHaveIdCard(result)
                } else {
                    idCardView.onIdCardNotBind(result)
                }
            }
        }
    }

    fun submitIdCard(context: Context, name: String, idNumber: String, idCardView: IDCardView) {
        submitIdCardInternal(context, name, idNumber, idCardView)
        /*thread {
            val result = DataCenter.requestIdCard(name, idNumber, LoginManager.getUserToken(context), toInt(BaseAppUtil.getChannelID(context)), LoginManager.getMobile(context))
            uiThread.post {
                idCardView.onIdCardBindSuccess(result)
            }
        }*/
    }

    private fun submitIdCardInternal(context: Context, name: String, idNumber: String, idCardView: IDCardView) {
        val userToken: String? = LoginManager.getUserToken(context)

        val idCardBean = IDCardBean(name, idNumber, userToken
                ?: "", toInt(BaseAppUtil.getChannelID(context)), LoginManager.getMobile(context))
        val httpParamsBuild = HttpParamsBuild(Gson().toJson(idCardBean))
        val httpCallbackDecode: HttpCallbackDecode<*> = object : HttpCallbackDecode<IdCard?>(context, httpParamsBuild.authkey) {
            override fun onDataSuccess(data: IdCard?) {
                idCardView.onIdCardBindSuccess(data)
            }

            override fun onFailure(code: String, message: String) {
                idCardView.onIdCardBindFailed(code, message)
            }
        }
        httpCallbackDecode.isShowTs = true
        httpCallbackDecode.isLoadingCancel = false
        httpCallbackDecode.isShowLoading = true
        httpCallbackDecode.loadMsg = context.getResources().getString(MResource.getIdByName(context, "R.string.loading_login"))
        RxVolley.post(getIdCardUrl(context), httpParamsBuild.httpParams, httpCallbackDecode)
    }

    fun getIdCardUrl(context: Context): String {
        val TEST_ENV = BaseAppUtil.getMetaBooleanValue(context, "MGC_TEST_ENV")
        val baseUrl = if (TEST_ENV) LeBoxConstant.MGCServerUrlDev else LeBoxConstant.MGCServerUrl
        return "$baseUrl/api/v7/fcm/idcard"
    }

    fun isIDNumber(IDNumber: String?): Boolean {
        if (IDNumber == null || "" == IDNumber) {
            return false
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）

        val regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)"
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        val matches = IDNumber.matches(Regex(regularExpression))

        //判断第18位校验值


        if (matches) {
            if (IDNumber.length == 18) {
                return try {
                    val charArray = IDNumber.toCharArray()
                    //前十七位加权因子


                    val idCardWi = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
                    //这是除以11后，可能产生的11位余数对应的验证码


                    val idCardY = arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")
                    var sum = 0
                    for (i in idCardWi.indices) {
                        val current = Integer.parseInt(java.lang.String.valueOf(charArray[i]))
                        val count = current * idCardWi[i]
                        sum += count
                    }
                    val idCardLast = charArray[17]
                    val idCardMod = sum % 11
                    if (idCardY[idCardMod].toUpperCase() == idCardLast.toString().toUpperCase()) {
                        true
                    } else {
                        println("身份证最后一位:" + idCardLast.toString().toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase())
                        false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("异常:$IDNumber")
                    false
                }
            }
        }
        return matches
    }

    private fun toInt(text: String?): Int {
        return try {
            Integer.valueOf(text)
        } catch (e: java.lang.Exception) {
            0
        }
    }

    fun isIdCardValid(idno: String): Boolean {
        return isIDNumber(idno)
    }
}