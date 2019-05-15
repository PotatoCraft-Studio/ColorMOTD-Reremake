package net.andylizi.core;

import lombok.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Icon {
    @Getter @Setter private List<BufferedImage> bufferedImages;

    public void addIcon(BufferedImage icon){
        if(!bufferedImages.contains(icon))
            bufferedImages.add(icon);
    }
    public void removeIcon(BufferedImage icon){
        if(bufferedImages.contains(icon))
            bufferedImages.remove(icon);
    }
}
