package com.mobashop99.assistant;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.InputType;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import org.json.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    final int BG = Color.rgb(8, 17, 31);
    final int CARD = Color.rgb(14, 29, 50);
    final int GOLD = Color.rgb(255, 190, 76);
    final int CYAN = Color.rgb(67, 213, 255);
    final int TEXT = Color.WHITE;

    LinearLayout root, content, bottomNav;
    ScrollView scroll;
    String tab = "assistant";
    String game = "", pack = "", payment = "", proofUri = "";
    EditText etId, etName, etPhone, etTransfer;
    TextView proofLabel;
    static final int PICK_IMAGE = 991;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        buildShell();
        showAssistant();
    }

    void buildShell() {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(BG);
        setContentView(root);
        TextView header = tv("MOBA SHOP99", 24, GOLD, true);
        header.setGravity(Gravity.CENTER); header.setPadding(dp(16), dp(18), dp(16), dp(3));
        root.addView(header, new LinearLayout.LayoutParams(-1, -2));
        TextView sub = tv("فرعون مساعد الشحن", 14, Color.rgb(205,220,235), false);
        sub.setGravity(Gravity.CENTER); sub.setPadding(0,0,0,dp(10)); root.addView(sub);
        scroll = new ScrollView(this); content = new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(dp(14), dp(8), dp(14), dp(18)); scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        bottomNav = new LinearLayout(this); bottomNav.setPadding(dp(10), dp(8), dp(10), dp(10)); bottomNav.setGravity(Gravity.CENTER); bottomNav.setBackgroundColor(Color.rgb(6,12,23));
        root.addView(bottomNav, new LinearLayout.LayoutParams(-1, -2)); buildNav();
    }

    void buildNav() {
        bottomNav.removeAllViews();
        Button a = btn("👑 فرعون", tab.equals("assistant"));
        Button o = btn("📋 طلباتي", tab.equals("orders"));
        a.setOnClickListener(v -> { tab="assistant"; buildNav(); showAssistant(); });
        o.setOnClickListener(v -> { tab="orders"; buildNav(); showOrders(); });
        bottomNav.addView(a, new LinearLayout.LayoutParams(0, dp(50), 1));
        bottomNav.addView(o, new LinearLayout.LayoutParams(0, dp(50), 1));
    }

    void showAssistant() {
        content.removeAllViews(); resetFlow(false);
        addBot("اهلا بيك في MOBA SHOP99 👑\nانا فرعون مساعد الشحن. هعمل معاك الطلب خطوة بخطوة.");
        addSectionTitle("اختار اللعبة");
        String[] games = {"PUBG Mobile", "TikTok Coins", "Mobile Legends", "Free Fire", "FC Points", "Last War", "Roblox", "طلب مخصص"};
        addChoiceGrid(games, value -> { game = value; choosePack(); });
        addTinyActions();
    }

    void choosePack() {
        clearAfterTitle("اختار اللعبة");
        addUser("اللعبة: " + game);
        addBot("تمام. اختار الباقة او اكتب طلب مخصص.");
        addSectionTitle("اختار الباقة");
        ArrayList<String> packs = new ArrayList<>();
        if (game.contains("PUBG")) Collections.addAll(packs, "60 شدة - 50ج", "325 شدة - 235ج", "660 شدة - 435ج", "1800 شدة - 1120ج", "3850 شدة - 2170ج", "8100 شدة", "ازدهار 1", "ازدهار 2", "ازدهار 3", "طلب مخصص");
        else if (game.contains("TikTok")) Collections.addAll(packs, "70 عملة", "350 عملة", "850 عملة", "1750 عملة", "3500 عملة", "7000 عملة", "طلب مخصص");
        else if (game.contains("Mobile")) Collections.addAll(packs, "278 ألماسة - 260ج", "571 ألماسة - 500ج", "1192 ألماسة - 970ج", "1788 ألماسة - 1450ج", "3005 ألماسة - 2400ج", "طلب مخصص");
        else Collections.addAll(packs, "باقة صغيرة", "باقة متوسطة", "باقة كبيرة", "طلب مخصص");
        addChoiceGrid(packs.toArray(new String[0]), value -> { pack = value; askData(); });
        scrollDown();
    }

    void askData() {
        addUser("الباقة: " + pack);
        addBot("حلو. اكتب بيانات الاكونت عشان نسجل الطلب.");
        LinearLayout c = card();
        etId = input("ID / رقم الحساب");
        etName = input("اسم الحساب داخل اللعبة اختياري");
        etPhone = input("رقم الموبايل للمتابعة"); etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        c.addView(etId); c.addView(etName); c.addView(etPhone);
        Button next = solid("التالي: الدفع");
        next.setOnClickListener(v -> {
            if (etId.getText().toString().trim().isEmpty() || etPhone.getText().toString().trim().isEmpty()) { toast("اكتب ID ورقم الموبايل الاول"); return; }
            hideKeyboard(); choosePayment();
        });
        c.addView(next); content.addView(c); scrollDown();
    }

    void choosePayment() {
        addBot("اختار طريقة الدفع وبعدها ارفع سكرين التحويل.");
        addSectionTitle("طريقة الدفع");
        addChoiceGrid(new String[]{"Vodafone Cash", "InstaPay", "محل دفع / رقم تاني"}, value -> { payment=value; showProof(); });
        scrollDown();
    }

    void showProof() {
        addUser("الدفع: " + payment);
        LinearLayout c = card();
        TextView title = tv("تأكيد التحويل", 18, GOLD, true); c.addView(title);
        etTransfer = input("اخر 3 ارقام من رقم التحويل او رقم المحفظة"); etTransfer.setInputType(InputType.TYPE_CLASS_PHONE); c.addView(etTransfer);
        if (payment.contains("InstaPay")) {
            Button insta = outline("فتح لينك InstaPay");
            insta.setOnClickListener(v -> openUrl(Config.INSTAPAY_LINK)); c.addView(insta);
        }
        Button upload = solid("📷 اختار صورة التحويل");
        upload.setOnClickListener(v -> pickImage()); c.addView(upload);
        proofLabel = tv("لم يتم اختيار صورة", 13, Color.rgb(200,210,220), false); proofLabel.setPadding(0, dp(8),0,dp(8)); c.addView(proofLabel);
        Button submit = solid("✅ تنفيذ الطلب");
        submit.setOnClickListener(v -> submitOrder()); c.addView(submit);
        content.addView(c); scrollDown();
    }

    void submitOrder() {
        if (etTransfer == null || etTransfer.getText().toString().trim().isEmpty()) { toast("اكتب رقم تأكيد التحويل"); return; }
        String orderNo = "MOBA" + new SimpleDateFormat("MMddHHmm", Locale.ENGLISH).format(new Date());
        try {
            JSONObject o = new JSONObject();
            o.put("orderNo", orderNo); o.put("game", game); o.put("pack", pack); o.put("id", etId.getText().toString());
            o.put("name", etName.getText().toString()); o.put("phone", etPhone.getText().toString()); o.put("payment", payment); o.put("transfer", etTransfer.getText().toString());
            o.put("proof", proofUri); o.put("status", "قيد المراجعة"); o.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).format(new Date()));
            saveOrder(o);
            String msg = formatOrder(o);
            sendToTelegramBot(msg);
            addBot("تم تسجيل طلبك ✅\nرقم الطلب: " + orderNo + "\nالحالة: قيد المراجعة");
            Button share = outline("إرسال الطلب للدعم"); share.setOnClickListener(v -> shareText(msg)); content.addView(share);
            tab="orders"; buildNav(); showOrders();
        } catch(Exception e){ toast("حصل خطأ في تسجيل الطلب"); }
    }

    String formatOrder(JSONObject o) throws JSONException {
        return "طلب جديد من MOBA SHOP99\n"+
                "رقم الطلب: "+o.getString("orderNo")+"\n"+
                "اللعبة: "+o.getString("game")+"\n"+
                "الباقة: "+o.getString("pack")+"\n"+
                "ID: "+o.getString("id")+"\n"+
                "الاسم: "+o.optString("name")+"\n"+
                "الموبايل: "+o.getString("phone")+"\n"+
                "الدفع: "+o.getString("payment")+"\n"+
                "تأكيد التحويل: "+o.getString("transfer")+"\n"+
                "الوقت: "+o.getString("time");
    }

    void showOrders() {
        content.removeAllViews();
        addSectionTitle("سجل الطلبات");
        JSONArray arr = getOrders();
        if (arr.length()==0) { addBot("لسه مفيش طلبات. افتح فرعون وابدأ اول طلب."); return; }
        for (int i=arr.length()-1;i>=0;i--) {
            try {
                JSONObject o=arr.getJSONObject(i); LinearLayout c=card();
                c.addView(tv(o.getString("orderNo") + "  •  " + o.getString("status"), 17, GOLD, true));
                c.addView(tv(o.getString("game") + " - " + o.getString("pack"), 15, TEXT, false));
                c.addView(tv("ID: " + o.getString("id") + "\n" + o.getString("time"), 13, Color.rgb(200,210,220), false));
                Button support = outline("مشكلة في الطلب"); support.setOnClickListener(v -> openUrl(Config.SUPPORT_TELEGRAM)); c.addView(support);
                content.addView(c);
            } catch(Exception ignored) {}
        }
    }

    void addTinyActions(){
        LinearLayout c=card(); c.addView(tv("اختصارات", 16, GOLD, true));
        Button store=outline("تليجرام الستور"); store.setOnClickListener(v->openUrl(Config.TELEGRAM_STORE)); c.addView(store);
        Button support=outline("الدعم"); support.setOnClickListener(v->openUrl(Config.SUPPORT_TELEGRAM)); c.addView(support);
        content.addView(c);
    }

    void saveOrder(JSONObject o) throws JSONException { JSONArray arr=getOrders(); arr.put(o); getPreferences(0).edit().putString("orders", arr.toString()).apply(); }
    JSONArray getOrders(){ try { return new JSONArray(getPreferences(0).getString("orders", "[]")); } catch(Exception e){ return new JSONArray(); } }

    void sendToTelegramBot(String text) {
        if (Config.TELEGRAM_BOT_TOKEN.length()<5 || Config.ADMIN_CHAT_ID.length()<2) return;
        new Thread(() -> { try {
            String url = "https://api.telegram.org/bot"+Config.TELEGRAM_BOT_TOKEN+"/sendMessage";
            String data = "chat_id="+URLEncoder.encode(Config.ADMIN_CHAT_ID,"UTF-8")+"&text="+URLEncoder.encode(text,"UTF-8");
            HttpURLConnection con=(HttpURLConnection)new URL(url).openConnection(); con.setRequestMethod("POST"); con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes("UTF-8")); con.getInputStream().close();
        } catch(Exception ignored){} }).start();
    }

    void pickImage(){ Intent i=new Intent(Intent.ACTION_GET_CONTENT); i.setType("image/*"); i.addCategory(Intent.CATEGORY_OPENABLE); startActivityForResult(Intent.createChooser(i,"اختار صورة التحويل"), PICK_IMAGE); }
    @Override protected void onActivityResult(int r,int c,Intent d){ super.onActivityResult(r,c,d); if(r==PICK_IMAGE && c==RESULT_OK && d!=null){ proofUri = d.getData().toString(); if(proofLabel!=null) proofLabel.setText("تم اختيار الصورة ✅"); } }

    interface Choice { void onPick(String v); }
    void addChoiceGrid(String[] items, Choice listener){ LinearLayout wrap = new LinearLayout(this); wrap.setOrientation(LinearLayout.VERTICAL); for(String s:items){ Button b=outline(s); b.setOnClickListener(v->listener.onPick(s)); wrap.addView(b); } content.addView(wrap); }
    LinearLayout card(){ LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setPadding(dp(14),dp(14),dp(14),dp(14)); c.setBackgroundColor(CARD); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,0,0,dp(12)); c.setLayoutParams(lp); return c; }
    TextView tv(String s,int sp,int color,boolean bold){ TextView t=new TextView(this); t.setText(s); t.setTextSize(sp); t.setTextColor(color); t.setLineSpacing(4,1); if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); return t; }
    void addBot(String s){ LinearLayout c=card(); c.addView(tv("👑 فرعون", 13, CYAN, true)); c.addView(tv(s, 16, TEXT, false)); content.addView(c); }
    void addUser(String s){ TextView t=tv(s, 15, Color.rgb(220,230,240), true); t.setGravity(Gravity.RIGHT); t.setPadding(0,dp(8),0,dp(8)); content.addView(t); }
    void addSectionTitle(String s){ TextView t=tv(s, 18, GOLD, true); t.setPadding(0, dp(8),0,dp(8)); content.addView(t); }
    EditText input(String hint){ EditText e=new EditText(this); e.setHint(hint); e.setTextColor(TEXT); e.setHintTextColor(Color.rgb(150,165,180)); e.setSingleLine(false); e.setPadding(dp(12),0,dp(12),0); return e; }
    Button btn(String s, boolean active){ Button b=new Button(this); b.setText(s); b.setTextColor(active?Color.BLACK:TEXT); b.setBackgroundColor(active?GOLD:CARD); return b; }
    Button solid(String s){ Button b=new Button(this); b.setText(s); b.setTextColor(Color.BLACK); b.setTypeface(Typeface.DEFAULT,Typeface.BOLD); b.setBackgroundColor(GOLD); return b; }
    Button outline(String s){ Button b=new Button(this); b.setText(s); b.setTextColor(TEXT); b.setBackgroundColor(Color.rgb(22,43,70)); return b; }
    void openUrl(String url){ try{ startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); }catch(Exception e){ toast("مش قادر افتح الرابط"); } }
    void shareText(String text){ Intent i=new Intent(Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(Intent.EXTRA_TEXT,text); startActivity(Intent.createChooser(i,"إرسال الطلب")); }
    void toast(String s){ Toast.makeText(this,s,Toast.LENGTH_SHORT).show(); }
    int dp(int v){ return (int)(v*getResources().getDisplayMetrics().density+0.5f); }
    void scrollDown(){ scroll.postDelayed(()->scroll.fullScroll(View.FOCUS_DOWN),150); }
    void hideKeyboard(){ try{ ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(root.getWindowToken(),0);}catch(Exception ignored){} }
    void resetFlow(boolean all){ if(all){game=pack=payment=proofUri="";} }
    void clearAfterTitle(String marker){ /* intentionally keeps chat history for natural assistant flow */ }
}
