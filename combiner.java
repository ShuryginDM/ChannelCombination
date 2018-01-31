import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;


public class combiner
{
    public static void main(String []args) {
        File dir = new File(".");
        for(File item : dir.listFiles()){
            if(item.isDirectory()){
                File dir1 = item;
                File fr = null;
                File fg = null;
                File fb = null;
                String name = null;
                int w = 0; 
                int h = 0;
                BufferedImage imager = null;
                BufferedImage imageg = null;
                BufferedImage imageb = null;
                Boolean b_blue = false;
                Boolean b_green = false;
                Boolean b_red = false;
                for(File it1 : dir1.listFiles()){
                    if(it1.getName().compareTo("c1") == 0){
                        for(File it2 : it1.listFiles()){
                            try{
                                imageb = ImageIO.read(it2);
                                w = imageb.getWidth();
                                h = imageb.getHeight();
                                b_blue = true;
                                name = it2.getName().substring(0, it2.getName().indexOf('.') - 2);
                                name += "_combined.png";
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    if(it1.getName().compareTo("c2") == 0){
                        for(File it2 : it1.listFiles()){
                            try{
                                imageg = ImageIO.read(it2);
                                w = imageg.getWidth();
                                h = imageg.getHeight();
                                b_green = true;
                                if(b_blue == false){
                                    name = it2.getName().substring(0, it2.getName().indexOf('.') - 2);
                                    name += "_combined.png";
                                }
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    if(it1.getName().compareTo("c3") == 0){
                        for(File it2 : it1.listFiles()){
                            try{
                                imager = ImageIO.read(it2);
                                w = imager.getWidth();
                                h = imager.getHeight();
                                b_red = true;
                                if(b_blue == false && b_green == false){
                                    name = it2.getName().substring(0, it2.getName().indexOf('.') - 2);
                                    name += "_combined.png";
                                }
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(b_blue | b_green | b_red){
                    BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    for(int y = 0; y < h; y++){
                        for(int x = 0; x < w; x++){
                            int p = 0;
                            int pr = 0;
                            int pg = 0;
                            int pb = 0;
                            if(b_red){
                                pr = imager.getRGB(x,y);
                            }
                            if(b_green){
                                pg = imageg.getRGB(x,y);
                            }
                            if(b_blue){
                                pb = imageb.getRGB(x,y);
                            }
                            int r = (pr>>16)&0xff;// + (pr>>8)&0xff + pr&0xff);
                            int g = (pg>>8)&0xff;
                            int b = pb&0xff;
                            p = (r<<16) | (g<<8) | b;
                            result.setRGB(x, y, p);
                        }
                    }

                    File g = new File(name);
                    String format = "PNG";
                    try{
                        ImageIO.write(result, format, g);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
