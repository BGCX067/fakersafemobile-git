package com.faker.mobilesafe.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {

	public static Bitmap zoomImg(Bitmap paramBitmap, int paramInt1, int paramInt2)
	  {
	    int i = paramBitmap.getWidth();
	    int j = paramBitmap.getHeight();
	    Matrix localMatrix = new Matrix();
	    float f1 = paramInt1;
	    float f2 = i;
	    float f3 = f1 / f2;
	    float f4 = paramInt2;
	    float f5 = j;
	    float f6 = f4 / f5;
	    localMatrix.postScale(f3, f6);
	    Bitmap localBitmap = paramBitmap;
	    int k = 0;
	    return Bitmap.createBitmap(localBitmap, 0, k, i, j, localMatrix, true);
	}
}
