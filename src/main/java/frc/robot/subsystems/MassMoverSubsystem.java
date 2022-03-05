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
public class MassMoverSubsystem extends SubsystemBase {
  // private Logger logger = Logger.getLogger(this.getClass().getName());
  private CANSparkMax massMover = new CANSparkMax(Constants.massMoverCanID, MotorType.kBrushless);
  private RelativeEncoder massMoverEncoder = massMover.getEncoder();
  private double massMoverStartPosition;
  private SendableChooser<Boolean> showValues;

  public MassMoverSubsystem() {
    // Wipe any prior motor settings
    // Set motor direction
    massMoverStartPosition = massMoverEncoder.getPosition();
    setName("Mass Mover Subsystem");
    showValues = new SendableChooser<Boolean>();
    showValues.setDefaultOption("false", false);
    showValues.addOption("true", true);
    SmartDashboard.putData("Mass Mover Subsystem show value", showValues);
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
  }
}
