package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import java.util.logging.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

import com.revrobotics.CANEncoder;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;


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

  public ClimbingSubsystem() {
    // Wipe any prior motor settings
    // Set motor direction
    winchStartPosition = winchEncoder.getPosition();
    massMoverStartPosition = massMoverEncoder.getPosition();
    setName("Climbing Subsystem");
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
    // if(){
      SmartDashboard.putNumber("Hook position", winchEncoder.getPosition());
      SmartDashboard.putNumber("Hook start position", winchStartPosition);
      SmartDashboard.putNumber("Mass Mover position", massMoverEncoder.getPosition());
      SmartDashboard.putNumber("Mass mover start position", massMoverStartPosition);
    // }
  }
}
