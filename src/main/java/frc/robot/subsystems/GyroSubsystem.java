package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import java.util.logging.Logger;

import com.kauailabs.navx.frc.AHRS;

public class GyroSubsystem extends SubsystemBase {
  // private final Logger logger = Logger.getLogger(this.getClass().getName());

  AHRS Gyro = new AHRS();

  public GyroSubsystem() {
    }

  public double getAngle() {
    return Gyro.getAngle();
  }
  
  @Override
  public void periodic() {
   	// This method will be called once per scheduler run
    SmartDashboard.putNumber("Gyro Angle", Gyro.getAngle());
  }

  public void calibrateGyro(){
    Gyro.reset();
  }
}