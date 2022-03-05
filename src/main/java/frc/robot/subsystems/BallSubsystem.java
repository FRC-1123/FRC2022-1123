package frc.robot.subsystems;

// import java.util.logging.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.SendableWrapperClasses.*;
// import com.revrobotics.CANSparkMax.ControlType;


public class BallSubsystem extends SubsystemBase {
  // private Logger logger = Logger.getLogger(this.getClass().getName());
  // private CANSparkMax intakeRoller = new CANSparkMax(Constants.intakeBallMotorCanID,MotorType.kBrushless);
  private SendableSparkMax intakeRoller = new SendableSparkMax(Constants.intakeBallMotorCanID,MotorType.kBrushless);
  private RelativeEncoder rollerEncoder = intakeRoller.getEncoder();
  SendableChooser<Boolean> showValues;


  public BallSubsystem() {
    intakeRoller.setIdleMode(CANSparkMax.IdleMode.kCoast);
    setName("Ball Subsystem");
    // addChild("Intake Roller", intakeRoller);
    showValues = new SendableChooser<Boolean>();
    showValues.setDefaultOption("false", false);
    showValues.addOption("true", true);
    SmartDashboard.putData("Ball Subsystem show value", showValues);
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

  @Override
  public void periodic() {
    if(showValues.getSelected()){
      updateSmartDashboard();
    }
  }

  public void updateSmartDashboard(){
    SmartDashboard.putNumber("Intake Roller Speed", rollerEncoder.getVelocity()); 
  }

}
