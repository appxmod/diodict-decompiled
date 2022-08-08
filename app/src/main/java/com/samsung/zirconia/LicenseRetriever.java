package com.samsung.zirconia;

import com.diotek.diodict.dhwr.b2c.kor.DHWR;
import com.diotek.diodict.engine.DictType;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LicenseRetriever {
    private String applicationID;
    private String deviceIMEI;
    private String deviceIMSI;
    private String deviceMIN;
    private String deviceModel;
    private int errorCode = 0;
    private String executableFilePath;
    private URL httpURL;
    private HttpURLConnection httpUrlConnection;
    private String licenseFilePath;
    private int numRedirects;

    public LicenseRetriever(String deviceIMEI, String applicationID, String deviceIMSI, String deviceModel, String deviceMIN, String licenseFilePath, String executableFilePath) {
        this.deviceIMEI = deviceIMEI;
        this.applicationID = applicationID;
        this.deviceIMSI = deviceIMSI;
        this.deviceModel = deviceModel;
        this.deviceMIN = deviceMIN;
        this.licenseFilePath = licenseFilePath;
        this.executableFilePath = executableFilePath;
    }

    public int retrieveLicense() {
        try {
            this.errorCode = 62;
            open();
            sendRequest();
            this.errorCode = 61;
            receiveResponse();
            close();
        } catch (LicenseRetrieverException e) {
        } catch (MalformedURLException e2) {
        } catch (IOException e3) {
        }
        return this.errorCode;
    }

    private String urlEncode(String s) {
        return URLEncoder.encode(s).replaceAll("\\+", "%20");
    }

    private String makeParameter() {
        String versionString = String.format("%02d%03d", 1, Integer.valueOf((int) DHWR.DLANG_HINDI));
        String parameterString = String.format("deviceid=%s&applicationid=%s&subscriberid=%s&model=%s&min=%s&version=%s", urlEncode(this.deviceIMEI), urlEncode(this.applicationID), urlEncode(this.deviceIMSI), urlEncode(this.deviceModel), urlEncode(this.deviceMIN), urlEncode(versionString));
        return parameterString;
    }

    private void open() throws MalformedURLException {
        String urlString = "https://zirconia.samsungapps.com:443/chkLicense.as";
        if ("POST".compareToIgnoreCase("GET") == 0) {
            urlString = String.valueOf(urlString) + "?" + makeParameter();
        }
        this.numRedirects = 0;
        this.httpURL = new URL(urlString);
        this.httpUrlConnection = null;
        HttpURLConnection.setFollowRedirects(false);
    }

    private void sendRequest() throws MalformedURLException, IOException {
        this.httpUrlConnection = (HttpURLConnection) this.httpURL.openConnection();
        this.httpUrlConnection.setRequestProperty("Acceept", "*/*");
        this.httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        this.httpUrlConnection.setRequestProperty("User-agent", "ZrO2-ADR");
        this.httpUrlConnection.setDefaultUseCaches(false);
        this.httpUrlConnection.setUseCaches(false);
        if (this.numRedirects == 0 && "POST".compareToIgnoreCase("POST") == 0) {
            String postParameterData = makeParameter();
            this.httpUrlConnection.setDoOutput(true);
            this.httpUrlConnection.setRequestMethod("POST");
            OutputStream outputStream = this.httpUrlConnection.getOutputStream();
            outputStream.write(postParameterData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        }
        int responseCode = this.httpUrlConnection.getResponseCode();
        if (responseCode >= 300 && responseCode <= 307 && responseCode != 306 && responseCode != 304) {
            URL base = this.httpUrlConnection.getURL();
            String loc = this.httpUrlConnection.getHeaderField("Location");
            if (loc != null) {
                this.httpURL = new URL(base, loc);
            }
            this.httpUrlConnection.disconnect();
            if (this.httpURL == null || this.numRedirects >= 5) {
                throw new SecurityException("illegal URL redirect");
            }
            this.numRedirects++;
            sendRequest();
        }
    }

    private void receiveResponse() throws IOException, LicenseRetrieverException {
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(this.httpUrlConnection.getInputStream()));
        byte[] buffer = new byte[DictType.SEARCHTYPE_PINYIN];
        dataInputStream.read(buffer, 0, buffer.length);
        byte[] bufferHeader = new byte[12];
        System.arraycopy(buffer, 0, bufferHeader, 0, 11);
        String licenseResponseHeader = new String(bufferHeader);
        if (!licenseResponseHeader.startsWith("ZrO2")) {
            this.errorCode = 71;
            throw new LicenseRetrieverException(this, null);
        }
        int serverResponseCode = Integer.valueOf(licenseResponseHeader.substring(9, 11)).intValue();
        if (serverResponseCode != 12) {
            this.errorCode = serverResponseCode;
            throw new LicenseRetrieverException(this, null);
        }
        byte[] licenseKey = new byte[40];
        System.arraycopy(buffer, 11, licenseKey, 0, 20);
        boolean isStored = NativeInterface.storeLicenseKey(this.licenseFilePath, licenseKey, this.executableFilePath);
        if (!isStored) {
            this.errorCode = 81;
            throw new LicenseRetrieverException(this, null);
        } else {
            this.errorCode = 50;
        }
    }

    private void close() {
        this.httpURL = null;
        if (this.httpUrlConnection != null) {
            this.httpUrlConnection.disconnect();
        }
        this.httpUrlConnection = null;
    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = {new X509TrustManager() { // from class: com.samsung.zirconia.LicenseRetriever.1
            @Override // javax.net.ssl.X509TrustManager
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override // javax.net.ssl.X509TrustManager
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override // javax.net.ssl.X509TrustManager
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class LicenseRetrieverException extends Exception {
        private static final long serialVersionUID = 1;

        private LicenseRetrieverException() {
        }

        /* synthetic */ LicenseRetrieverException(LicenseRetriever licenseRetriever, LicenseRetrieverException licenseRetrieverException) {
            this();
        }
    }

    static {
        disableSSLCertificateChecking();
    }
}
