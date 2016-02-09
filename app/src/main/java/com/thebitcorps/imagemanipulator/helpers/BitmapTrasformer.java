package com.thebitcorps.imagemanipulator.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

/**
 * Created by diegollams on 2/6/16.
 */
public class BitmapTrasformer {

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
														 int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);


		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}


	public static Bitmap getBitmapFromUri(Uri uri,int reqWidth, int reqHeight){
		File file = new File(uri.getPath());
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), getOptions(reqWidth, reqHeight));

		return bitmap;
	}

	@NonNull
	private static BitmapFactory.Options getOptions(int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
		options.inMutable  = true;
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return options;
	}

	public static Bitmap decodeSampledBitmapFromData(byte[] data
														 ,int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inMutable = true;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	public static void grayScaleBitmap(Bitmap bitmap){
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				int pixel = bitmap.getPixel(x,y);
				bitmap.setPixel(x, y,RGBHelper.getGrayScaleColor(pixel));
			}
		}
	}

	public static void inverseMaxCanalImageBitmap(Bitmap bitmap){
		int maxRed = 0,maxGreen = 0,maxBlue = 0;
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				RGBHelper pixel = new RGBHelper(bitmap.getPixel(x,y));
				if(pixel.getRed() > maxRed) maxRed = pixel.getRed();
				if(pixel.getGreen() > maxGreen) maxGreen= pixel.getGreen();
				if(pixel.getBlue() > maxBlue) maxBlue = pixel.getBlue();
			}
		}
		inverseMethod(bitmap, maxRed, maxGreen, maxBlue);
	}

	private static void inverseMethod(Bitmap bitmap, int maxRed, int maxGreen, int maxBlue) {
		for (int x = 0; x < bitmap.getWidth(); x++) {
			for (int y = 0; y < bitmap.getHeight(); y++) {
				RGBHelper pixel = new RGBHelper(bitmap.getPixel(x,y));
				int newPixel = RGBHelper.createPixel(maxRed - pixel.getRed(), maxGreen - pixel.getGreen(), maxBlue - pixel.getBlue(), pixel.getAlpha());
				bitmap.setPixel(x,y,newPixel);
			}
		}
	}

	public static void inverseNormal(Bitmap bitmap){
		inverseMethod(bitmap,255,255,255);
	}
}
