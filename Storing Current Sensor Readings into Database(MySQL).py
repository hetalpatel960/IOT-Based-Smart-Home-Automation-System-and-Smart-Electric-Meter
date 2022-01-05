import MySQLdb as sql
import serial #Serial imported for Serial communication
import time #Required to use delay functions
 
ArduinoSerial = serial.Serial('com8',9600) #Create Serial port object called arduinoSerialData
time.sleep(2) #wait for 2 secounds for the communication to get established

 #read the serial data and print it as line

while True:
    reading = ArduinoSerial.readline('AmpsRMS*100')
    print(reading)


#insert values
db = sql.connect("localhost:9090/abcd","root","root")

cursor = db.cursor()

query = ("insert into sensor values(CURDATE(), CURTIME(), %d ,240,(cur*volt));");" %reading

try:
   # Execute the SQL command
   cursor.execute(query)
   result = cursor.fetchall()
   # Commit your changes in the database
   db.commit()
except:
   # Rollback in case there is any error
   db.rollback()

db.close()
