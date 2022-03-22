package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import java.util.logging.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


/**
 * Creates a new ShooterSubsystem.
 */
public class FingerSubsystem extends SubsystemBase {
  // private Logger logger = Logger.getLogger(this.getClass().getName());
  private TalonFX finger = new TalonFX(Constants.FingerCanID);
  private double fingerStartPosition;
  private SendableChooser<Boolean> showValues;

  public FingerSubsystem() {
    // Wipe any prior motor settings
    // Set motor direction
    fingerStartPosition = finger.getSelectedSensorPosition();
    finger.setNeutralMode(NeutralMode.Brake);
    setName("Finger Subsystem");
    showValues = new SendableChooser<Boolean>();
    showValues.setDefaultOption("false", false);
    showValues.addOption("true", true);
    SmartDashboard.putData("Finger Subsystem show value", showValues);
  }

  public void stopFinger(){
    finger.set(ControlMode.PercentOutput, 0);
  }

  public void runFinger(double percent){
    finger.set(ControlMode.PercentOutput, percent);
  }

  public double getFingerPosition() {
    return finger.getSelectedSensorPosition();
  }

  public double getfingerStartPosition(){
    return fingerStartPosition;
  }

  @Override
  public void periodic() {
    if(showValues.getSelected()){
      updateSmartDashboard();
    }
  }

  public void updateSmartDashboard(){
      SmartDashboard.putNumber("mass mover position", finger.getSelectedSensorPosition());
      SmartDashboard.putNumber("mass mover velocity", finger.getSelectedSensorVelocity());
  }
}
