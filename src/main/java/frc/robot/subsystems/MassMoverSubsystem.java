package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import java.util.logging.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMax.IdleMode;


/**
 * Creates a new ShooterSubsystem.
 */
public class MassMoverSubsystem extends SubsystemBase {
  // private Logger logger = Logger.getLogger(this.getClass().getName());
  private TalonFX massMover = new TalonFX(Constants.massMoverCanID);
  private double massMoverStartPosition;
  private SendableChooser<Boolean> showValues;

  public MassMoverSubsystem() {
    // Wipe any prior motor settings
    // Set motor direction
    massMoverStartPosition = massMover.getSelectedSensorPosition();
    setName("Mass Mover Subsystem");
    showValues = new SendableChooser<Boolean>();
    showValues.setDefaultOption("false", false);
    showValues.addOption("true", true);
    SmartDashboard.putData("Mass Mover Subsystem show value", showValues);
    massMover.setNeutralMode(NeutralMode.Brake);
  }

  public void stopMassMover(){
    massMover.set(ControlMode.PercentOutput, 0);
  }

  public void runMassMover(double percent){
    massMover.set(ControlMode.PercentOutput, percent);
  }

  public double getMassMoverPosition() {
    return massMover.getSelectedSensorPosition();
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
      SmartDashboard.putNumber("mass mover position", massMover.getSelectedSensorPosition());
      SmartDashboard.putNumber("mass mover velocity", massMover.getSelectedSensorVelocity());
  }
}
