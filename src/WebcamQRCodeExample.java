
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WebcamQRCodeExample extends JFrame implements Runnable, ThreadFactory, ActionListener{

    private static final long serialVersionUID = 6441489157408381878L;

    private final Executor executor = Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private JTextArea textarea = null;
    private JTextArea textarea1 = null;
    private JTextArea textarea2 = null;
    private JTextArea textarea3 = null;
    
    public WebcamQRCodeExample() {
        super();
        final JLabel label1 = new JLabel();
        JButton button = new JButton();
        
        setLayout(new FlowLayout());
        setTitle("Read QR / Bar Code With Webcam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Dimension size = WebcamResolution.QVGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        button.setPreferredSize(size);
        textarea = new JTextArea();
        textarea1 = new JTextArea();
        textarea2 = new JTextArea();
        textarea3 = new JTextArea();
        textarea.setEditable(false);
        textarea.setPreferredSize(size);
        textarea1.setEditable(false);
        textarea1.setPreferredSize(size);
        textarea2.setEditable(false);
        textarea2.setPreferredSize(size);
        textarea3.setEditable(false);
        textarea3.setPreferredSize(size);
        
       
        button.setText("PAID");
        add(panel);
        
        
        add(textarea);
        add(button);
        
          
        button.addActionListener(this);
      

        pack();
        setVisible(true);

        executor.execute(this);
    }

    @Override
    public void run() {

        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                textarea.setText(result.getText());
                String s = textarea.getText();

                Date date1 = new Date();
                System.out.println(date1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dateFormatted = dateFormat.format(date1);
                System.out.println(dateFormatted);

                String dateStart = dateFormatted;
                String dateStop = s;

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                Date d1 = null;
                Date d2 = null;

                try {
                    d1 = format.parse(dateStart);
                    d2 = format.parse(dateStop);

                    long diff = d2.getTime() - d1.getTime();
                    System.out.println(d1+".................");
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    System.out.print(diffDays + " days, ");
                    System.out.print(diffHours + " hours, ");
                    System.out.print(diffMinutes + " minutes, ");
                    System.out.print(diffSeconds + " seconds.");
                      long payment;
                    if (diffHours == 0){
                    payment = diffHours;
                    }else{
                    payment = diffHours * 2;
                    }

                    textarea.setText("\n Total Time");

                    textarea.setText(s + " is the time u entered. \n" + dateFormatted + " current time.\n total time is" + diffDays + diffHours + diffMinutes + diffSeconds + ".\n You have to pay RM"+payment+".");

                } catch (ParseException e) {
                }

            }

        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "example-runner");
        t.setDaemon(true);
        return t;
    }

    public static void main(String[] args) {
        new WebcamQRCodeExample();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      
      new gui().setVisible(true);
     
}}
