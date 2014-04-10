package com.faker.mobilesafe.util;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class FormatUtil {

    // 23.87MB 00.87MB/KB byte
    public static String formatByte(long data) {
        DecimalFormat format = new DecimalFormat("##.##");
        if (data < 1024) {
            return data + " bytes";
        } else if (data < 1024 * 1024) {
            return format.format(data / 1024f) + " KB";
        } else if (data < 1024 * 1024 * 1024) {
            return format.format(data / 1024f / 1024f) + " MB";
        } else if (data < 1024 * 1024 * 1024 * 1024) {
            return format.format(data / 1024f / 1024f / 1024f) + " GB";
        } else {
            return "无法计算";
        }
    }

    public static String formatTraffic(long data) {
        DecimalFormat format = new DecimalFormat("##.#");
        if (data < 1024 * 1024) {
            return format.format(data / 1024f) + " KB";
        } else if (data < 1024 * 1024 * 1024) {
            return format.format(data / 1024f / 1024f) + " M";
        } else if (data < 1024 * 1024 * 1024 * 1024) {
            return format.format(data / 1024f / 1024f / 1024f) + " G";
        } else {
            return "无法计算";
        }
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return [yy/MM/dd HH:MM]
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");
        Date date = new Date(time);
        return "[" + formatter.format(date) + "]";
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return yy/MM/dd
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(time);
        return formatter.format(date);
    }
}
