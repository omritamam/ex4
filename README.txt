omri.tamam
315564112

Each char gets brightness vale according to the formula in the instructions, the values are saved to a field called
brightnessChar[]. Each subImages gets a brightness value by getImageBrightness() method, matching the corresponding char
is made by matchChar() method by picking the char with the minimal distance (in abs) from the char's brightness. There
are 2 arrays I use - charSet and brightnessChars - which match each other.