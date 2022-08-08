package com.diotek.diodict.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.RuleBasedCollator;

/* loaded from: classes.dex */
public class ICUCollator {
    private static ICUCollator collatorInstance = null;
    private static RuleBasedCollator mRuledCollator = null;
    private static String ICU_ORDER_FILE_NAME = "_PinStrOrder.dat";

    public static ICUCollator getInstance() {
        ICUCollator iCUCollator;
        synchronized (ICUCollator.class) {
            if (collatorInstance == null) {
                collatorInstance = new ICUCollator();
            }
            iCUCollator = collatorInstance;
        }
        return iCUCollator;
    }

    public void initialize() {
        if (mRuledCollator == null) {
            try {
                mRuledCollator = new RuleBasedCollator(readRule());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int compareTo(String a, String b) {
        if (mRuledCollator == null) {
            return a.compareTo(b);
        }
        StringKey key_a = new StringKey(a, mRuledCollator.getCollationKey(a));
        StringKey key_b = new StringKey(b, mRuledCollator.getCollationKey(b));
        return key_a.compareTo(key_b);
    }

    private String readRule() {
        StringBuilder rules = new StringBuilder();
        try {
            if (isUsableICU()) {
                BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(DictUtils.getDBPath() + ICU_ORDER_FILE_NAME), Charset.forName("UTF-8")));
                while (true) {
                    String temp = buf.readLine();
                    if (temp == null) {
                        break;
                    }
                    rules.append(temp);
                }
                buf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rules.toString();
    }

    public static boolean isUsableICU() {
        File table = null;
        try {
            String path = DictUtils.getDBPath() + ICU_ORDER_FILE_NAME;
            table = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (table.exists()) {
            return true;
        }
        return false;
    }
}
