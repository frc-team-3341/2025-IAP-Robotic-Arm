# 2025-IAP-Robotic-Arm
this color senor code's purpose is for the robots ability to be identifying different colors on a game field.
It applies the standard WPILib library to manage the robots hardware and software components.
the entire block of code explains a ColorSensor subsystem, which means it manages a single piece of the robots functionality.


this color sensor code's purpose is for the robots ability to be identifying different colors on a game field.

It applies the standard WPILib library to take care of the robot's hardware and software parts
the entire block of code explains a ColorSensor subsystem, which means it takes care of a single piece of the robot's functionality extending SubsystemBase:

public class ColorSensor extends SubsystemBase { 
}

The most important tool this system uses is the REV Robotics Color Sensor V3, which is connected to a specific I2C communication port on the robot’s main control hub

this is shown with these lines:

private final I2C.Port i2cPort = I2C.Port.kOnboard; private final 
ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

The code initializes this sensor and prepares a ColorMatch utility to compare readings in the future

private final ColorMatch m_colorMatcher = new ColorMatch();

specific colors like blue, green, red, and yellow are hardcoded with exact numerical recipes like the mixture of red, green, and blue light, so that the robot knows exactly what shade to look for 

For example, the red target is:

final Color kRedTarget = new Color(0.561, 0.232, 0.114);

then, the robot is supposed to save these target colors inside the ColorMatch tool in the constructor:

m_colorMatcher.addColorMatch(kBlueTarget); m_colorMatcher.addColorMatch(kRedTarget); 

the main action happens within the periodic() function, which acts like the sensor's heart, and runs many times a second.

in this loop, the color sensor gets the current color data from its surroundings:

Color detectedColor = m_colorSensor.getColor();

and then, it runs the new data through the ColorMatch tool to find the closest match of the color from the 4 target colors it was programmed to recognize (red, blue, green, yellow):

ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);


it also calculates a "confidence" score to indicate how sure the robot is of the match it made to the color.

The ColorMatchResult object named match also calculates a "confidence" score
(match.confidence) 
to figure out how confident the robot is of the color match it made.

The final important step here is communication. The program broadcasts all the data it collects to the SmartDashboard. 
The people using SmartDashboard can see the raw sensor numbers and the final "Detected Color" string, which helps them debug the robot or verify that the sensor is working correctly:

SmartDashboard.putNumber("Red", detectedColor.red); 
SmartDashboard.putNumber("Confidence", match.confidence); 
++SmartDashboard.putString("Detected Color", colorString);

the color senor code has been tested and it effectively gives the robot the ability to tell its us what it sees


## DriveToTarget Mark 1 (Nishk 12/5 - time of writing)
I wrote a command that finds gets the yaw of the AprilTag in order to tell the robot how much to turn. I use the PIDTurn method to figure out the motor output. Once the yaw is within +-5 degrees of 0, we change the variable "isAligned" to true to tell the command to stop and set the motors to 0. 
## DriveToTarget Mark 2 (Nishk 12/7 & 12/13)
The algorithm for DriveToTarget (turning aspect for now) should turn at a fixed power output to the motors and stop (60 deg) angle intervals wait for a few seconds, checking to see if an AprilTag is detected (yet to be implemented). We have to do this because the GSC can't detect the AprilTag while turning. Then, I have the ranging part of DriveToTarget that should get the distance away from the AprilTag and drive towards it using a PID controller and encoders (yet to be tested). 
