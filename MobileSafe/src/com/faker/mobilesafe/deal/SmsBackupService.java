package com.faker.mobilesafe.deal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import com.faker.mobilesafe.bean.SmsBean;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: LichFaker
 * Date: 14-4-4
 * Time: 下午5:22
 * Email: lichfaker@gmail.com
 */
public class SmsBackupService {

    private final String BACKUP_DIRPATH = Environment.getExternalStorageDirectory()
            .getPath() + "/MobileSafe/backup";
    private final String BACKUP_FILENAME = "smsbak.xml";

    private Context context;

    public SmsBackupService(Context context) {
        this.context = context;
    }

    /**
     * 获得所有短信信息
     */
    public List<SmsBean> findAll() {
        List<SmsBean> smsBeans = new ArrayList<SmsBean>();
        Uri uri = Uri.parse("content://sms");
        Cursor c = context.getContentResolver().query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        while (c.moveToNext()) {
            String address = c.getString(0);
            String date = c.getString(1);
            String type = c.getString(2);
            String body = c.getString(3);
            SmsBean bean = new SmsBean(address, date, type, body);
            smsBeans.add(bean);
        }
        c.close();
        return smsBeans;
    }

    /**
     * 创建短信备份xml文件，并写入信息
     */
    public void createBackupXml(List<SmsBean> smsBeans) throws Exception {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        File dir = new File(BACKUP_DIRPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File backupFile = new File(dir, BACKUP_FILENAME);
        OutputStream os = new FileOutputStream(backupFile);
        xmlSerializer.setOutput(os, "UTF-8");
         /*
         * startDocument(String encoding, Boolean standalone)encoding代表编码方式
         * standalone  用来表示该文件是否呼叫其它外部的文件。
         * 若值是 ”yes” 表示没有呼叫外部规则文件，若值是 ”no” 则表示有呼叫外部规则文件。默认值是 “yes”。
         */
        xmlSerializer.startDocument("UTF-8", true);
        xmlSerializer.startTag(null, "smsinfos");
        for (SmsBean bean : smsBeans) {
            xmlSerializer.startTag(null, "smsinfo");
            // address
            xmlSerializer.startTag(null, "address");
            xmlSerializer.text(bean.getAddress());
            xmlSerializer.endTag(null, "address");
            // date
            xmlSerializer.startTag(null, "date");
            xmlSerializer.text(bean.getDate());
            xmlSerializer.endTag(null, "date");
            // type
            xmlSerializer.startTag(null, "type");
            xmlSerializer.text(bean.getType());
            xmlSerializer.endTag(null, "type");
            // body
            xmlSerializer.startTag(null, "body");
            xmlSerializer.text(bean.getBody());
            xmlSerializer.endTag(null, "body");

            xmlSerializer.endTag(null, "smsinfo");
        }
        xmlSerializer.endTag(null, "smsinfos");
        xmlSerializer.endDocument();
        os.close();
    }

    /**
     * 取得备份的xml中的短信数据
     *
     * @return
     * @throws Exception
     */
    public List<SmsBean> getSmsFromBackupXml() throws Exception {
        List<SmsBean> smsBeans = null;
        SmsBean bean = null;
        File dir = new File(BACKUP_DIRPATH);
        if (dir.exists()) {
            File file = new File(dir, BACKUP_FILENAME);
            InputStream is = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if ("smsinfos".equals(parser.getName())) {
                            smsBeans = new ArrayList<SmsBean>();
                        } else if ("smsinfo".equals(parser.getName())) {
                            bean = new SmsBean();
                        } else if ("address".equals(parser.getName())) {
                            bean.setAddress(parser.nextText());
                        } else if ("date".equals(parser.getName())) {
                            bean.setDate(parser.nextText());
                        } else if ("type".equals(parser.getName())) {
                            bean.setType(parser.nextText());
                        } else if ("body".equals(parser.getName())) {
                            bean.setBody(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("smsinfo".equals(parser.getName())) {
                            smsBeans.add(bean);
                            bean = null;
                        }
                        break;
                    default:
                        break;
                }
                type = parser.next();
            }
        }
        return smsBeans;
    }
}
