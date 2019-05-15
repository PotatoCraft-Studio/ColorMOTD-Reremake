package net.andylizi.core;

import lombok.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.List;
import java.util.Random;

public class Icon {
    @Getter @Setter private List<BufferedImage> bufferedImages;
    @Getter @Setter private BufferedImage maintenanceImage;

    public Icon(List<BufferedImage> bufferedImages){
        this.bufferedImages=bufferedImages;
    }

    public void addIcon(BufferedImage icon){
        if(!bufferedImages.contains(icon) && icon!=maintenanceImage)
            bufferedImages.add(icon);
    }
    public void removeIcon(BufferedImage icon){
        if(bufferedImages.contains(icon))
            bufferedImages.remove(icon);
    }
    public BufferedImage randomIcon(){
        Random random = new Random();
        return bufferedImages.get(random.nextInt(bufferedImages.size()));
    }
}
