# wifi-connect-poc
## Programmattically connect to a wifi access point

Super simple proof-of-concept to programattically change the wifi network to one with a given SSID
and password.  Then after 15 seconds, reconnect back to the original SSID, all with zero user input.

Edit `res/values/strings.xml` to contain your SSID and password.  Assumes a connection to a WPA-protected AP.
