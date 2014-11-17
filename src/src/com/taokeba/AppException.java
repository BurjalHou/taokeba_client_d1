package com.taokeba;

import android.content.Context;
import android.os.Environment;

import org.apache.http.HttpException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;

/**
 * Created by zhaolin on 14-2-25.
 */
public class AppException extends Exception implements Thread.UncaughtExceptionHandler {

    private final static boolean DEBUG = false; //是否保存错误日志

    public final static byte TYPE_NETWORK   = 0x01;
    public final static byte TYPE_SOCKET	= 0x02;
    public final static byte TYPE_HTTP_CODE	= 0x03;
    public final static byte TYPE_HTTP_ERROR= 0x04;
    public final static byte TYPE_XML	 	= 0x05;
    public final static byte TYPE_IO	 	= 0x06;
    public final static byte TYPE_RUN	 	= 0x07;

    private byte type;
    private int code;

    private Thread.UncaughtExceptionHandler defaultHander;

    private AppException() {
        this.defaultHander = Thread.getDefaultUncaughtExceptionHandler();
    }

    private AppException(byte type, int code, Exception excp) {
        super(excp);
        this.type = type;
        this.code = code;
        if(DEBUG) {
            this.saveErrorLog(excp);
        }
    }

    private byte getType() {
        return this.type;
    }

    private int getCode() {
        return this.code;
    }

    public void makeToast(Context context) {
        switch (this.getType()) {
            case TYPE_HTTP_CODE:

                break;
            case TYPE_HTTP_ERROR:

                break;
            case TYPE_SOCKET:

                break;
            case TYPE_NETWORK:
                break;
            case TYPE_XML:

                break;
            case TYPE_RUN:

                break;
            case TYPE_IO:

                break;
            default:
                break;
        }
    }

    public void saveErrorLog(Exception excp) {
        String errorLog = "errorlog.txt";
        String savePath = "";
        String logFilePath = "";
        FileWriter fw = null;
        PrintWriter pw = null;

        try {
            String storageState = Environment.getExternalStorageState();
            if(storageState.equals(Environment.MEDIA_MOUNTED)) {
                savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/taokeba/";
                File file = new File(savePath);
                if(!file.exists()) {
                    file.mkdirs();
                }
                logFilePath = savePath + errorLog;
            }
            if(logFilePath == "") {
                return;
            }
            File logFile = new File(logFilePath);
            if(!logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile, true);
            pw = new PrintWriter(fw);
            pw.println("-----------------" + (new GregorianCalendar().toString()) + "-----------------");
            excp.printStackTrace(pw);
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public static AppException http(int code) {
        return new AppException(TYPE_HTTP_CODE, code, null);
    }

    public static AppException http(Exception e) {
        return new AppException(TYPE_HTTP_ERROR, 0, e);
    }

    public static AppException socket(Exception e) {
        return new AppException(TYPE_SOCKET, 0, e);
    }

    public static AppException io(Exception e) {
        if(e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, 0, e);
        } else if(e instanceof  IOException) {
            return new AppException(TYPE_IO, 0, e);
        }
        return run(e);
    }

    public static AppException xml(Exception e) {
        return new AppException(TYPE_XML, 0, e);
    }

    public static AppException network(Exception e) {
        if(e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, 0, e);
        } else if(e instanceof HttpException) {
            return http(e);
        } else if(e instanceof SocketException) {
            return socket(e);
        }
        return http(e);
    }

    public static AppException run(Exception e) {
        return new AppException(TYPE_RUN, 0, e);
    }

    public static AppException getAppExceptionhandler() {
        return new AppException();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if(!handleException(throwable) && defaultHander == null) {
            defaultHander.uncaughtException(thread, throwable);
        }
    }

    private boolean handleException(Throwable throwable) {
        if(throwable == null) {
            return false;
        }

        final Context context = AppManager.getAppManager().currentActivity();

        if(context == null) {

        }
        return true;
    }

//    private String getCrashReport(Context context, Throwable throwable) {
//
//    }
}
