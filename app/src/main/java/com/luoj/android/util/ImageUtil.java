package com.luoj.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @author LuoJ
 * @date 2013-10-10
 * @package j.android.library.utils -- ImageUtil.java
 * @Description 图片处理工具类
 */
public class ImageUtil {

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d("readPictureDegree failed");
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

//    public static boolean displayByVertical(ImageView iv, Bitmap bitmap) {
//            int degree = ImageUtil.readPictureDegree(imgPath);
//            LogUtil.d("degree->" + degree);
//            Bitmap bitmap = null;
//            try {
//                bitmap = BitmapFactory.decodeStream(new FileInputStream(imgPath));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            if (null != bitmap) {
//                if (degree == 0) {
//                    iv.setImageBitmap(bitmap);
//                } else {
//                    iv.setImageBitmap(ImageUtil.rotateBitmap(bitmap, degree));
//                }
//                return true;
//            }
//        return false;
//    }

    public static boolean displayByVertical(ImageView iv, String imgPath) {
        if (!TextUtils.isEmpty(imgPath)) {
            int degree = ImageUtil.readPictureDegree(imgPath);
            LogUtil.d("degree->" + degree);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(imgPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (null != bitmap) {
                if (degree == 0) {
                    iv.setImageBitmap(bitmap);
                } else {
                    iv.setImageBitmap(ImageUtil.rotateBitmap(bitmap, degree));
                }
                return true;
            }
        }
        return false;
    }

    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return BitmapFactory.decodeFile(imgpath);
        } else {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    /**
     * 将指定路径的图片压缩
     *
     * @param path
     * @return
     */
    public static Drawable fitSizeImg(String path) {
        File file = new File(path);
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        // opts.inSampleSize = 5;
        // 数字越大读出的图片占用的heap越小 不然总是溢出
        if (file.length() < 20480) { // 0-20k
            opts.inSampleSize = 1;
        } else if (file.length() < 51200) { // 20-50k
            opts.inSampleSize = 1;
        } else if (file.length() < 204800) {
            opts.inSampleSize = 1;
        } else if (file.length() < 307200) { // 50-300k
            opts.inSampleSize = 2;
        } else if (file.length() < 509200) {
            opts.inSampleSize = 4;
        } else if (file.length() < 819200) { // 300-800k
            opts.inSampleSize = 6;
        } else if (file.length() < 1048576) { // 800-1024k
            opts.inSampleSize = 8;
        } else {
            opts.inSampleSize = 10;
        }
        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
        @SuppressWarnings("deprecation")
        Drawable drawable = new BitmapDrawable(resizeBmp);
        return drawable;
    }

    /**
     * bitmap to string
     *
     * @param bitmap
     * @return
     */
    public static String bitmaptoString(Bitmap bitmap) {
        String string = null;
        if (null != bitmap) {
            // 将Bitmap转换成字符串
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 90, bStream);
            byte[] bytes = bStream.toByteArray();
            string = Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return string;
    }

    /**
     * 简易截屏方法
     *
     * @param v        视图
     * @param filePath 保存路径
     */
    public static void getScreenHot(Context mctx, View v, String filePath) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            v.draw(canvas);

            try {
                FileOutputStream fos = new FileOutputStream(filePath);
                bitmap.compress(CompressFormat.PNG, 90, fos);
            } catch (FileNotFoundException e) {
                Log.e("errormsg", e.toString());
//				ToastUtil.showToast(mctx, "找不到路径");
                // ToastUtil.showToast(mctx, "找不到路径", 200);
            }
        } catch (Exception e) {
            Log.e("errormsg", e.toString());
//			ToastUtil.showToast(mctx, "截图失败");
            // ToastUtil.showToast(mctx, "截图失败", 200);
        }
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    public static void saveCurrentImage(Context mctx) {
        Activity mActivity = (Activity) mctx;
        // 创建Bitmap
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        @SuppressWarnings("deprecation")
        int w = display.getWidth();
        @SuppressWarnings("deprecation")
        int h = display.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

        // 获取屏幕
        View decorview = mActivity.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        bmp = decorview.getDrawingCache();

        String SavePath = getSDCardPath() + "/ScreenImage";

        // 存储为Bitmap
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);

            File path = new File(SavePath);
            // 文件
            String filepath = SavePath + "/" + sdf.format(new Date()) + ".png";
            File file = new File(filepath);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                bmp.compress(CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                LogUtil.d("截屏文件已保存至SDCard/ScreenImage/下");
                Toast.makeText(mctx, "截屏文件已保存至SDCard/ScreenImage/下", Toast.LENGTH_LONG).show();
                // ToastUtil.showToast(mctx, "截屏文件已保存至SDCard/ScreenImage/下",
                // Toast.LENGTH_LONG);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SDCard的目录
     *
     * @return
     */
    private static String getSDCardPath() {
        File sdcardDir = null;
        // 判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    /**
     * 生成圆角图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            final float roundPx = 14;
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            LogUtil.e("创建圆角图片失败，返回原图");
            return bitmap;
        }
    }

    /**
     * @param x              图像的宽度
     * @param y              图像的高度
     * @param image          源图片
     * @param outerRadiusRat 圆角的大小
     * @return 圆角图片
     */
    public static Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
        // 根据源文件新建一个darwable对象
        @SuppressWarnings("deprecation")
        Drawable imageDrawable = new BitmapDrawable(image);

        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);

        // 产生一个红色的圆角矩形
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

        // 将源图片绘制到这个圆角矩形上
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();

        return output;
    }

    public static Bitmap createFramedPhoto(Bitmap image, float outerRadiusRat) {
        return createFramedPhoto(image.getWidth(), image.getHeight(), image, outerRadiusRat);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = radius;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    //获得带倒影的图片方法
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,
                0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap,
                deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in  
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient  
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

//    public static Bitmap createQRImage(String url,int qrWidth,int qrheight) {
//        try {
//            if (TextUtils.isEmpty(url)) {
//                return null;
//            }
//            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            //图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, qrWidth, qrheight, hints);
//            int[] pixels = new int[qrWidth * qrheight];
//            //下面这里按照二维码的算法，逐个生成二维码的图片，
//            //两个for循环是图片横列扫描的结果
//            for (int y = 0; y < qrheight; y++) {
//                for (int x = 0; x < qrWidth; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * qrWidth + x] = 0xff000000;
//                    } else {
//                        pixels[y * qrWidth + x] = 0xffffffff;
//                    }
//                }
//            }
//            //生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(qrWidth, qrheight, Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrheight);
//            return bitmap;
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
