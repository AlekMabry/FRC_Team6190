
package org.usfirst.frc.team6190.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.SampleRobot;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    
    String autoSelected;
    SendableChooser chooser;
    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick Xbox = new Joystick(0); // set to ID 1 in DriverStation
    Ultrasonic ultra = new Ultrasonic(9,8);
    
    AnalogInput ai = new AnalogInput(0);
    
    double leftValuea;
    double rightValuea;
    
    public Robot() {
        myRobot = new RobotDrive(0, 1);
        myRobot.setExpiration(0.1);
    }
    
    
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        ultra.setAutomaticMode(true);
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here 
        	double range = ultra.getRangeInches();
        	if (range >= 1) {
        		leftValuea = 0.1;
        		rightValuea = 0.1;
        		}
        	if (range <= 5) {leftValuea = 0.7; rightValuea = 0.7;}
        	myRobot.tankDrive(leftValuea, rightValuea);
            Timer.delay(0.005);		// wait for a motor update time
    	case defaultAuto:
    	default:
    	//Put default auto code here
        	range = ultra.getRangeInches();
        	if (range >= 1) {
        		leftValuea = 0.1;
        		rightValuea = 0.1;
        		}
        	if (range <= 5) {leftValuea = 0.7; rightValuea = 0.7;}
        	myRobot.tankDrive(leftValuea, rightValuea);
            Timer.delay(0.005);		// wait for a motor update time
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
        myRobot.setSafetyEnabled(true);
        
        while (isOperatorControl() && isEnabled()) {
        	
	        double xLeft = Xbox.getRawAxis(5); 
	        double xRight = Xbox.getRawAxis(1);    
	        	
        	myRobot.tankDrive(xLeft, xRight);
        	
            Timer.delay(0.005);		// wait for a motor update time
            
        }
        
    }

    /**
     * This function is used to measure and read analog inputs
     */
    
    public void readAnlg() {
    	
        AnalogInput.setGlobalSampleRate(62500);
        
        // Reads Input Of Analog Input 0
        int Raw = ai.getValue();
        double Volts = ai.getVoltage();
        int averageRaw = ai.getAverageValue();
        double averageVolts = ai.getAverageVoltage();
        
        if (Volts >= 2.5) {
        	
        //	myRobot.tankDrive(0.3, -0.3);
        	
        }
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
