package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import java.util.logging.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


/**
 * Creates a new ShooterSubsystem.
 */
public class ClimbingSubsystem extends SubsystemBase {
  // private Logger logger = Logger.getLogger(this.getClass().getName());
  private CANSparkMax winch = new CANSparkMax(Constants.climberMotorCanID, MotorType.kBrushless);
  private RelativeEncoder winchEncoder = winch.getEncoder();
  private CANSparkMax massMover = new CANSparkMax(Constants.massMoverCanID, MotorType.kBrushless);
  private RelativeEncoder massMoverEncoder = massMover.getEncoder();
  private double winchStartPosition;
  private double massMoverStartPosition;
  private SendableChooser<Boolean> showValues;

  public ClimbingSubsystem() {
    // Wipe any prior motor settings
    // Set motor direction
    winchStartPosition = winchEncoder.getPosition();
    massMoverStartPosition = massMoverEncoder.getPosition();
    setName("Climbing Subsystem");
    showValues = new SendableChooser<Boolean>();
    showValues.setDefaultOption("false", false);
    showValues.addOption("true", true);
    SmartDashboard.putData("Climbing Subsystem show value", showValues);
  }

  public void runWinch(double input){
    winch.set(input);
  }

  public void retractRope(){
    runWinch(0.5);
  }

  public void releaseRope(){
    runWinch(-0.5);
  }

  public void stopWinch() {
    winch.stopMotor();
  }

  public double getWinchPosition() {
    return winchEncoder.getPosition();
  }

  public double getWinchStartPosition(){
    return winchStartPosition;
  }

  public void stopMassMover(){
    massMover.stopMotor();
  }

  public void runMassMover(double percent){
    massMover.set(percent);
  }

  public double getMassMoverPosition() {
    return massMoverEncoder.getPosition();
  }

  public double getMassMoverStartPosition(){
    return massMoverStartPosition;
  }

  @Override
  public void periodic() {
    if(showValues.getSelected()){
      updateSmartDashboard();
    }
  }

  public void updateSmartDashboard(){
      SmartDashboard.putNumber("mass mover position", massMoverEncoder.getPosition());
      SmartDashboard.putNumber("mass mover velocity", massMoverEncoder.getVelocity());
      SmartDashboard.putNumber("hooks position", winchEncoder.getPosition());
      SmartDashboard.putNumber("hooks velocity", winchEncoder.getVelocity());
  }
}
