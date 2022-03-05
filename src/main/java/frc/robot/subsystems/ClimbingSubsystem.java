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
  private double winchStartPosition;
  private SendableChooser<Boolean> showValues;

  public ClimbingSubsystem() {
    // Wipe any prior motor settings
    // Set motor direction
    winchStartPosition = winchEncoder.getPosition();
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

  @Override
  public void periodic() {
    if(showValues.getSelected()){
      updateSmartDashboard();
    }
  }

  public void updateSmartDashboard(){
      SmartDashboard.putNumber("hooks position", winchEncoder.getPosition());
      SmartDashboard.putNumber("hooks velocity", winchEncoder.getVelocity());
  }
}
