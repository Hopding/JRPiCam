package com.hopding.jrpicam.examples;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.AWB;
import com.hopding.jrpicam.enums.DRC;
import com.hopding.jrpicam.enums.Encoding;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

public class DemoView {
	
	private JFrame			frame;
	private JTextField		txtTimeout;
	private JButton			btnTake;
	private JButton			btnSave;
	private BufferedImage	buffImg;
							
	private RPiCamera		piCamera;
							
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DemoView window = new DemoView();
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
	public DemoView() {
		try {
			piCamera = new RPiCamera("/home/pi/Pictures");
		} catch (FailedToRunRaspistillException e) {
			e.printStackTrace();
		}
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
		
		txtTimeout = new JTextField();
		txtTimeout.setText("Timeout");
		txtTimeout.setBounds(584, 198, 90, 20);
		frame.getContentPane().add(txtTimeout);
		txtTimeout.setColumns(10);
		
		String[] encComboBoxChoices = { "jpg", "png", "bmp", "gif" };
		JComboBox encComboBox = new JComboBox(encComboBoxChoices);
		encComboBox.setBounds(584, 91, 90, 20);
		frame.getContentPane().add(encComboBox);
		
		Label label = new Label("AWB:");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setBounds(615, 224, 36, 22);
		frame.getContentPane().add(label);
		
		JLabel lblNewLabel = new JLabel("DRC:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(615, 283, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		String[] awbComboBoxChoices = { "Off", "Auto", "Sun", "Cloud", "Shade", "Tungsten",
				"Fluorescent", "Incandescent", "Flash", "Horizon" };
		JComboBox awbComboBox = new JComboBox(awbComboBoxChoices);
		awbComboBox.setBounds(584, 252, 90, 20);
		frame.getContentPane().add(awbComboBox);
		
		String[] drcComboBoxChoices = { "Off", "High", "Medium", "Low" };
		JComboBox drcComboBox = new JComboBox(drcComboBoxChoices);
		drcComboBox.setBounds(584, 308, 90, 20);
		frame.getContentPane().add(drcComboBox);
		
		Label label_1 = new Label("Exposure:\r\n");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_1.setBounds(604, 334, 70, 22);
		frame.getContentPane().add(label_1);
		
		String[] expComboBoxChoices = { "Antishake", "Auto", "Backlight", "Beach", "Fireworks",
				"FixedFPS", "Night", "NightPreview", "Snow", "Sports",
				"Spotlight", "Verylong" };
		JComboBox expComboBox = new JComboBox(expComboBoxChoices);
		expComboBox.setBounds(584, 362, 90, 20);
		frame.getContentPane().add(expComboBox);
		
		JSlider contrastSlider = new JSlider();
		contrastSlider.setMinimum(-100);
		contrastSlider.setBounds(588, 418, 90, 23);
		frame.getContentPane().add(contrastSlider);
		
		JLabel lblContrast = new JLabel("Contrast:");
		lblContrast.setBounds(588, 393, 67, 14);
		frame.getContentPane().add(lblContrast);
		
		JLabel lblQuality = new JLabel("Quality:");
		lblQuality.setBounds(588, 452, 46, 14);
		frame.getContentPane().add(lblQuality);
		
		JSlider qualitySlider = new JSlider();
		qualitySlider.setValue(75);
		qualitySlider.setBounds(584, 477, 90, 29);
		frame.getContentPane().add(qualitySlider);
		
		JLabel lblSharpness = new JLabel("Sharpness:");
		lblSharpness.setBounds(585, 517, 66, 14);
		frame.getContentPane().add(lblSharpness);
		
		JSlider sharpnessSlider = new JSlider();
		sharpnessSlider.setValue(0);
		sharpnessSlider.setMinimum(-100);
		sharpnessSlider.setBounds(588, 542, 90, 23);
		frame.getContentPane().add(sharpnessSlider);
		
		btnTake = new JButton("Take");
		btnTake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					piCamera.turnOffPreview()
						.setAWB(AWB.valueOf(((String) awbComboBox.getSelectedItem()).toUpperCase()))
					    .setDRC(DRC.valueOf(((String) drcComboBox.getSelectedItem()).toUpperCase()))
					    .setExposure(Exposure.valueOf(((String) expComboBox.getSelectedItem()).toUpperCase()))
					    .setEncoding(Encoding.valueOf(((String) encComboBox.getSelectedItem()).toUpperCase()))
					    .setWidth(575)
					    .setHeight(565)
					    .setContrast(contrastSlider.getValue())
					    .setQuality(qualitySlider.getValue())
					    .setSharpness(sharpnessSlider.getValue())
					    .setTimeout(Integer.parseInt(txtTimeout.getText()));
					buffImg = piCamera.takeBufferedStill();
					System.out.println("Executed this command:\n\t" + piCamera.getPrevCommand());
					ImageIcon icon = new ImageIcon(buffImg);
					imageLabel.setIcon(icon);
					btnSave.setEnabled(true);
					
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Please Enter a Value for Timeout.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnTake.setBounds(585, 11, 89, 23);
		frame.getContentPane().add(btnTake);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					String saveFilePath = fileChooser.getSelectedFile().getAbsolutePath() +
							"." + ((String) encComboBox.getSelectedItem());
					File saveFile = new File(saveFilePath);
					try {
						ImageIO.write(buffImg, ((String) encComboBox.getSelectedItem()), saveFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnSave.setBounds(585, 34, 89, 23);
		btnSave.setEnabled(false);
		frame.getContentPane().add(btnSave);
		
		JLabel lblEncoding = new JLabel("Encoding:");
		lblEncoding.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEncoding.setBounds(605, 68, 69, 14);
		frame.getContentPane().add(lblEncoding);
	}
}
