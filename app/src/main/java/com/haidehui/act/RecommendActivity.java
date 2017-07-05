package com.haidehui.act;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.haidehui.R;
import com.haidehui.common.Urls;
import com.haidehui.model.ResultRecommendInfoContentBean;
import com.haidehui.model.ResultSentSMSContentBean;
import com.haidehui.net.UserLoadout;
import com.haidehui.network.BaseParams;
import com.haidehui.network.BaseRequester;
import com.haidehui.network.HtmlRequest;
import com.haidehui.widget.TitleBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import onekeyshare.OnekeyShare;
import onekeyshare.PlatformListFakeActivity;

/**
 * 推荐好友
 * Created by hasee on 2017/6/13.
 */
public class RecommendActivity extends BaseActivity implements View.OnClickListener{


    private TextView tv_recommend_all_friend;       //  邀请好友数
    private TextView tv_recommend_all_acount;       //  好友为我赚取金额
    private ImageView iv_recommend_invite_code;     //  我的邀请二维码
    private TextView tv_recommend_mycode;           //  我的推荐码
    private TextView tv_recommend_btn;              //  邀请好友
    private TextView tv_recommend_rule;             //  邀请规则
    private TextView tv_recommend_record;           //  邀请记录

    private final static String CACHE = "/dafuweng/imgs";
    private int QR_WIDTH = 360, QR_HEIGHT = 360;
    private String recommendCode = "";              //  邀请码
    private String way;
    private Context context;

    private ResultRecommendInfoContentBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.ac_recommend);
        initTopTitle();
        initView();
        initData();
        try {
            saveImage(drawableToBitamp(getResources().getDrawable(R.mipmap.img_logo_bg_white)), "dafuweng.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initView(){

        context = this;
        bean = new ResultRecommendInfoContentBean();

        tv_recommend_all_friend = (TextView) findViewById(R.id.tv_recommend_all_friend);
        tv_recommend_all_acount = (TextView) findViewById(R.id.tv_recommend_all_acount);
        iv_recommend_invite_code = (ImageView) findViewById(R.id.iv_recommend_invite_code);
        tv_recommend_mycode = (TextView) findViewById(R.id.tv_recommend_mycode);
        tv_recommend_btn = (TextView) findViewById(R.id.tv_recommend_btn);
        tv_recommend_rule = (TextView) findViewById(R.id.tv_recommend_rule);
        tv_recommend_record = (TextView) findViewById(R.id.tv_recommend_record);

        tv_recommend_btn.setOnClickListener(this);
        tv_recommend_rule.setOnClickListener(this);
        tv_recommend_record.setOnClickListener(this);

    }

    public void initData(){

        getRecommendData();

    }

    public void setView(){

        tv_recommend_all_friend.setText(bean.getTotal()+"位朋友为我赚取了");
        tv_recommend_all_acount.setText("￥"+bean.getTotalAmount()+"元");
        tv_recommend_mycode.setText("我的推荐码："+recommendCode);

        StringBuffer randomNum = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int t = (int) (Math.random() * 10);
            randomNum.append(t);
        }
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.img_logo_recommend_code);
//        createQRImage(iv_myperson_invite_code, ApplicationConsts.URL_DEBUG_M + "vjinke/" + inviteBean.getRecommendCode()
//                + "/scanQRCode/" + randomNum, bitmap);

        createQRImage(iv_recommend_invite_code, Urls.URL + "register/" + recommendCode + "/recommend", bitmap);

    }

    private void getRecommendData() {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();

        param.put("userId", userId);

        HtmlRequest.getRecommendInfo(RecommendActivity.this, param,new BaseRequester.OnRequestListener() {

            @Override
            public void onRequestFinished(BaseParams params) {
                bean = (ResultRecommendInfoContentBean) params.result;
                if (bean != null) {
                    recommendCode = bean.getRecommendCode();

                    setView();

                } else {
                    Toast.makeText(RecommendActivity.this, "加载失败，请确认网络通畅",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null))
                .setLogo(R.drawable.icons, false).setIndicator(R.drawable.back)
                .setCenterText(getResources().getString(R.string.setting_recommend_title))
                .showMore(false).setOnActionListener(new TitleBar.OnActionListener() {

            @Override
            public void onMenu(int id) {
            }

            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onAction(int id) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_recommend_btn:     //  邀请朋友
                ShareSDK.initSDK(this);
                sharedSDK();
                break;

            case R.id.tv_recommend_rule:        //  邀请规则

                Intent i_rule = new Intent(this,RecommendRuleActivity.class);
                startActivity(i_rule);

                break;

            case R.id.tv_recommend_record:      //  邀请记录

                Intent i_record = new Intent(this,RecommendRecordActivity.class);
                i_record.putExtra("recommendCode",recommendCode);
                startActivity(i_record);

                break;

            default:

                break;


        }

    }

    private void sharedSDK() {

        final OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setOnShareButtonClickListener(new PlatformListFakeActivity.OnShareButtonClickListener() {

            @Override
            public void onClick(View v, List<Object> checkPlatforms) {
                String string = checkPlatforms.toString();
                oks.setSilent(false);
                StringBuffer randomNum = new StringBuffer();
                for (int i = 0; i < 6; i++) {
                    int t = (int) (Math.random() * 10);
                    randomNum.append(t);
                }
                StringBuffer randomNum2 = new StringBuffer();
                for (int i = 0; i < 6; i++) {
                    int t = (int) (Math.random() * 10);
                    randomNum2.append(t);
                }
                String url = Urls.URL + "register/" + recommendCode + "/recommend";
                if (string.contains("WechatMoments")) {
                    way = "weixin";            //微信朋友圈
                    oks.setText(getString(R.string.shared_message) + url);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                    oks.setTitle(getString(R.string.login_title));
                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/dafuweng/imgs/dafuweng.png");
                } else if (string.contains("Wechat")) {
                    way = "weixinFr";        //微信好友
                    oks.setText(getString(R.string.shared_message) + url);
                    oks.setTitle(getString(R.string.login_title));
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
//					oks.setImagePath("/sdcard/vjinke/imgs/test.jpg");
                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/dafuweng/imgs/dafuweng.png");
                } else if (string.contains("QZone")) {
                    way = "Qzone";
                    oks.setText(getString(R.string.shared_message) + url);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                } else if (string.contains("SinaWeibo")) {
                    way = "sinablog";
                    oks.setText(getString(R.string.shared_message) + url);
//					oks.setTitleUrl(url);
                    oks.setUrl(url);

                    oks.setSilent(false);
                } else if (string.contains("TencentWeibo")) {
                    way = "tencentblog";
                    oks.setText(getString(R.string.shared_message) + url);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                } else if (string.contains("QQ")) {
                    way = "QQ";
                    oks.setText(getString(R.string.shared_message) + url);
//					oks.setTitleUrl(url);
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                    // url仅在微信（包括好友和朋友圈）中使用
                    oks.setUrl(url);
                    // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                    // oks.setComment("我是测试评论文本");
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite(getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl(url);
                } else if (string.contains("Email")) {
                    way = "email";
                    oks.setText(getString(R.string.shared_message) + url);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                } else if (string.contains("ShortMessage")) {
                    way = "sms";
                    oks.setText(getString(R.string.shared_message) + url);
                    oks.setTitleUrl(url);
                    oks.setUrl(url);
                }
            }

        });

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

        oks.setTitle(getString(R.string.share));
//		oks.setImagePath("/sdcard/vjinke/imgs/test.jpg");//确保SDcard下面存在此张图片
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用

        // oks.setTitleUrl("http://www.vjinke.com");

        // text是分享文本，所有平台都需要这个字段
        Bitmap enableLogo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_lianjie);
        String label = "复制链接";
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                StringBuffer randomNum = new StringBuffer();
                for (int i = 0; i < 6; i++) {
                    int t = (int) (Math.random() * 10);
                    randomNum.append(t);
                }
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(Urls.URL + "register/" + recommendCode + "/recommend");
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
            }
        };
        oks.setCustomerLogo(enableLogo, enableLogo, label, listener);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toast.makeText(context,"--------"+platform.getName(),Toast.LENGTH_SHORT).show();

                if(platform.getName().equals("WechatMoments")){


                }else if(platform.getName().equals("Wechat")){


                }else if(platform.getName().equals("QZone")){


                }else if(platform.getName().equals("SinaWeibo")){


                }else if(platform.getName().equals("ShortMessage")){


                }else if(platform.getName().equals("QQ")){

                }


            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });


//        oks.getCallback();

        if (!TextUtils.isEmpty(recommendCode)) {
            // 启动分享GUI
            oks.show(this);
        }


    }


    // 要转换的地址或字符串,可以是中文
    public void createQRImage(ImageView img, String url, Bitmap logoBm) {
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
//            bitMatrix = deleteWhite(bitMatrix);//删除白边
            bitMatrix = updateBit(bitMatrix, 10);  //生成新的bitMatrix

            QR_WIDTH = bitMatrix.getWidth();
            QR_HEIGHT = bitMatrix.getHeight();

            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            // 显示到一个ImageView上面
            img.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    private BitMatrix updateBit(BitMatrix matrix, int margin){

        int tempM = margin*2;

        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性

        int resWidth = rec[2] + tempM;

        int resHeight = rec[3] + tempM;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix

        resMatrix.clear();

        for(int i= margin; i < resWidth- margin; i++){   //循环，将二维码图案绘制到新的bitMatrix中

            for(int j=margin; j < resHeight-margin; j++){

                if(matrix.get(i-margin + rec[0], j-margin + rec[1])){

                    resMatrix.set(i,j);

                }

            }

        }

        return resMatrix;

    }

    /**

     * 图片放大缩小

     */

//    public static BufferedImage  zoomInImage(BufferedImage  originalImage, int width, int height){
//
//        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
//
//        Graphics g = newImage.getGraphics();
//
//        g.drawImage(originalImage, 0,0,width,height,null);
//
//        g.dispose();
//
//        return newImage;
//
//    }



    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 7 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }



    /**
     * 保存图片的方法 保存到sdcard
     *
     * @throws Exception
     */
    public static void saveImage(Bitmap bitmap, String imageName)
            throws Exception {
        String filePath = isExistsFilePath();
        FileOutputStream fos = null;
        File file = new File(filePath, imageName);
        try {
            fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取sd卡的缓存路径， 一般在卡中sdCard就是这个目录
     *
     * @return SDPath
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
        } else {
            Log.e("ERROR", "没有内存卡");
        }
        return sdDir.toString();
    }

    /**
     * 获取缓存文件夹目录 如果不存在创建 否则则创建文件夹
     *
     * @return filePath
     */
    private static String isExistsFilePath() {
        String filePath = getSDPath() + CACHE;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

}
