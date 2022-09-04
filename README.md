# MSNotepad
Give a :star: if you *like it!*<br>
<br>
![Screenshot of MSNotepad ](/res/.readme/msnotepad_home.png)

<br>

## Overview :
MSNotepad is a simple text editor, with cross platform support, and a basic text-editing program which enables computer users to create documents. It is light-weight editor which entirely looks like *Microsoft Windows' Notepad*. Moreover, it is a beginner-friendly java swing project, with proper documentation and directory structure.

### Font Window :
![Screenshot of Font Window ](/res/.readme/msnotepad_font.png)

This font window is made up with `JDialog` using `GroupLayout` as the `LayoutManager`. And this part is handled by the `FontDialog` class, which is located at `src/msnotepad/gui/helper/FontDialog.java`.

### Find And Replace Window :
![Screenshot of Find Window ](/res/.readme/msnotepad_find.png)
![Screenshot of Replace Window ](/res/.readme/msnotepad_replace.png)

Both these windows are made up using `GridBagLayout` as the `LayoutManager`. And these dialogs are handled by the `FindAndRepaceDialog` class, which is located at `src/msnotepad/gui/helper/FindAndReplaceDialog.java`.

### About Window :
![Screenshot of About Window ](/res/.readme/msnotepad_about.png)

There is an interesting fact about this window/dialog. This Window is made up with a HTML file, named as `AboutDialog.html`. And this html code rendered by `JLabel` String renderer and initially formated and handled by the `AboutDialog` class, which is located at `src/msnotepad/gui/helper/AboutDialog.java`.

### Settings File :
```css
Show-StatusBar : true
Wrap-The-Line : false
Frame-Width : 600
Frame-Height : 450
Font-Family : Dialog
Font-Style : 0
Font-Size : 14
Opened-File-Name : Untitled
Opened-File-Path : null
Caret-Position : 0
```

MSNotepad writes the settings of the application in the `Settings.txt` file, before closing the application. And load it again at the starting of the application. If MSNotepad not find any `Settings.txt` file (or this file is corrupted), then, the default settings will work as shown in the image above.
  <br>
<br>
<br>

---
## Technical Details :

- **Full Setup Size :** 2.21 MB
- **Programming Language :** Java (JavaSE)
- **Framework Used :** AWT and Swing
  <br>
  <br>
<br>



---
## System Requirements :

- **Operating System :** Any (Platform Independent)
- **JRE version :** 1.8.0 or higher
- **Development Environment :** VS Code, Eclipse, IntelliJ IDEA
  <br>
  <br>
<br>

---
## Downloads : 
#### 1. Entire Repository
https://github.com/mohitsainiknl/MSNotepad/archive/refs/heads/master.zip
<br>

#### 2. JAR File (Executable)
https://github.com/mohitsainiknl/MSNotepad/blob/master/publish/MSNotepad.jar
<br>

#### 3. .exe File for Windows (not independent, require JRE to run)
https://github.com/mohitsainiknl/MSNotepad/blob/master/publish/MSNotepad.exe
<br>
<br>
<br>


---
**"Suggestions and project Improvements are Invited!"** <br>
Thanks a lot <br>
Mohit Saini
