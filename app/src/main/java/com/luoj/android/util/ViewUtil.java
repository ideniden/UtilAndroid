package com.luoj.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Field;


/**
 * @author LuoJ
 * @date 2014-3-5
 * @package j.android.library.utils -- ViewUtil.java
 * @Description 组件工具类
 */
public class ViewUtil {

	public static void setText(View rootView,int textViewId,CharSequence text){
		View tv = rootView.findViewById(textViewId);
		if (null!=tv) {
			((TextView)tv).setText(text);
		}else{
			
		}
	}

	public static void setText(View rootView,int textViewId,int textResId){
		View tv = rootView.findViewById(textViewId);
		if (null!=tv) {
			((TextView)tv).setText(rootView.getContext().getString(textResId));
		}else{

		}
	}

	public static void setText(Activity activity,int textViewId,CharSequence text){
		View tv = activity.findViewById(textViewId);
		if (null!=tv) {
			((TextView)tv).setText(text);
		}else{
			
		}
	}
	
	public static void setText(Activity activity,int textViewId,int textResId){
		View tv = activity.findViewById(textViewId);
		if (null!=tv) {
			((TextView)tv).setText(activity.getString(textResId));
		}else{
			
		}
	}
	
	public static void append(View rootView,int textViewId,CharSequence text){
		View tv = rootView.findViewById(textViewId);
		if (null!=tv) {
			((TextView)tv).append(text);
		}else{
			
		}
	}
	
	public static void setOnClickListener(Activity activity,int viewId,OnClickListener listener){
		View v = activity.findViewById(viewId);
		if (null!=v) {
			v.setOnClickListener(listener);
		}else{
			
		}
	}
	
	public static void setOnClickListener(View rootView,int viewId,OnClickListener listener){
		View v = rootView.findViewById(viewId);
		if (null!=v) {
			v.setOnClickListener(listener);
		}else{
			
		}
	}
	public static void setOnLongClickListener(View rootView,int viewId,OnLongClickListener listener){
		View v = rootView.findViewById(viewId);
		if (null!=v) {
			v.setOnLongClickListener(listener);
		}else{
			
		}
	}
	
	public static void setOnCheckChangeListener(Activity activity,int viewId,OnCheckedChangeListener listener){
		CompoundButton v = (CompoundButton) activity.findViewById(viewId);
		if (null!=v) {
			v.setOnCheckedChangeListener(listener);
		}else{
			
		}
	}
	
	public static void setOnCheckChangeListener(View rootView,int viewId,OnCheckedChangeListener listener){
		CompoundButton v = (CompoundButton) rootView.findViewById(viewId);
		if (null!=v) {
			v.setOnCheckedChangeListener(listener);
		}else{
			
		}
	}

	public static void setOnTouchListener(View rootVIew,int viewId,View.OnTouchListener listener){
		View v = rootVIew.findViewById(viewId);
		if (null!=v) {
			v.setOnTouchListener(listener);
		}else{

		}
	}

	public static void setOnTouchListener(Activity activity,int viewId,View.OnTouchListener listener){
		View v = activity.findViewById(viewId);
		if (null!=v) {
			v.setOnTouchListener(listener);
		}else{

		}
	}

	public static void autoScrollBottom(TextView textView, final ScrollView scrollView) {
		autoScroll(textView, scrollView, ScrollView.FOCUS_DOWN);
	}

