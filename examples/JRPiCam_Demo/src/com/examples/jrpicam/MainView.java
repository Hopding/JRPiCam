package com.examples.jrpicam;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.jrpicam.AWB;
import com.jrpicam.Camera;
import com.jrpicam.DRC;
import com.jrpicam.Exposure;

public class MainView {
	
	private JFrame		frame;
	private JTextField	txtTimeLapsePeriod;
	private JTextField	txtTimeout;
						
	private Camera		piCamera	= new Camera("/home/pi/Pictures");
									
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 615);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel imageLabel = new JLabel();
		imageLabel.setBounds(3, 11, 575, 565);
		frame.getContentPane().add(imageLabel);
		
		ButtonGroup group = new ButtonGroup();
		
		AbstractButton singleBtn = new JRadioButton("Single");
		singleBtn.setBounds(584, 41, 90, 23);
		
		AbstractButton timelapseBtn = new JRadioButton("Timelapse");
		timelapseBtn.setBounds(584, 65, 90, 23);
		
		group.add(singleBtn);
		group.add(timelapseBtn);
		frame.getContentPane().add(singleBtn);
		frame.getContentPane().add(timelapseBtn);
		
		txtTimeLapsePeriod = new JTextField();
		txtTimeLapsePeriod.setText("Timelapse Period");
		txtTimeLapsePeriod.setBounds(588, 95, 86, 20);
		frame.getContentPane().add(txtTimeLapsePeriod);
		txtTimeLapsePeriod.setColumns(10);
		
		txtTimeout = new JTextField();
		txtTimeout.setText("Timeout");
		txtTimeout.setBounds(588, 126, 86, 20);
		frame.getContentPane().add(txtTimeout);
		txtTimeout.setColumns(10);
		
		Label label = new Label("AWB:");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setBounds(615, 152, 36, 22);
		frame.getContentPane().add(label);
		
		JLabel lblNewLabel = new JLabel("DRC:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(617, 211, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		String[] awbComboBoxChoices = { "Off", "Auto", "Sun", "Cloud", "Shade", "Tungsten",
				"Fluorescent", "Incandescent", "Flash", "Horizon" };
		JComboBox awbComboBox = new JComboBox(awbComboBoxChoices);
		awbComboBox.setBounds(584, 180, 90, 20);
		frame.getContentPane().add(awbComboBox);
		
		String[] drcComboBoxChoices = { "Off", "High", "Medium", "Low" };
		JComboBox drcComboBox = new JComboBox(drcComboBoxChoices);
		drcComboBox.setBounds(584, 236, 90, 20);
		frame.getContentPane().add(drcComboBox);
		
		Label label_1 = new Label("Exposure:\r\n");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_1.setBounds(604, 262, 70, 22);
		frame.getContentPane().add(label_1);
		
		String[] expComboBoxChoices = { "Antishake", "Auto", "Backlight", "Beach", "Fireworks",
				"FixedFPS", "Night", "NightPreview", "Snow", "Sports",
				"Spotlight", "Verylong" };
		JComboBox expComboBox = new JComboBox(expComboBoxChoices);
		expComboBox.setBounds(584, 290, 90, 20);
		frame.getContentPane().add(expComboBox);
		
		JSlider contrastSlider = new JSlider();
		contrastSlider.setMinimum(-100);
		contrastSlider.setBounds(584, 341, 90, 23);
		frame.getContentPane().add(contrastSlider);
		
		JLabel lblContrast = new JLabel("Contrast:");
		lblContrast.setBounds(584, 316, 67, 14);
		frame.getContentPane().add(lblContrast);
		
		JLabel lblQuality = new JLabel("Quality:");
		lblQuality.setBounds(584, 375, 46, 14);
		frame.getContentPane().add(lblQuality);
		
		JSlider qualitySlider = new JSlider();
		qualitySlider.setValue(75);
		qualitySlider.setBounds(584, 400, 90, 29);
		frame.getContentPane().add(qualitySlider);
		
		JLabel lblSharpness = new JLabel("Sharpness:");
		lblSharpness.setBounds(584, 439, 66, 14);
		frame.getContentPane().add(lblSharpness);
		
		JSlider sharpnessSlider = new JSlider();
		sharpnessSlider.setValue(0);
		sharpnessSlider.setMinimum(-100);
		sharpnessSlider.setBounds(584, 464, 90, 23);
		frame.getContentPane().add(sharpnessSlider);
		
		JButton btnTake = new JButton("Take");
		btnTake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BufferedImage img;
				try {
					if (singleBtn.isSelected()) {
						piCamera.turnOffPreview();
						piCamera.setAWB(AWB.valueOf(((String) awbComboBox.getSelectedItem()).toUpperCase()));
						piCamera.setDRC(DRC.valueOf(((String) drcComboBox.getSelectedItem()).toUpperCase()));
						piCamera.setExposure(Exposure.valueOf(((String) expComboBox.getSelectedItem()).toUpperCase()));
						piCamera.setWidth(575);
						piCamera.setHeight(565);
						piCamera.setContrast(contrastSlider.getValue());
						piCamera.setQuality(qualitySlider.getValue());
						piCamera.setSharpness(sharpnessSlider.getValue());
						piCamera.setTimeout(Integer.parseInt(txtTimeout.getText()));
						img = ImageIO.read(piCamera.take("JRPiCamDemo.jpg"));
						ImageIcon icon = new ImageIcon(img);
						imageLabel.setIcon(icon);
					} else if (timelapseBtn.isSelected()) {
						System.out.println("Taking timelapse pic");
						JOptionPane.showMessageDialog(null, "Operation Not Yet Supported by JRPiCam Demo.");
						//IMPLEMENT TIME LAPSE
					}
					
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Value for Timeout.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnTake.setBounds(585, 11, 89, 23);
		frame.getContentPane().add(btnTake);
	}
}
