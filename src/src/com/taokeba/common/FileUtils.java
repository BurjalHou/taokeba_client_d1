package com.taokeba.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * Created by zhaolin on 14-2-25.
 */
public class FileUtils {

	private String LOG_TAG = "=== FileUtils ===";
	private String SDPATH = Environment.getExternalStorageDirectory() +"/";

    public static void write(Context context, String fileName, String content) {
        if(content == null) {
            content = "";
        }
        try {
            FileOutputStream fs = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fs.write(content.getBytes());
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readInStream(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            Log.e("FileTest", e.getMessage());
        }
        return null;
    }

    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if(!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName + fileName);
    }

    public static boolean writeFile(byte[] buffer, String folder, String fileName) {
        boolean writeSucc = false;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String folderPath = "";
        if(sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory() + File.separator + folder + File.separator;
        } else {
            writeSucc = false;
        }

        File fileDir = new File((folderPath));
        if(!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(folderPath + fileName);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            writeSucc = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writeSucc;
    }

    public static String getFileName(String filePath) {
        if(StringUtils.isEmpty(filePath)) {
            return "";
        }
        return filePath.substring((filePath.lastIndexOf(File.separator) + 1));
    }

    public static String getFileNameNoFormat(String filePath) {
        if(StringUtils.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1, point);
    }

    public static String getFileFormat(String fileName) {
        if(StringUtils.isEmpty(fileName)) {
            return "";
        }
        int point = fileName.lastIndexOf('c');
        return fileName.substring(point + 1);
    }

    public static long getFileSize(String filePath) {
        long size = 0;
        File file = new File(filePath);
        if(file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    public static String getFileSize(long size) {
        if(size <= 0) {
            return "0";
        }
        java.text.DecimalFormat df = new DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if(temp >= 1024) {
            return df.format(temp / 1024) + "M";
        } else {
            return df.format(temp) + "K";
        }
    }

    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for(File file : files) {
            if(file.isDirectory()) {
                count = count + getFileList(file);
                count --;
            }
        }
        return count;
    }

    public static byte[] toBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }
        byte buffer[] = out.toByteArray();
        out.close();
        return buffer;
    }

    public static boolean checkFileExists(String name) {
        boolean status;
        if (!name.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + name);
            status = newPath.exists();
        } else {
            status = false;
        }
        return status;
    }

    public static boolean checkFilePathExists(String path) {
        return new File(path).exists();
    }

    @SuppressLint("NewApi") public static long getFreeDiskSpace() {
        String status =Environment.getExternalStorageState();
        long freeSpace = 0;
        if(status.equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlock = stat.getAvailableBlocksLong();
            freeSpace = availableBlock * blockSize / 1024;
        } else {
            return -1;
        }
        return freeSpace;
    }

    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            //status = newPath.mkdir();
            status = true;
        } else
            status = false;
        return status;
    }

    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
            status = true;
        } else
            status = false;
        return status;
    }

    public static boolean checkExternalSDExists() {

        Map<String, String> evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    public static boolean deleteDirectory(String fileName) {
        boolean status = false;
        SecurityManager checker = new SecurityManager();

        if(!fileName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if(newPath.isDirectory()) {
                String[] listFile = newPath.list();

                for(int i = 0; i < listFile.length; i++) {
                    File deleteFile = new File(newPath.toString() + listFile[i].toString());
                    deleteFile.delete();
                }
                newPath.delete();
                status = true;
            } else {
                status = false;
            }

        } else {
            return false;
        }
        return status;
    }


    public static boolean deleteFile(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    Log.i("DirectoryManager deleteFile", fileName);
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    public static int deleteBlankPath(String path) {
        File f = new File(path);
        if (!f.canWrite()) {
            return 1;
        }
        if (f.list() != null && f.list().length > 0) {
            return 2;
        }
        if (f.delete()) {
            return 0;
        }
        return 3;
    }


    public static boolean reNamePath(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }

    public static String getSDRoot() {

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getExternalSDRoot() {

        Map<String, String> evn = System.getenv();

        return evn.get("SECONDARY_STORAGE");
    }

    /**
     * 列出root目录下所有子目录
     *
     * @param
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        // 过滤掉以.开始的文件夹
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory() && !f.getName().startsWith(".")) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }

    public enum PathStatus {
        SUCCESS, EXITS, ERROR
    }

    /**
     * 创建目录
     *
     * @param
     */
    public static PathStatus createPath(String newPath) {
        File path = new File(newPath);
        if (path.exists()) {
            return PathStatus.EXITS;
        }
        if (path.mkdir()) {
            return PathStatus.SUCCESS;
        } else {
            return PathStatus.ERROR;
        }
    }

    /**
     * 截取路径名
     *
     * @return
     */
    public static String getPathName(String absolutePath) {
        int start = absolutePath.lastIndexOf(File.separator) + 1;
        int end = absolutePath.length();
        return absolutePath.substring(start, end);
    }
    
    
    /*
     * 在SD卡上创建目录
     */
     
    public File createSDDir(String dirName){
        File dir = new File(SDPATH+dirName);
        dir.mkdir();
        return dir;
    }
    
    /*
     * 在SD卡上创建文件
     */
    public File createSDFile(String fileName) throws IOException{
        File file = new File(SDPATH+fileName);
        file.createNewFile();
        return file;
    }
    
    
    /**
     * 将一个InputStream里面的数据写入到SD卡
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public File write2SDFromInput(String path,String fileName,InputStream input){
        File file = null;
        OutputStream output = null;
        try {
            //创建目录
        	createSDDir(path);
            //创建文件
            file = createSDFile(path+fileName);
            //创建输出流
            output = new FileOutputStream(file);
            //创建缓冲区
            byte buffer[] =  new byte[4*1024];
            //写入数据
            while((input.read(buffer))!=-1){
                output.write(buffer);
            }
            //清空缓存
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("LOG_TAG","write to SD false!");
        }finally{
            try {
                //关闭输出流
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}

