package com.diotek.diodict.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* loaded from: classes.dex */
public class DioSerial {
    private static final String PREF_KEY_AUTHORIZE_CODE = "DioDictProductCode";
    private static final String PREF_KEY_DB_PATH = "DioDictDBPath";

    private static int crc16(char[] buf, int len) {
        int[] crc16tab = {0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935, 4657, 528, 12915, 8786, 21173, 17044, 29431, 25302, 37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334, 9314, 13379, 1056, 5121, 25830, 29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669, 13907, 9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390, 55197, 51132, 18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789, 59790, 63919, 35144, 39273, 43274, 47403, 23285, 19156, 31415, 27286, 6769, 2640, 14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802, 27814, 31879, 19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395, 36200, 40265, 32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374, 57309, 53244, 48923, 44858, 40793, 36728, 37256, 33193, 45514, 41451, 53516, 49453, 61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679, 33721, 37784, 41979, 46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270, 46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798, 25671, 21540, 17413, 42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939, 14066, 1681, 5808, 26199, 30326, 17941, 22068, 55628, 51565, 63758, 59695, 39368, 35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403, 52093, 56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801, 6864, 10931, 14994, 64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782, 27655, 23652, 19525, 15522, 11395, 7392, 3265, 61215, 65342, 53085, 57212, 44955, 49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920};
        int crc = 0;
        for (int counter = 0; counter < len; counter++) {
            crc = (crc << 8) ^ crc16tab[((crc >> 8) ^ buf[counter]) & 255];
        }
        return crc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int getWeightedTotalSum(char[] passWord, int nPasslen) {
        int nSum = 0;
        for (int iCnt = 0; iCnt < nPasslen; iCnt++) {
            nSum += passWord[iCnt] * (iCnt + 1);
        }
        return nSum;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int getTotalSum(char[] passWord, int nPasslen) {
        int nSum = 0;
        for (int iCnt = 0; iCnt < nPasslen; iCnt++) {
            nSum += passWord[iCnt];
        }
        return nSum;
    }

    public static String getEigenValue(String DeviceId) {
        char[] tempId = new char[16];
        for (int i = 0; i < tempId.length; i++) {
            tempId[i] = 0;
        }
        for (int i2 = 0; i2 < DeviceId.length(); i2++) {
            tempId[i2] = DeviceId.charAt(i2);
        }
        int FirstValue = 0;
        int SecondValue = 0;
        int ThirdValue = 0;
        int FourthValue = 0;
        for (int j = 0; j < tempId.length; j++) {
            if (j % 4 == 0) {
                FirstValue += tempId[j];
            } else if (j % 4 == 1) {
                SecondValue += tempId[j];
            } else if (j % 4 == 2) {
                ThirdValue += tempId[j];
            } else {
                FourthValue += tempId[j];
            }
        }
        int nCrcNum = crc16(tempId, 16);
        String id = String.valueOf((char) ((((nCrcNum & 15) + FirstValue) % 26) + 65)).toUpperCase() + String.valueOf((char) (((((nCrcNum >> 4) & 15) + SecondValue) % 26) + 65)).toUpperCase() + String.valueOf((char) (((((nCrcNum >> 8) & 15) + ThirdValue) % 26) + 65)).toUpperCase() + String.valueOf((char) (((((nCrcNum >> 12) & 15) + FourthValue) % 26) + 65)).toUpperCase();
        return id;
    }

    public static void saveAuthorizeCode(Context context, String code) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_KEY_AUTHORIZE_CODE, code);
        editor.commit();
    }

    public static String getAuthorizeCode(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_KEY_AUTHORIZE_CODE, null);
    }

    public static void removeRegistration(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_KEY_AUTHORIZE_CODE, null);
        editor.commit();
    }

    public static void saveDBPathtoPreference(Context context, String szPath) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_KEY_DB_PATH, szPath);
        editor.commit();
    }

    public static String getDBPathfromPreference(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PREF_KEY_DB_PATH, null);
    }

    public static void removeDBPathfromPreference(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_KEY_DB_PATH, null);
        editor.commit();
    }

    public static void saveDBSizetoPreference(Context context, String szDBName, int nDBSize) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(szDBName, nDBSize);
        editor.commit();
    }

    public static int getDBSizefromPreference(Context context, String szDBName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(szDBName, -1);
    }

    public static void removeDBSizefromPreference(Context context, String szDBName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(szDBName, null);
        editor.commit();
    }
}
