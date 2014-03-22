package com.faker.mobilesafe.util;

import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import com.faker.mobilesafe.bean.UpdateBean;
import android.util.Xml;

public class XmlParseUtil {

	public static void setUpdateInfo(InputStream inputStream) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF-8");
		int type = parser.getEventType();
		UpdateBean bean = UpdateBean.getInstance();
		while(type != XmlPullParser.END_DOCUMENT){
			if(type == XmlPullParser.START_TAG){
				String str = parser.getName();
				if("version".equals(str)){
					bean.setVersion(parser.nextText());
				}else if("descripte".equals(str)){
					bean.setDescripte(parser.nextText());
				}else if("apkurl".equals(str)){
					bean.setApkurl(parser.nextText());
				}
			}
			type = parser.next();
		}
	}

}
