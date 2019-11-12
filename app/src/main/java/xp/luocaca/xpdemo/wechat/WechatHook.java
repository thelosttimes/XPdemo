package xp.luocaca.xpdemo.wechat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import xp.luocaca.xpdemo.shell.ShellUtils;
import xp.luocaca.xpdemo.wechat.iwechat.IWechat;
import xp.luocaca.xpdemo.wechat.share.WechatShareObject;
import xp.luocaca.xpdemo.粗话系统.SensitivewordFilter;

/**
 * hook 微信密码矿
 */
public class WechatHook implements IXposedHookLoadPackage, IWechat {


    public static final String 微信包名 = "com.tencent.mm";
    public static final String 发红包界面 = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyPrepareUI";


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        微信首页(lpparam);

        口令发红包(lpparam);
        自动输入密码(lpparam);
        微信方法堆栈(lpparam);
        微信方法日志i(lpparam);
        聊天消息监听(lpparam);

    }


    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        handleLoadPackage(lpparam);
    }


    @Override
    public void 口令发红包(XC_LoadPackage.LoadPackageParam lpparam) {


        XposedHelpers.findAndHookMethod(
                发红包界面,
                lpparam.classLoader,
                "onCreate",
                Bundle.class,
                new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Activity thisObject = ((Activity) param.thisObject);

                        int chatId = thisObject.getIntent().getIntExtra("key_chatroom_num", 0);

                        Toast.makeText(thisObject, "我被你抓到了。亲\n" + thisObject.getIntent().getExtras().toString(), Toast.LENGTH_SHORT).show();

                        //反射打印所有方法 与属性


                        String price = thisObject.getIntent().getExtras().getString("key_price");

                        List<EditText> editTexts = new ArrayList<>();


                        getAnyViews(((FrameLayout) thisObject.getWindow().getDecorView()), editTexts, new IInstance666() {
                            @Override
                            public boolean invokeInstance(View view) {
                                return view instanceof EditText;
                            }
                        });


                        int 基数 = 1;
                        if (WechatShareObject.回复的敏感词.contains("1点")) {
                            基数 = 1;
                        } else if (WechatShareObject.回复的敏感词.contains("2点")) {
                            基数 = 2;
                        } else if (WechatShareObject.回复的敏感词.contains("3点")) {
                            基数 = 3;
                        } else if (WechatShareObject.回复的敏感词.contains("4点")) {
                            基数 = 4;
                        } else if (WechatShareObject.回复的敏感词.contains("5点")) {
                            基数 = 5;
                        } else if (WechatShareObject.回复的敏感词.contains("6点")) {
                            基数 = 6;
                        }

                        double realPrice = Double.parseDouble(price) * 基数;

                        for (int i = 0; i < editTexts.size(); i++) {
                            if (i == 0) {
                                editTexts.get(i).setText("" + realPrice);
                            } else if (i == 1) {
                                editTexts.get(i).setText("" + 1);
                            } else if (i == 2) {
                                editTexts.get(i).setText(WechatShareObject.回复的敏感词);
//                                editTexts.get(i).setText("自动红包");
                            }
                        }


                        List<TextView> buttons = new ArrayList<>();


                        getAnyViews(((FrameLayout) thisObject.getWindow().getDecorView()), buttons, new IInstance666() {
                            @Override
                            public boolean invokeInstance(View view) {
                                return view instanceof TextView;
                            }
                        });


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (TextView button : buttons) {
                                    XposedBridge.log("---------看看有多少个被找到--------" + button.getText());


                                    if (button.getText().toString().equals("塞钱进红包")) {
                                        button.performClick();
                                    }
                                }
                            }
                        }, 1000);


                        XposedBridge.log("-------extras print -----\n" + thisObject.getIntent().getExtras().toString());
                        XposedBridge.log("-------extras print -----\n" + thisObject.getIntent().getExtras().toString());
                        XposedBridge.log("-------extras print -----\n" + thisObject.getIntent().getExtras().toString());
                        XposedBridge.log("-------extras print -----\n" + thisObject.getIntent().getExtras().toString());
                        XposedBridge.log("-------extras print -----\n" + thisObject.getIntent().getExtras().toString());


                    }
                });

    }

    @Override
    public void 自动输入密码(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedBridge.log("-------微信密码-----被hook到了------");

        // private void aI(Context context) {
        //package com.tencent.mm.wallet_core.ui.formview;
        //EditHintPasswdView
        XposedHelpers.findAndHookMethod(
                "com.tencent.mm.wallet_core.ui.formview.EditHintPasswdView",
                lpparam.classLoader,
                "aI",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Toast.makeText(((Context) param.args[0]), "密码被hook到", Toast.LENGTH_SHORT).show();
                        EditText editText = (EditText) XposedHelpers.findField(param.thisObject.getClass(), "mEditText").get(param.thisObject);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                XposedBridge.log("-------2秒后主动输入2个文字---------");
                                editText.setText(WechatShareObject.password);
                            }
                        }, 0);

                        try {
                            throw new NullPointerException("--------自定义异常抛出，查看堆栈-------");
                        } catch (Exception e) {
                            XposedBridge.log(e);
                        }
                    }
                });

    }

    @Override
    public void 微信方法堆栈(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("-------微信方法堆栈-----被hook到了------");

        //package com.tencent.matrix.trace.core;
        //public class AppMethodBeat {
        //public static void o(int i) {
//        XposedHelpers.findAndHookMethod(
//                "com.tencent.matrix.trace.core.AppMethodBeat",
//                lpparam.classLoader,
//                "o",
//                int.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        super.afterHookedMethod(param);
//                        XposedBridge.log("-----AppMethodBeat堆栈记录------- " + param.args[0]);
//
////                        new Thread(new Runnable() {
////                            @Override
////                            public void run() {
////                                try {
////                                    throw new NullPointerException("--------AppMethodBeat堆栈记录 主动抛出异常-----");
////                                } catch (Exception e) {
////                                    XposedBridge.log(e);
////                                }
////                            }
////                        }).start();
//
//                    }
//                });
    }

    @Override
    public void 微信方法日志i(XC_LoadPackage.LoadPackageParam lpparam) {
//        public static void i(String str, String str2) {
        XposedHelpers.findAndHookMethod(
                "com.tencent.mm.sdk.platformtools.ab",
                lpparam.classLoader,
                "i",
                String.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        XposedBridge.log("-----微信日志 " + param.args[0] + " " + param.args[1]);
                    }
                });
    }


    public static long lastSendTime = 0;


    private static SensitivewordFilter filter;

    @Override
    public void 聊天消息监听(XC_LoadPackage.LoadPackageParam lpparam) {
        /**
         * 插入消息监听
         */
        XposedBridge.log("---------监听微信消息---------- 开始");
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = String.class;
        arrayOfObject[1] = String.class;
        arrayOfObject[2] = ContentValues.class;
        arrayOfObject[3] = new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam) throws XmlPullParserException, IOException {
                //5.577 24979-25044/? I/EdXposed-Bridge: 监听微信消息 insertMsgDBListener 0message
                //1573359185.577 24979-25044/? I/EdXposed-Bridge: 监听微信消息insertMsgDBListener 1msgId
                //insertMsgDBListener 2bizClientMsgId=
                // talker=gh_3dfda90e39d6
                // flag=0 bizChatId=-1
                // msgId=981
                // type=1 content=您好，很高兴认识您~
                // msgSvrId=6203557812977276320
                // lvbuffer=[B@d2cc7df createTime=1573359183000
                // talkerId=110 isSend=0 msgSeq=678076699
                // status=3
                XposedBridge.log("监听微信消息 insertMsgDBListener talbe--> " + paramAnonymousMethodHookParam.args[0]);
                XposedBridge.log("监听微信消息insertMsgDBListener msgId-->" + paramAnonymousMethodHookParam.args[1]);
                XposedBridge.log("监听微信消息insertMsgDBListener content value -->" + paramAnonymousMethodHookParam.args[2].toString());

                ContentValues contentValues = (ContentValues) paramAnonymousMethodHookParam.args[2];


                String talker = contentValues.getAsString("talker");

                String content = contentValues.getAsString("content");

                XposedBridge.log("------微信消息内容---content------" + content);

                if (content == null) {
                    return;
                }
                if (talker == null) {
                    return;
                }


                new
                        Thread(new Runnable() {


                    @Override
                    public void run() {
                        XposedBridge.log("----------微信消息内容----------" + contentValues.getAsString("content"));
                        XposedBridge.log("----------微信消息内容----------" + contentValues.getAsString("talker"));

                        //@chatroom


                        if (filter == null) {
                            filter = new SensitivewordFilter();
                        }
                        System.out.println("敏感词的数量：" + filter.sensitiveWordMap.size());

                        System.out.println("待检测语句字数：" + content.length());
                        long beginTime = System.currentTimeMillis();
                        Set<String> set = filter.getSensitiveWord(content, 1);
                        long endTime = System.currentTimeMillis();
                        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
                        System.out.println("总共消耗时间为：" + (endTime - beginTime));

                        String num1 = "da1c289d4e363f3ce1ff36538903b92f";
                        String num2 = "9e3f303561566dc9342a3ea41e6552a6";
                        String num3 = "dbcc51db2765c1d0106290bae6326fc4";
                        String num4 = "9a21c57defc4974ab5b7c842e3232671";
                        String num5 = "3a8e16d650f7e66ba5516b2780512830";
                        String num6 = "5ba8e9694b853df10b9f2a77b312cc09";


                        if (content.contains("我要红包") || set.size() > 0 ||
                                content.contains(num1) ||
                                content.contains(num2) ||
                                content.contains(num3) ||
                                content.contains(num4) ||
                                content.contains(num5) ||
                                content.contains(num6)
                        ) {
//                        if (content.contains("我要红包") || set.size() > 0) {


                            if (content.contains("不许说脏话") || content.contains("给你想要的")) {
                                System.out.println("不许说脏话===========过滤死循环===");
                                return;
                            }
                            if (content.contains("我要红包")) {
                                WechatShareObject.回复的敏感词 = "给你想要的。。";
                            } else if (content.contains(num1)) {
                                WechatShareObject.回复的敏感词 = "红包1点";
                            } else if (content.contains(num2)) {
                                WechatShareObject.回复的敏感词 = "红包2点。。";
                            } else if (content.contains(num3)) {
                                WechatShareObject.回复的敏感词 = "红包3点。。";
                            } else if (content.contains(num4)) {
                                WechatShareObject.回复的敏感词 = "红包4点。。";
                            } else if (content.contains(num5)) {
                                WechatShareObject.回复的敏感词 = "红包5点。。";
                            } else if (content.contains(num6)) {
                                WechatShareObject.回复的敏感词 = "红包6点。。";
                            } else {
                                WechatShareObject.回复的敏感词 = "不许说脏话：" + set.iterator().next();
                            }


                            if (lastSendTime != 0) {
                                if (System.currentTimeMillis() - lastSendTime < 5000) {
                                    XposedBridge.log("-----------微信消息内容-----5秒防刷机制--------");
                                    return;
                                } else {
                                    lastSendTime = System.currentTimeMillis();
                                }
                            } else {
                                lastSendTime = System.currentTimeMillis();
                            }


                            StringBuffer stringBuffer = new StringBuffer();

                            if (talker.endsWith("@chatroom")) {
                                stringBuffer.append(" --ei pay_channel 14");
                                stringBuffer.append(" --ei key_way 1");
                                stringBuffer.append(" --ei key_from 1");
                                stringBuffer.append(" --ei key_type 1");
                                stringBuffer.append(" --es key_price 0.01");
                                stringBuffer.append(" --ei key_chatroom_num 1");
                                stringBuffer.append(" --es key_username " + talker);
                            } else {
                                stringBuffer.append(" --ei pay_channel 11");
                                stringBuffer.append(" --ei key_way 0");
                                stringBuffer.append(" --ei key_from 1");
                                stringBuffer.append(" --es key_price 0.01");
                                stringBuffer.append(" --ei key_type 0");
                                stringBuffer.append(" --es key_username " + talker);
                            }

//                        stringBuffer.append(" --es key_username wxid_e3aaorpcfn3e22");
                            ShellUtils.execCommand("am start -n " + WechatHook.微信包名 + "/" + WechatHook.发红包界面 + stringBuffer, true);

                            //群消息微信消息内容
                            //bizClientMsgId= talker=5491751483@chatroom flag=0 bizChatId=-1 msgId=986 type=1 content=huweiws707242308:
                            //    荣耀带的动吗？ msgSvrId=2653146997463004420 lvbuffer=[B@7196ba7 createTime=1573359521000 talkerId=543 isSend=0 msgSeq=678076704 status=3


//                            监听微信消息insertMsgDBListener content
//                            value-- > bizClientMsgId = talker = weixin flag = 0 bizChatId = -1
//                            msgId = 985 type = 1 content = 好的。msgSvrId = 2949682216028027455
//                            lvbuffer =[B @ece484d createTime = 1573359448000 talkerId = 139
//                            isSend = 0 msgSeq = 678076702 status = 3
//                            监听微信消息 insertMsgDBListener talbe-- > message
//                            监听微信消息insertMsgDBListener msgId -- > msgId
//                            监听微信消息insertMsgDBListener content
//                            value-- > bizClientMsgId = talker = 5491751483 @chatroom flag = 0
//                            bizChatId = -1 msgId = 986 type = 1 content = huweiws707242308:
//                            荣耀带的动吗？msgSvrId = 2653146997463004420 lvbuffer =[B @ 7196
//                            ba7 createTime = 1573359521000 talkerId = 543 isSend = 0
//                            msgSeq = 678076704 status = 3

                        }


                    }
                }).start();


                //talker=weixin


            }
        };
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.tencent.wcdb.database.SQLiteDatabase", lpparam.classLoader), "insert", arrayOfObject);
        XposedBridge.log("监听微信消息insertMsgDBListener 结束");
    }

    @Override
    public void 微信首页(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
                "com.tencent.mm.ui.LauncherUI",
                lpparam.classLoader,
                "onCreate",
                Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Activity thisObject = ((Activity) param.thisObject);

                        WechatShareObject.password = thisObject.getIntent().getStringExtra("password");

                        WechatShareObject.wechatLaunchActivity = thisObject;
                        WechatShareObject.wechatApplication = thisObject.getApplication();


                    }
                });
    }


    /**
     * 获取当前界面所有的 想要的view
     */

    public static void getAnyViews(ViewGroup viewGroup, List listViews, IInstance666 iInstance) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            if (viewGroup.getChildAt(i) instanceof ViewGroup) {

                getAnyViews((ViewGroup) viewGroup.getChildAt(i), listViews, iInstance);

            } else {

                View view = viewGroup.getChildAt(i);

                if (iInstance.invokeInstance(view)) {

                    listViews.add(view);

                }

            }

        }


    }

    /**
     * 类型判断回调接口， instanceof 不懂怎么写态传类型 。所以用回调。
     * <p>
     * 要是有大佬能指点下。请回复哦——哈哈
     */

    public interface IInstance666 {

        boolean invokeInstance(View view);

    }
}
