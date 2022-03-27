package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalebFalcon extends WPI_TalonFX {
  private boolean reversed;
  private double temp0;
  private int canID;
  public CalebFalcon(int deviceNumber, String canbus, boolean reversed) {
    super(deviceNumber, canbus);
    this.reversed = reversed;
    this.canID = deviceNumber;
  }

  public CalebFalcon(int deviceNumber, boolean reversed) {
    super(deviceNumber);
    this.reversed = reversed;
    this.canID = deviceNumber;
  }

  public double getDistance(){
    double distance;
    if(reversed){
      distance =  -super.getSelectedSensorPosition() - temp0;
    }
    else {
      distance =  super.getSelectedSensorPosition() - temp0;
    }
    // SmartDashboard.putNumber("Motor Can ID " + canID, distance);
    return distance;
  }

  public void resetDistance(){
    if(reversed){
      temp0 = -super.getSelectedSensorPosition();
    }
    else{
      temp0 = super.getSelectedSensorPosition();
    }
  }

}