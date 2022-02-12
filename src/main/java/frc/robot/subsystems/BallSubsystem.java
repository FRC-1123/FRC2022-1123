package frc.robot.subsystems;

import java.util.logging.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMax.ControlType;



/**
 * Creates a new ShooterSubsystem.
 */
public class BallSubsystem extends SubsystemBase {
  private Logger logger = Logger.getLogger(this.getClass().getName());
  private TalonFX LiftMotor = new TalonFX(Constants.IntakeLifterMotorCanId);
  // logger.info("intake motor" + Constants.IntakeBallMotorCanID);
  private CANSparkMax intakeRoller = new CANSparkMax(Constants.IntakeBallMotorCanID,
    com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
  final static int ConfigTimeOut = 30;

  public BallSubsystem() {
    LiftMotor.setNeutralMode(NeutralMode.Brake);

    LiftMotor.configOpenloopRamp(0.2);
    LiftMotor.configClosedloopRamp(0.2);

  }
  /*
    input the voltage you want if use preset input 0
  */
  public void spinIntake(double setVoltage){
    if(setVoltage == 0){
      setVoltage = 0.3;
    }
    intakeRoller.set(setVoltage);
  }

  public void stopIntake(){
    intakeRoller.stopMotor();
  }

  public void lift(double input){
    LiftMotor.set(ControlMode.PercentOutput, input);
  }

  public void liftToPos(double pos, double p){
    LiftMotor.config_kP(0, p);
    LiftMotor.set(ControlMode.Position, pos);
  }

  public void resetPos(){
    LiftMotor.setSelectedSensorPosition(0.0);
  }

  public void stopLifter() {
    LiftMotor.set(ControlMode.PercentOutput, 0);
  }

  public double getLiftMotorPosition(){
    return LiftMotor.getSelectedSensorPosition();
  }

  public double LifterStartPosition = getLiftMotorPosition();

  public double getStartPosition(){
    return LifterStartPosition;
  }
  @Override
  public void periodic() {
    // TODO: Update dashboard motor speed via NetworkTables
    SmartDashboard.putNumber("Arm Position", getLiftMotorPosition());
  }

}
