package robotopencontrol;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;


public class ROUI extends JFrame implements Observer,ItemListener,KeyListener {

	private static final long serialVersionUID = 8926783466054436880L;
	private RORobotInstance robotInstance;
	
	private boolean connected = false;
	private boolean enabled = false;
	
	private javax.swing.JList dsList;
    private javax.swing.JButton jConnectBtn;
    private javax.swing.JButton jEnableBtn;
    private javax.swing.JTextField jIPField;
    private javax.swing.JLabel jProgTitle;
    private javax.swing.JLabel jRoboStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel joy1Label;
    private javax.swing.JComboBox joy1Selection;
    private javax.swing.JLabel joy2Label;
    private javax.swing.JComboBox joy2Selection;
    private javax.swing.JLabel robotIPLabel;
	
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception e) {}

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ROUI().setVisible(true);
            }
        });
    }
    
	public ROUI() {
		robotInstance = new RORobotInstance();
		robotInstance.getDashboardData().addObserver(this);
		robotInstance.getJoystickHandler().addObserver(this);
		addKeyListener (this);
		buildGUI();
	}

	public void buildGUI() {
		jProgTitle = new javax.swing.JLabel();
        jRoboStatus = new javax.swing.JLabel();
        jEnableBtn = new javax.swing.JButton();
        joy1Selection = new javax.swing.JComboBox();
        joy2Selection = new javax.swing.JComboBox();
        jIPField = new javax.swing.JTextField();
        jConnectBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        String initList[] = { "Waiting for dashboard data..." };
        dsList = new javax.swing.JList(initList);
        joy1Label = new javax.swing.JLabel();
        joy2Label = new javax.swing.JLabel();
        robotIPLabel = new javax.swing.JLabel();

        jProgTitle.setText("RobotOpenController v0.1");

        jRoboStatus.setText("Disconnected");

        jEnableBtn.setEnabled(false);
        jEnableBtn.setText("Enable");
        jEnableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEnableBtnActionPerformed(evt);
            }
        });

        String[] joys = robotInstance.getJoystickHandler().getControllers();
        
        joy1Selection.setModel(new javax.swing.DefaultComboBoxModel(joys));
        joy1Selection.addItemListener(this);

        joy2Selection.setModel(new javax.swing.DefaultComboBoxModel(joys));
        joy2Selection.addItemListener(this);

        jIPField.setText("10.0.0.22");

        jConnectBtn.setText("Connect");
        jConnectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jConnectBtnActionPerformed(evt);
            }
        });

        dsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dsList.setEnabled(false);
        jScrollPane1.setViewportView(dsList);

        joy1Label.setText("Joy 1:");

        joy2Label.setText("Joy 2:");

        robotIPLabel.setText("Robot IP:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jEnableBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        		.add(jProgTitle)
                            .add(layout.createSequentialGroup()
                                .add(joy1Label)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(joy1Selection, 160, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                
                                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                    .add(joy2Label)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(joy2Selection, 160, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 117, Short.MAX_VALUE)
                                .add(jRoboStatus))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jConnectBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                    .add(robotIPLabel)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jIPField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jProgTitle)
                    .add(jRoboStatus))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(joy1Label)
                    .add(joy1Selection, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jIPField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(robotIPLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(joy2Label)
                    .add(joy2Selection, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jConnectBtn))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jEnableBtn)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        // Set the title of the root GUI window
	    setTitle("RobotOpenControl v0.1");
	    // Make sure the user can't resize it and fudge up all the spacing
	    setResizable(false);
	    // Exit the application when the main GUI window is closed
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // Size the window to fit in everything nicely
	    setSize(400, 500);
    }

	private void jConnectBtnActionPerformed(java.awt.event.ActionEvent evt) {
		if (!connected) {
			try {
				joy1Selection.setEnabled(false);
				joy2Selection.setEnabled(false);
	            InetAddress.getByName(jIPField.getText());
	            robotInstance.getPacketTransmitter().setRemoteTarget(jIPField.getText(), 2);
				robotInstance.getPacketTransmitter().beginXmit();
				jEnableBtn.setEnabled(true);
				jConnectBtn.setText("Disconnect");
				jRoboStatus.setText("Broadcasting (Disabled)");
				connected = true;
	        }
	        catch (Exception e) {System.out.println(e.toString());}
		}
		else {
			robotInstance.getPacketTransmitter().terminate();
			jEnableBtn.setEnabled(false);
			jEnableBtn.setText("Enable");
			jConnectBtn.setText("Connect");
			jRoboStatus.setText("Disconnected");
			enabled = false;
			connected = false;
			joy1Selection.setEnabled(true);
			joy2Selection.setEnabled(true);
		}
	}

	private void jEnableBtnActionPerformed(java.awt.event.ActionEvent evt) {
		if (!enabled) {
			robotInstance.getPacketTransmitter().setEnabled();
			jEnableBtn.setText("Disable");
			jRoboStatus.setText("Broadcasting (Enabled)");
			enabled = true;
		}	
		else {
			robotInstance.getPacketTransmitter().setDisabled();
			jEnableBtn.setText("Enable");
			jRoboStatus.setText("Broadcasting (Disabled)");
			enabled = false;
		}
	}
	
	private void activateControllers() {
		robotInstance.getJoystickHandler().clearControllers();
		if (joy1Selection.getSelectedIndex() != 0)
			robotInstance.getJoystickHandler().activateController(joy1Selection.getSelectedIndex() - 1);
		if (joy2Selection.getSelectedIndex() != 0)
			robotInstance.getJoystickHandler().activateController(joy2Selection.getSelectedIndex() - 1);
	}

	@Override
	public void update(Observable obs, Object obj) {
		if (obs == robotInstance.getJoystickHandler()) {
			// We've hit a joystick error - disable everything
			if (connected) {
				robotInstance.getPacketTransmitter().terminate();
				jEnableBtn.setEnabled(false);
				jEnableBtn.setText("Enable");
				jConnectBtn.setText("Connect");
				jRoboStatus.setText("Disconnected");
				enabled = false;
				connected = false;
			}
			
			System.exit(0);
		}
		else if (obs == robotInstance.getDashboardData()) {
			// New Packet!
			ArrayList<String> bundles = robotInstance.getDashboardData().getBundles();
			
			String[] dashboardData = new String[bundles.size()];
			
			for (int i = 0; i < bundles.size(); i++)
				dashboardData[i] = bundles.get(i);
			
			dsList.setListData(dashboardData);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			activateControllers();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		if(keyCode==KeyEvent.VK_SPACE) {
			if (enabled) {
				robotInstance.getPacketTransmitter().setDisabled();
				jEnableBtn.setText("Enable");
				jRoboStatus.setText("Connected (Disabled)");
				enabled = false;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}