#include <SoftwareSerial.h> //Including the software serial library
#define DEBUG true
SoftwareSerial esp8266(2,3); // This will make the Arduino pin 2 as the RX pin and Arduino pin 3 as the TX. Software UART

/* So you have to connect the TX of the esp8266 to the pin 2 of the Arduino and the TX of the
esp8266 to the pin 3 of the Arduino. This means that you need to connect the TX line from the esp
to the Arduino's pin 2 */
const int sensorIn = A0;
int mVperAmp = 185; // use 100 for 20A Module and 66 for 30A Module
double Voltage = 0;
double VRMS = 0;
double AmpsRMS = 0;
void setup()
{
 Serial.begin(9600); // Setting the baudrate to 9600
 esp8266.begin(9600); // Set it according to your esp�s baudrate. Different esp�s have different baud rates.
 pinMode(11,OUTPUT); // Setting the pin 11 as the output pin.
 digitalWrite(11,LOW); // Making it low.
 pinMode(12,OUTPUT); // Setting the pin 12 as the output pin..
 digitalWrite(12,LOW); // Making pin 12 low.
 pinMode(13,OUTPUT); // Setting the pin 13 as the output pin.
 digitalWrite(13,LOW); // Making pin 13 low.
 
 sendData("AT+RST\r\n",2000,DEBUG); //This command will reset module to default
 sendData("AT+CWMODE=2\r\n",1000,DEBUG); // This will configure the mode as access point
 sendData("AT+CIFSR\r\n",1000,DEBUG); // This will get ip address and will show it
 sendData("AT+CIPMUX=1\r\n",1000,DEBUG); // This will configure the ESP8266 for multiple connections
 sendData("AT+CIPSERVER=1,80\r\n",1000,DEBUG); // This will set the server on port 80
}
void loop()
{
 if(esp8266.available()) // Checking that whether the esp8266 is sending a message or not (Software UART Data)
 {
 if(esp8266.find("+IPD,"))
 {
 delay(1000); // Waiting for 1 sec
 int connectionId = esp8266.read()-48; // Subtracting 48 from the character to get the number.
 esp8266.find("pin="); // Advancing the cursor to the "pin="
 int pinNumber = (esp8266.read()-48)*10; // Getting the first number which is pin 13
 pinNumber += (esp8266.read()-48); // This will get the second number. For example, if the pin number is 13 then the 2nd number will be 3 and then add it to the first number
 digitalWrite(pinNumber, !digitalRead(pinNumber)); // This will toggle the pin
 // The following commands will close the connection
 String closeCommand = "AT+CIPCLOSE=";
 closeCommand+=connectionId;
 closeCommand+="\r\n";
 sendData(closeCommand,1000,DEBUG); // Sending the data to the ESP8266 to close the command
 }
 }
Voltage = getVPP();
 VRMS = (Voltage/2.0) *0.707; //root 2 is 0.707
 AmpsRMS = (VRMS * 1000)/mVperAmp;
 Serial.print(AmpsRMS);
 Serial.println(" Amps RMS");
}
String sendData(String command, const int timeout, boolean debug) // Function to send the data to the esp8266
{
 String response = "";
 esp8266.print(command); // Send the command to the ESP8266
 long int time = millis();
 while( (time+timeout) > millis()) // ESP8266 will wait for some time for the data to receive
 {
 while(esp8266.available()) // Checking whether ESP8266 has received the data or not
 {
 char c = esp8266.read(); // Read the next character.
 response+=c; // Storing the response from the ESP8266
 }
 }
 if(debug)
 {
 Serial.print(response); // Printing the response of the ESP8266 on the serial monitor.
 }
 return response;
}
float getVPP()
{
 float result;
 int readValue; //value read from the sensor
 int maxValue = 0; // store max value here
 int minValue = 1024; // store min value here

 uint32_t start_time = millis();
 while((millis()-start_time) < 1000) //sample for 1 Sec
 {
 readValue = analogRead(sensorIn);
 // see if you have a new maxValue
 if (readValue > maxValue)
 {
 /*record the maximum sensor value*/
 maxValue = readValue;
 }
 if (readValue < minValue)
 {
 /*record the minimum sensor value*/
 minValue = readValue;
 }
 }

 // Subtract min from max
 result = ((maxValue - minValue) * 5.0)/1024.0;

 return result;
 }
