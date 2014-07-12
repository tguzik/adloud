adloud
------
Simple application to measure perceived loudness of audio stream (typically advertisements) based 
on [ITU-R  BS.1770-3](http://www.itu.int/dms_pubrec/itu-r/rec/bs/R-REC-BS.1770-3-201208-I!!PDF-E.pdf) 
recommendation.

This application opens the default input line (typically microphone) and while reading the input, 
it measures the perceived loudness and compares it to an average from previous 20 seconds. If the 
difference between current peak value and the average is equal or greater than the user-supplied 
threshold, it saves the time and peak value.

The application is (was?) an old university project, features some horrible code and most likely 
won't be updated. However if you are still interested, you are welcome to tinker with it :-)

Swing "GUI" included - here's a screenshot:

![Screenshot of the application](http://i.imgur.com/2TVgkIv.png)
 

#### Additional software:

* Development: JDK1.7+
* Runtime: JRE1.7 (tested on Windows 7)

#### License
This application is licensed under GNU General Public License.
