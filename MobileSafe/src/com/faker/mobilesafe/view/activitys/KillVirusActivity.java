package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.util.ImageUtil;
import com.faker.mobilesafe.util.MD5;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: LichFaker
 * Date: 14-4-3
 * Time: 上午11:43
 * Email: lichfaker@gmail.com
 */
public class KillVirusActivity extends Activity implements View.OnClickListener {

    private final static int SCANNING = 0;
    private final static int FINISH_SCAN = 1;

    private Button backButton;
    private TextView safeState;
    private ImageView iv_safeState;
    private TextView scanHint;
    private ScrollView scrollView;
    private LinearLayout ll_scanning;
    private LinearLayout linearLayout1;
    private LinearLayout ll_application;
    private Button killVirus;
    private Button scanButton;
    private ImageView iv_scan;

    private Animation ad;

    // 病毒库中的病毒
    private List<String> allVirus;
    // 当前扫描出的病毒
    private List<String> existVirus;
    // 病毒库文件
    private File file;
    // 是否是第一次扫描
    private boolean isFirst = true;

    private PackageManager pm;

    private LayoutInflater inflater;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.killvirus_main);
        initUI();

        allVirus = new ArrayList<String>();
        existVirus = new ArrayList<String>();
        file = new File(getFilesDir(), "antivirus.db");

        pm = getPackageManager();
        inflater = LayoutInflater.from(this);

        moveVirusDb();
    }

    /**
     * 初始化界面控件
     */
    private void initUI() {
        // 返回按钮
        backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(this);
        // 当前系统状态，安全|不安全,默认是安全状态
        safeState = (TextView) findViewById(R.id.safe_state);
        iv_safeState = (ImageView) findViewById(R.id.safe_state_image);
        // 扫描动画布局
        ll_scanning = (LinearLayout) findViewById(R.id.ll_scanning);
        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        ad = AnimationUtils.loadAnimation(this, R.animator.scan);

        scanHint = (TextView) findViewById(R.id.scan_hint);

        // 滚动视图
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
        ll_application = (LinearLayout) findViewById(R.id.ll_application);
        // 扫描病毒按钮
        scanButton = (Button) findViewById(R.id.scanning);
        scanButton.setOnClickListener(this);
        // 清理病毒按钮
        killVirus = (Button) findViewById(R.id.kill);
        killVirus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            // 返回上一个Activity
            onBackPressed();
            finish();
        } else if (v == scanButton) {
            // 扫描病毒
            scanVirus();
        } else if (v == killVirus) {
            // 杀死病毒
            for (String packageName : existVirus) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
            killVirus.setVisibility(View.GONE);
            scanButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 扫描病毒
     */
    private void scanVirus() {
        // 隐藏扫描按钮
        scanButton.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.GONE);
        ll_application.setVisibility(View.VISIBLE);
        // TODO 开启动画
        iv_scan.setAnimation(ad);
        ad.start();
        ad.setRepeatCount(Animation.INFINITE);
        ll_scanning.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {
                if (isFirst) {
                    getDbVirus();
                }
                List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_SIGNATURES |
                        PackageManager.GET_UNINSTALLED_PACKAGES);
                for (PackageInfo info : packageInfos) {
                    View view = inflater.inflate(R.layout.virus_scroll_item, null);
                    ImageView appIcon = (ImageView) view.findViewById(R.id.appicon);
                    TextView appName = (TextView) view.findViewById(R.id.appname);

                    String packageName = info.packageName;
                    ApplicationInfo appInfo = info.applicationInfo;
                    Drawable icon = appInfo.loadIcon(pm);
                    String name = appInfo.loadLabel(pm).toString();

                    Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
                    appIcon.setImageBitmap(ImageUtil.zoomImg(bitmap, 50, 50));
                    appName.setText(name);

                    Message msg = new Message();
                    msg.what = SCANNING;
                    msg.obj = view;
                    mHandler.sendMessage(msg);

                    Signature[] signatures = info.signatures;
                    StringBuilder sb = new StringBuilder();
                    for (Signature signature : signatures) {
                        sb.append(signature.toCharsString());
                    }
                    String s = MD5.getMd5String(sb.toString());
                    if (allVirus.contains(s)) {
                        existVirus.add(packageName);
                    }
                }
                Message msg = new Message();
                msg.what = FINISH_SCAN;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCANNING:
                    View view = (View) msg.obj;
                    ll_application.addView(view);
                    scrollView.scrollBy(0, 60);
                    break;
                case FINISH_SCAN:
                    int count = existVirus.size();
                    // TODO 停止动画
                    ad.cancel();
                    ll_scanning.setVisibility(View.GONE);
                    if (count > 0) {
                        scanHint.setText("本次扫描共发现 " + count + " 个病毒");
                        killVirus.setVisibility(View.VISIBLE);
                        safeState.setText("不安全");
                        safeState.setTextColor(Color.RED);
                        iv_safeState.setImageResource(R.drawable.content_top_bg_04_virus_02);
                    } else {
                        scanHint.setText("本次扫描未发现病毒");
                        scanButton.setVisibility(View.VISIBLE);
                        safeState.setText("安全");
                        safeState.setTextColor(getResources().getColor(R.color.safe_state));
                        iv_safeState.setImageResource(R.drawable.content_top_bg_04_virus_01);
                    }
                    break;
            }
        }
    };

    /**
     * 拷贝病毒库
     */
    private void moveVirusDb() {
        if (file.exists())
            return;
        try {
            InputStream is = getAssets().open("antivirus.db");
            OutputStream os = new FileOutputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到病毒库信息
     *
     * @return
     */
    private void getDbVirus() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getAbsolutePath(), null);
        if (db.isOpen()) {
            Cursor c = db.query("datable", new String[]{"md5"}, null, null, null, null, null);
            while (c.moveToNext()) {
                String md5 = c.getString(0);
                allVirus.add(md5);
            }
            c.close();
            db.close();
        }
    }
}