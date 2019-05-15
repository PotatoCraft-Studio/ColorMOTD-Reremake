package net.andylizi.core;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.ConnectIOException;
import java.util.ArrayList;

public class ConfigManager {
    Gson gson = new Gson();
    @Getter @Setter Config config;
    @Getter @Setter Icon icon;
    File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
        if (!dataFolder.exists())
            dataFolder.mkdirs();
        saveResources();
        config = readConfig();
        icon = readIcons();

    }

    private Icon readIcons(){
        Icon icon = new Icon(new ArrayList<>()); //以ArrayList方式创建Icon表
        File[] files = dataFolder.listFiles();
        for (File file : files){
            if(file.getName().endsWith(".png")){
                BufferedImage bufferedImage;
                try{
                    bufferedImage = ImageIO.read(file);
                    icon.addIcon(bufferedImage);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                    continue;
                }

            }
        }
        return icon;
    }


    public boolean saveConfig() {
        try {
            File configFile = new File(dataFolder, "config.json");
            if (!configFile.exists())
                configFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(configFile);
            fileOutputStream.write(gson.toJson(config).getBytes());
            fileOutputStream.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    private Config readConfig() {
        File configFile = new File(dataFolder, "config.json");
        String configString = readToString(configFile.getPath());
        Config config;
        if (!configString.isEmpty()) {
            //初始化配置文件
            config = Config.createDefaultConfig(); //读取默认配置
            this.config = config; //设置到Field
            saveConfig(); //从Field保存到Disk
        } else {
            try {
                config = gson.fromJson(configString, Config.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return Config.createDefaultConfig();
            }
        }
        return config;
    }

    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //生成默认资源文件
    private void saveResources() {
        //1.png
        try {
            saveResource("/default/1.png", this.dataFolder.getPath(), false);
            saveResource("/default/2.png", this.dataFolder.getPath(), false);
            saveResource("/default/3.png", this.dataFolder.getPath(), false);
            saveResource("/default/error.png", this.dataFolder.getPath(), false);
            saveResource("/default/serviceModeIcon.png", this.dataFolder.getPath(), false);
            saveResource("/default/formater.js", this.dataFolder.getPath(), false);
        } catch (IOException ignore) {
        }
    }

    private boolean saveResource(String jarPath, String diskPath, boolean overWrite) throws IOException {
        InputStream is = this.getClass().getResourceAsStream(jarPath);
        byte b[] = new byte[1024];
        int len = 0;
        int temp = 0;
        while ((temp = is.read()) != -1) {    //当没有读取完时，继续读取
            b[len] = (byte) temp;
            len++;
        }

        is.close();

        File file = new File(diskPath);
        if (!overWrite && file.exists())
            return true; //不覆写
        boolean createResult = false;
        if (!file.exists()) {
            createResult = file.createNewFile();
            if (!createResult)
                return false;
        }

        FileOutputStream out = new FileOutputStream(file);
        out.write(b);
        out.close();
        return true;
    }
}
