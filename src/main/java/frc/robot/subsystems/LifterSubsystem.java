package frc.robot.subsystems;

// import java.util.logging.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;



/**
 * Creates a new ShooterSubsystem.
 */
public class LifterSubsystem extends SubsystemBase {
  // private Logger logger = Logger.getLogger(this.getClass().getName());
  private CANSparkMax leftWinch = new CANSparkMax(Constants.ClimberMotorLeftCanID,
   com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
  private CANSparkMax rightWinch = new CANSparkMax(Constants.ClimberMotorRightCanID,
   com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);


  final static int ConfigTimeOut = 30;
  // private DoubleSolenoid solenoid = null;

  public LifterSubsystem() {
    // Wipe any prior motor settings
    // Set motor direction
  }

  public void lift(double input){
    leftWinch.set(input);
    rightWinch.set(input);
  }

  public void stop() {
    rightWinch.stopMotor();
    leftWinch.stopMotor();
  }

  @Override
  public void periodic() {
    // TODO: Update dashboard motor speed via NetworkTables
  }

}
