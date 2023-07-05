# 2nd_Year_Project_Animal_Spot_Detection
In this project I had to implement a program in Java that, given an image of a cheetah, recognise and count the number of spots on the cheetah.

# HOW TO RUN GUI

In order to implement use the following command line arguments

_java Animal   `mode` `filename ` `version`_

_**for the usage of GUI mode should be 6.**_

whereby,

- mode = The mode(which is always 6)
- filename = The name of the input picture
- version = The version you want the gui to run from.i.e see the table below for version

					

| version | GUI mode |
| ------ | ------ |
| 0 | grey-scale |
| 1 | Noise reduction |
| 2 | Edge detection |

example:

_java Animal `6` `cheetah1.jpeg` `0`_

- the above example will run a GUI for grey-scale.


 **PLEASE NOTE SPECIAL CASE!!**
- The command line inputs for edge detection are different.
- To run GUI for Edge detection please see the format below.

_java Animal `mode` `filename` `version` `epsilon value`_

example:

_java Animal `6` `cheetah1.jpeg` `2` `50`_

In this case 
- mode==6
- filename==cheetah1.jpeg
- version==2
- epsilon==50


> Author Takunda Charles Mudima: 23969156
