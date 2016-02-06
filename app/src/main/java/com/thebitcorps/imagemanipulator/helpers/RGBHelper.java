package com.thebitcorps.imagemanipulator.helpers;

import android.graphics.Color;

/**
 * Created by diegollams on 2/6/16.
 */
public class RGBHelper {

	public static int getGrayScaleColor(int color){
		int gray = (RGBHelper.getRed(color) + RGBHelper.getGreen(color) + RGBHelper.getBlue(color)) /3;
		return (gray << 24) | (gray << 16) | (gray << 8) | gray;

	}

	public static int getRed(int color){
		return color >> 16 & 0xFF;
	}
	public static int getBlue(int color){
		return color >> 0 & 0xFF;
	}

	public static int getGreen(int color){
		return color >> 8 & 0xFF;
	}
	public static int getAlpha(int color){
		return color >>> 24;
	}

}
