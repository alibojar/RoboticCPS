#Robotic teamwork using LEGO NXT robots in AgentSpeak/Jason

This is an implementation of a robotic rescue scenario where a rescuer robot searches for injured people which are shown by colour papers and informs the two medic/doctor robots of their location.

The medic/doctor robots attend an injured, spend a few seconds with her and move to the next injured person. Should an injured person with more severe injure is found in the meanwhile by the rescuer, the medic will leave to attend her.

The LeJOS folder contains the code that runs on the rescuer and doctor/medic robots. The AgentSpeak folder contains the code for robotic teamwork and the middle-ware that connects AgentSpeak and LeJOS though bluetooth.

This [video](http://vimeo.com/42409674) demonstrates the code in action.
