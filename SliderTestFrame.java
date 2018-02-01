
package combiner;


import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;




/**
* Фрейм с линейными регуляторами и полем редактирования,
* отображающим текущее значение выбранного регулятора.
*/
class SliderTestFrame extends JFrame 
{
    public static int rfon = 37, bfon = 128, gfon = 147;
    public static void visit(File dir, File dir0){
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
        Boolean made = false;
        for(File it1 : dir.listFiles()){
            if(it1.isDirectory()){
                if(it1.getName().compareTo("Ch1") == 0){
                    for(File it2 : it1.listFiles()){
                        try{
                            imageb = ImageIO.read(it2);
                            w = imageb.getWidth();
                            h = imageb.getHeight();
                            b_blue = true;
                            name = it2.getName().substring(0, it2.getName().indexOf('.') - 2);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }else if(it1.getName().compareTo("Ch2") == 0){
                    for(File it2 : it1.listFiles()){
                        try{
                            imageg = ImageIO.read(it2);
                            w = imageg.getWidth();
                            h = imageg.getHeight();
                            b_green = true;
                            if(b_blue == false){
                                name = it2.getName().substring(0, it2.getName().indexOf('.') - 2);
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }else if(it1.getName().compareTo("Ch3") == 0){
                    for(File it2 : it1.listFiles()){
                        try{
                            imager = ImageIO.read(it2);
                            w = imager.getWidth();
                            h = imager.getHeight();
                            b_red = true;
                            if(b_blue == false && b_green == false){
                                name = it2.getName().substring(0, it2.getName().indexOf('.') - 2);
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                if(!b_blue && !b_green && !b_red){
                    visit(it1, dir0);
                }
       
            }
        }
        if(b_blue | b_green | b_red){
                   BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                   BufferedImage result_r = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                   BufferedImage result_g = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                   BufferedImage result_b = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
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
                            
                            
                            r-=rfon;
                            if(r <= rfon){
                                r = 0;
                            }else{
                                r+=rfon;
                            }
                            result_r.setRGB(x,y,r << 16);
                            /*if(g <= gfon){
                                g = 0;
                            }*/
                            g-=gfon;
                            if(g <= 0){
                                g = 0;
                            }else{
                                g+=gfon;
                            }
                            result_g.setRGB(x,y,g << 8);
                            b-=bfon;
                            if(b <= 0){
                                b = 0;
                            }else{
                                b+=bfon;
                            }
                            result_b.setRGB(x,y,b);
                            p = (r<<16) | (g<<8) | b;
                            result.setRGB(x, y, p);
                        }
                    }

                    if(made == false){
                        File comb = new File(dir0.getAbsolutePath() + '/' + name + "_combined.png");
                        File red = new File(dir0.getAbsolutePath() + '/' + name + "_red.png");
                        File green = new File(dir0.getAbsolutePath() + '/' + name + "_green.png");
                        File blue = new File(dir0.getAbsolutePath() + '/' + name + "_blue.png");
                        String format = "PNG";
                        try{
                            ImageIO.write(result, format, comb);
                            ImageIO.write(result_r, format, red);
                            ImageIO.write(result_g, format, green);
                            ImageIO.write(result_b, format, blue);
                            made = true;
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
        }
    }
    
    
    
    
	public SliderTestFrame()
	{
		setTitle("Combiner");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		sliderPanel = new JPanel();
		sliderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel rlab = new JLabel(Integer.toString(rfon));
                JLabel glab = new JLabel(Integer.toString(gfon));
                JLabel blab = new JLabel(Integer.toString(bfon));
                
                
                
		// Общий слушатель для всех регуляторов.
                blistener = new ChangeListener()
		{
			public void stateChanged(ChangeEvent event)
			{
				// Обновление поля редактирования при
				// изменении значения регулятора.
				JSlider source = (JSlider) event.getSource();
                                bfon = source.getValue();
                                blab.setText(Integer.toString(source.getValue()));
			}
		};
                
                glistener = new ChangeListener()
		{
			public void stateChanged(ChangeEvent event)
			{
				// Обновление поля редактирования при
				// изменении значения регулятора.
				JSlider source = (JSlider) event.getSource();
				gfon = source.getValue();
                                glab.setText(Integer.toString(source.getValue()));
			}
		};
                
		rlistener = new ChangeListener()
		{
			public void stateChanged(ChangeEvent event)
			{
				// Обновление поля редактирования при
				// изменении значения регулятора.
				JSlider source = (JSlider) event.getSource();
				rfon = source.getValue();
                                rlab.setText(Integer.toString(source.getValue()));
			}
		};
		

		JPanel panel = new JPanel();
		// Добавление регулятора с числовыми метками.
		
		JSlider rslider = new JSlider(0, 255, rfon);
		rslider.setPaintTicks(true);
		rslider.setPaintLabels(true);
		rslider.setMajorTickSpacing(50);
		rslider.setMinorTickSpacing(1);
                rslider.addChangeListener(rlistener);
                panel.add(rslider);
                panel.add(new JLabel("red"));
                panel.add(rlab);
                sliderPanel.add(panel);
                
                
                JSlider gslider = new JSlider(0, 255, gfon);
		gslider.setPaintTicks(true);
		gslider.setPaintLabels(true);
		gslider.setMajorTickSpacing(50);
		gslider.setMinorTickSpacing(1);
                gslider.addChangeListener(glistener);
		panel = new JPanel();
                panel.add(gslider);
                panel.add(new JLabel("green"));
                panel.add(glab);
                sliderPanel.add(panel);
                
                JSlider bslider = new JSlider(0, 255, bfon);
		bslider.setPaintTicks(true);
		bslider.setPaintLabels(true);
		bslider.setMajorTickSpacing(50);
		bslider.setMinorTickSpacing(1);
                bslider.addChangeListener(blistener);
		panel = new JPanel();
                panel.add(bslider);
                panel.add(new JLabel("blue"));
                panel.add(blab);
                sliderPanel.add(panel);
                
                
		JFileChooser fileopen = new JFileChooser();
		JButton button = new JButton("Open folder");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int ret = fileopen.showDialog(null, "Открыть файл");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File dir = fileopen.getSelectedFile();
                            visit(dir, dir);
                        }
                    }
                });
                sliderPanel.add(button);


		
		
		
		
		
		// Добавление поля редактирования для
		// отображения значения регулятора.
		
		add(sliderPanel, BorderLayout.CENTER);
	}
	/*
	 * Добавление регулятора в панель и связывание его со слушателем.
	 * @param s Регулятор
	 * @param description Описание регулятора
	 */
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 250;
	
	private JPanel sliderPanel;
	private JTextField textField;
	private ChangeListener rlistener;
        private ChangeListener glistener;
        private ChangeListener blistener;
        private ActionListener openfile;
}