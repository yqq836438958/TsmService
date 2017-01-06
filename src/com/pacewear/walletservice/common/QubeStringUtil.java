/**   
* @Title: QubeStringUtil.java
* @Package com.tencent.qube.utils 
* @author interzhang   
* @date 2012-5-16 下午01:18:02 
* @version V1.0   
*/

package com.pacewear.walletservice.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.tencent.qlauncher.LauncherApp;
//import com.tencent.qlauncher.R;
//import com.tencent.qube.QubeConstant;
import qrom.component.log.QRomLog;

public final class QubeStringUtil {

    // 用来将字节转换成 16 进制表示的字符
    private static char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static final String TAG = "QubeStringUtil";

    public static final float K = 1024;
    public static final float M = 1024 * 1024;
    public static final float G = 1024 * 1024 * 1024;
    private static final String SIZE_BYTE = "%dB";
    private static final String SIZE_KB = "%.2fK";
    private static final String SIZE_MB = "%.2fM";
    private static final String SIZE_GB = "%.2fG";

    private static final String SPEED_BYTE = "%dB/S";
    private static final String SPEED_KB = "%.2fK/S";
    private static final String SPEED_MB = "%.2fM/S";

    private QubeStringUtil() {
    }

    /**
     * 将字节数组转换成一个16进制字符串
     * 
     * @param bytes
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        int j = bytes.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 对String进行MD5加密
     * 
     * @param string
     * @return
     */
    public static String getMD5(String string) {
        String s = null;
        try {
            byte[] source = string.getBytes();
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            return QubeStringUtil.toHexString(md.digest());
        } catch (Exception e) {
            QRomLog.e(TAG, e.getMessage());
        }
        return s;
    }

    /**
     * 对byte[]进行MD5加密
     * 
     * @return
     */
    public static byte[] getMD5(byte[] src) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(src);
            return md.digest();
        } catch (Exception e) {
            QRomLog.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 判断字符串是否为空
     * 
     * @param src
     * @return
     */
    public static boolean isEmpty(String src) {
        if (src == null || src.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有中文
     */
    public static boolean hasNotAscII(String aInput) {

        int length = aInput.length();

        for (int i = 0; i < length; i++) {
            if (aInput.charAt(i) > 255) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断一个字符串是否是一个中国的手机号
     * 
     * @param src
     * @return
     */
    public static boolean isCellPhoneNumber(String src) {
        if (src == null) {
            return false;
        }
        int length = src.length();
        if (length < 11) {
            return false;
        }
        String regex = "^1\\d{10}$";
        String realNumber = src.substring(length - 11);
        boolean flag = realNumber.matches(regex); // 判断后11位是否是手机号
        if (!flag) {
            return false;
        }
        String other = src.substring(0, length - 11);
        if (other.length() == 0) {
            return true;
        }
        regex = "^\\+?\\d{2,4}$";
        return other.matches(regex);
    }

    /**
     * 替换一个字符串中的部分字符串，要求是替换的与
     * 
     * @return
     */
    public static String replace(String src, String target, String replacement) {
        if (target.length() == replacement.length()) {
            return src.replace(target, replacement);
        } else {
            return src;
        }
    }

    /**
     * @return 用关键词替换占位符%s后的字符串
     */
    public static String replaceSByKeyword(String src, String keyword) {
        if (src == null || keyword == null) {
            return src;
        }
        return src.replace("%s", "“" + keyword + "”");
    }

    /**
     * 用"|"对字符串进行分割，用于yiya来分割多个print或speak字符串
     * 
     * @param src
     * @return
     */
    public static String[] splitByDivider(String src) {
        if (src != null) {
            return src.split("\\|");
        } else {
            return null;
        }
    }

    /**
     * 将字节型数据转化为16进制字符串
     */
    public static String byteToHexString(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * 将16进制字符串转化为字节型数据
     */
    public static byte[] hexStringToByte(String hexString) {
        if (hexString == null || hexString.equals("") || hexString.length() % 2 != 0) {
            return null;
        }
        byte[] bData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bData[i / 2] = (byte) (Integer.parseInt(hexString.substring(i, i + 2), 16) & 0xff);
        }
        return bData;
    }

    /**
     * Get data speed string.
     */
    public static String getSpeedString(float speed) {
        if (speed < 0) {
            return String.format(SPEED_BYTE, 0);
        } else if (speed < K) {
            return String.format(SPEED_BYTE, (int) speed);
        } else if (speed < M) {
            return String.format(SPEED_KB, speed / K);
        } else if (speed < G) {
            return String.format(SPEED_MB, speed / M);
        } else {
            return String.format(SPEED_MB, speed / G);
        }
    }

    /**
     * Get data size string.
     */
    // public static String getSizeString(float size) {
    // if (size < 0) {
    // return
    // LauncherApp.getInstance().getResources().getString(R.string.qubestringutil_string_size_retrun);
    // } else if (size < K) {
    // return String.format(SIZE_BYTE, (int) size);
    // } else if (size < M) {
    // return String.format(SIZE_KB, size / K);
    // } else if (size < G) {
    // return String.format(SIZE_MB, size / M);
    // } else {
    // return String.format(SIZE_GB, size / G);
    // }
    // }

    /**
     * Get file's md5 string.
     */
    public static String md5sum(String filename) {
        InputStream fis = null;
        byte[] buffer = new byte[1024 * 8];
        int numRead;
        MessageDigest md5;
        try {
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return toHexString(md5.digest());
        } catch (Exception e) {
            QRomLog.e(TAG, e.getMessage());
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String md5sum(File file) {
        InputStream fis = null;
        byte[] buffer = new byte[1024 * 8];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(file);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return toHexString(md5.digest());
        } catch (Exception e) {
            QRomLog.e(TAG, e.getMessage());
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将十进制数据转化成指定长度的字符串 -- 长度不足用0补齐
     * 
     * @param num
     * @param len
     * @return
     */
    public static String formatInt2String(int num, int len) {
        if (len <= 0 || num > Math.pow(10, len)) { // 参数不合法
            return String.valueOf(num);
        }
        return String.format("%1$0" + len + "d", num);
    }

    public static String cleanString(String input) {
        if (input != null && !"".equals(input)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(input);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return input;
        }
    }

    public static int compareString(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return -1;
        }

        return str1.compareTo(str2);
    }
}