	public static void autoScroll(TextView textView, final ScrollView scrollView, final int direction) {
		textView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				scrollView.fullScroll(direction);
			}
		});
	}

	/**
	 * 设置dialog点击确定取消后是否关闭
	 * 
	 * @param dialog
	 * @param isShow
	 * @return
	 */
	public static boolean setDialogCanCancel(DialogInterface dialog, boolean isShow) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, isShow);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void enable(View view) {
		setViewEnable(view, true);
	}

	public static void disable(View view) {
		setViewEnable(view, false);
	}

	public static void setViewEnable(View view, boolean enable) {
		if (null != view) {
			view.setFocusable(enable);
			view.setEnabled(enable);
			view.setClickable(enable);
		}
	}

	public static View visiable(Activity activity,int viewId){
		View view = activity.findViewById(viewId);
		view.setVisibility(View.VISIBLE);
		return view;
	}
	public static View visiableWithAlpha(Activity activity,int viewId){
		View view = visiable(activity, viewId);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(400);
		view.startAnimation(alphaAnimation);
		return view;
	}
	public static void visiable(View rootView,int viewId){
		rootView.findViewById(viewId).setVisibility(View.VISIBLE);
	}
	public static void invisiable(Activity activity,int viewId){
		activity.findViewById(viewId).setVisibility(View.INVISIBLE);
	}
	public static void invisiable(View rootView,int viewId){
		rootView.findViewById(viewId).setVisibility(View.INVISIBLE);
	}
	public static void gone(Activity activity,int viewId){
		activity.findViewById(viewId).setVisibility(View.GONE);
	}
	public static void gone(View rootView,int viewId){
		rootView.findViewById(viewId).setVisibility(View.GONE);
	}

	public static void saveHistory(String keyName, AutoCompleteTextView autoCompleteTextView) {
		saveHistory(PreferenceManager.getDefaultSharedPreferences(autoCompleteTextView.getContext()), keyName, autoCompleteTextView);
	}
	
	/**
	 * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
	 * @param sp
	 * @param keyName
	 * @param autoCompleteTextView 要操作的AutoCompleteTextView
	 */
	public static void saveHistory(SharedPreferences sp, String keyName, AutoCompleteTextView autoCompleteTextView) {
		if(null==autoCompleteTextView.getText())return;
		String text = autoCompleteTextView.getText().toString();
		if(TextUtils.isEmpty(text))return;
		String longhistory = sp.getString(keyName, "nothing");
		if (!longhistory.contains(text + ",")) {
			StringBuilder sb = new StringBuilder(longhistory);
			sb.insert(0, text + ",");
			sp.edit().putString(keyName, sb.toString()).apply();
		}
	}

	public static String[] getHistory(Context ctx,String keyName){
		return getHistory(PreferenceManager.getDefaultSharedPreferences(ctx),keyName);
	}

	public static String[] getHistory(SharedPreferences sp,String keyName){
		String longhistory = sp.getString(keyName, "nothing");
		if("nothing".equals(longhistory)){
			return null;
		}
		return longhistory.split(",");
	}

	public static void setAutoComplete(Context context,String keyName,AutoCompleteTextView autoCompleteTextView, int reserveNum) {
		setAutoComplete(context, PreferenceManager.getDefaultSharedPreferences(autoCompleteTextView.getContext()), keyName, autoCompleteTextView, reserveNum);
	}

	/**
	 * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
	 * @param context
	 * @param sp
	 * @param keyName
	 * @param autoCompleteTextView 要操作的AutoCompleteTextView
	 * @param reserveNum
	 */
	public static void setAutoComplete(Context context, SharedPreferences sp, String keyName, final AutoCompleteTextView autoCompleteTextView, int reserveNum) {
		String longhistory = sp.getString(keyName, "nothing");
		String[] histories = longhistory.split(",");
		ArrayAdapter<String> adapter = null;
		if (histories.length > reserveNum) {
			String[] newHistories = new String[reserveNum];
			System.arraycopy(histories, 0, newHistories, 0, reserveNum);
			adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, newHistories);
		} else {
			adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, histories);
		}
		autoCompleteTextView.setAdapter(adapter);
		autoCompleteTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String s = autoCompleteTextView.getText().toString();
				if (hasFocus && !TextUtils.isEmpty(s)) {
					AutoCompleteTextView view = (AutoCompleteTextView) v;
					view.showDropDown();
				}
			}
		});
	}

	public static View getRootView(Activity context){  
        return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
    }
	
	public static TextView addFooterTextView(Context context,ListView lv){
		TextView v=new TextView(context);
		v.setLayoutParams(new AdapterView.LayoutParams(-2,-2));
		int padding=DensityUtil.dip2px(context, 18);
		v.setPadding(padding, padding, padding, padding);
		lv.addFooterView(v, null, false);
		return v;
	}

    public static void setExpandableListViewGroupCantClickable(ExpandableListView elv){
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				return true;
			}
		});
    }

	/**
	 * 动态设置ListView的高度.
	 * 仅针对itemView为LinearLayout
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if(listView == null) return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 动态设置ListView的高度.
	 * 仅针对itemView为LinearLayout
	 * 设置listview高度为item高度*b
	 * @param listView
	 * @param b
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView,int b) {
		if(listView == null) return;
		ListAdapter listAdapter = listView.getAdapter();
		int count=listAdapter.getCount();
		if (listAdapter == null||count==0) {
			// pre-condition
			return;
		}
		if(count<b){
			setListViewHeightBasedOnChildren(listView);
			return;
		}
		int totalHeight = 0;
		View listItem = listAdapter.getView(0, null, listView);
		listItem.measure(0, 0);
		totalHeight = listItem.getMeasuredHeight()*b;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));//加上分割线
		listView.setLayoutParams(params);
	}

	public static View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	public static void appendTextSpan(EditText et,BitmapSpan bitmapSpan){
		Editable e = et.getText();
		int selectionStart = et.getSelectionStart();
		SpannableString ss=new SpannableString(bitmapSpan.string);
		ImageSpan is=new ImageSpan(et.getContext(), bitmapSpan.bitmap);
		ss.setSpan(is, 0, bitmapSpan.string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		e.insert(selectionStart, ss);
		et.setText(e);
		et.setSelection(selectionStart+ss.length());
	}

	public static BitmapSpan createTextSpan(int icon,String text,float textSize,int textColor,int bgColor){
//		String testString="test";
		int rectPadding=10;

		int rectRedius=10;
		int padding=10;

		Paint textP=new Paint();
		textP.setAntiAlias(true);
		textP.setStyle(Paint.Style.FILL);
		textP.setColor(textColor);
		textP.setTextSize(textSize);

		float stringWidth = textP.measureText(text);
		Paint.FontMetrics fm = textP.getFontMetrics();
		float stringHeight = (int) Math.ceil(fm.descent - fm.ascent);

		Paint circleP = new Paint();
		circleP.setAntiAlias(true);
		circleP.setStyle(Paint.Style.FILL);
		circleP.setColor(bgColor);

		int rectWidth=(int)stringWidth+(rectPadding*2);
		int rectHeith=(int)stringHeight+(rectPadding*2);
		int bitmapWidth=rectWidth+(padding*2);
		int bitmapHeight=rectHeith+(padding*2);
		Bitmap bitmap=Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
		Canvas c=new Canvas(bitmap);
//		c.drawColor(Color.BLACK);
		c.drawRoundRect(new RectF(padding, padding, rectWidth+padding, rectHeith+padding), rectRedius, rectRedius, circleP);
		int mXCenter = c.getWidth() / 2;
		int mYCenter = c.getHeight() / 2;
		c.drawText(text, mXCenter - stringWidth / 2, mYCenter + stringHeight / 4, textP);
		return new BitmapSpan(bitmap, text);
	}

	public static class BitmapSpan {
		public Bitmap bitmap;
		public String string;
		public BitmapSpan(Bitmap bitmap, String string) {
			super();
			this.bitmap = bitmap;
			this.string = string;
		}
	}

}
