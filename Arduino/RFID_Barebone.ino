//Author: Johnatan Sona & Jonatan Fridsten
//Modify : Johnatan Sona
//Uses AddicoreRFID library for RFID-scanner
//
#include <AddicoreRFID.h>
#include <SPI.h>

#define	uchar	unsigned char
#define	uint	unsigned int

AddicoreRFID myRFID; // create AddicoreRFID object to control the RFID module

/////////////////////////////////////////////////////////////////////
//set the pins
/////////////////////////////////////////////////////////////////////
const int chipSelectPin = 10;
const int NRSTPD = 5;


//Maximum length of the array
#define MAX_LEN 16

void setup() {                
   Serial.begin(9600);                        // RFID reader SOUT pin connected to Serial RX pin at 9600bps 
 
  // start the SPI library:
  SPI.begin();
  
  pinMode(chipSelectPin,OUTPUT);              // Set digital pin 10 as OUTPUT to connect it to the RFID /ENABLE pin 
    digitalWrite(chipSelectPin, LOW);         // Activate the RFID reader
  pinMode(NRSTPD,OUTPUT);                     // Set digital pin 10 , Not Reset and Power-down
    digitalWrite(NRSTPD, HIGH);
  pinMode(7, OUTPUT);
  pinMode(8, OUTPUT);
  myRFID.AddicoreRFID_Init();  
}

void loop()
{
	uchar status;
        uchar str[MAX_LEN];
	//Find tags, return tag type
	status = myRFID.AddicoreRFID_Request(PICC_REQIDL, str);	

	//Anti-collision, return tag serial number 4 bytes
	status = myRFID.AddicoreRFID_Anticoll(str);
	if (status == MI_OK)
	{
    	    Serial.print(str[0], HEX);
           // Serial.print(" , ");
    	    Serial.print(str[1],HEX);
           // Serial.print(" , ");
    	    Serial.print(str[2],HEX);
           // Serial.print(" , ");
    	    Serial.print(str[3],HEX);
          Serial.println();
            digitalWrite(7, HIGH);
            digitalWrite(8, HIGH);
          delay(40);
            digitalWrite(7, LOW);
            digitalWrite(8, LOW);
          delay(40);
            digitalWrite(7, HIGH);
            digitalWrite(8, HIGH);
          delay(40);
            digitalWrite(7, LOW);
            digitalWrite(8, LOW);
          delay(6900);
	}
		
        myRFID.AddicoreRFID_Halt();		   //Command tag into hibernation              

}

