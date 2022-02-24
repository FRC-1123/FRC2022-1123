package frc.robot.subsystems;

import java.util.logging.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.SendableWrapperClasses.*;
// import com.revrobotics.CANSparkMax.ControlType;


public class BallSubsystem extends SubsystemBase {
  private Logger logger = Logger.getLogger(this.getClass().getName());
  private TalonFX liftMotor = new TalonFX(Constants.intakeLifterMotorCanID);
  // private CANSparkMax intakeRoller = new CANSparkMax(Constants.intakeBallMotorCanID,MotorType.kBrushless);
  private SendableSparkMax intakeRoller = new SendableSparkMax(Constants.intakeBallMotorCanID,MotorType.kBrushless);
  private double lifterStartPosition;

  public BallSubsystem() {
    liftMotor.setNeutralMode(NeutralMode.Brake);
    liftMotor.setInverted(true);
    liftMotor.configOpenloopRamp(0.2);
    liftMotor.configClosedloopRamp(0.2);

    intakeRoller.setIdleMode(CANSparkMax.IdleMode.kCoast);
    lifterStartPosition = getLiftMotorPosition();
    setName("Ball Subsystem");
    // addChild("Intake Roller", intakeRoller);
  }

  public void spinIntake(double setVoltage){
    intakeRoller.set(setVoltage);
  }

  public void spinIntake(){
    spinIntake(0.3);
  }

  public void stopIntake(){
    intakeRoller.stopMotor();
  }

  public void driveLifter(double input){
    liftMotor.set(ControlMode.PercentOutput, input);
  }

  public void liftToPos(double pos, double p){
    liftMotor.config_kP(0, p);
    liftMotor.set(ControlMode.Position, pos);
  }

  public void resetPos(){
    liftMotor.setSelectedSensorPosition(0.0);
  }

  public double getLifterCurrent(){
    return liftMotor.getSupplyCurrent();
  }

  public void stopLifter() {
    liftMotor.set(ControlMode.PercentOutput, 0);
  }

  public double getLiftMotorPosition(){
    return liftMotor.getSelectedSensorPosition();
  }

  public double getStartPosition(){
    return lifterStartPosition;
  }

  public void setStartPosition(double startPosition){
    this.lifterStartPosition = startPosition;
  }

  public double getLiftMotorVelocity(){
    return liftMotor.getSelectedSensorVelocity();
  }
  @Override
  public void periodic() {
    // if(){
      SmartDashboard.putNumber("Arm Position", getLiftMotorPosition());
      SmartDashboard.putNumber("Arm Start position", lifterStartPosition);
      SmartDashboard.putNumber("Lifter stator current", getLifterCurrent());
      SmartDashboard.putNumber("Lifter supply current", getLifterCurrent());
    // }
  }

}
