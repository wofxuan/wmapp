package com.mx.android.wmapp.utils;

import android.content.Context;
import android.util.Log;

public class DensityUtil {

	/**
	 * 根据手机的分辨率�?dip 的单�?转成�?px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		Log.i("tag", "dip2px  "+(int) (dpValue * scale + 0.5f));
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�?px(像素) 的单�?转成�?dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}