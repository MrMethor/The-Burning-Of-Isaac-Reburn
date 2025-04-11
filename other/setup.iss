; -- reburn.iss --
[Setup]
; A unique GUID for your app (you can generate one online or through Inno Setup)
AppId=7O7S7NsvR0mp0QA1NpnL-g
AppName=The Burning Of Isaac: Reburn
AppVersion=0.9
; Where it installs by default
DefaultDirName={pf}\The Burning of Isaac Reburn
; Where to put the built installer .exe
OutputDir=C:\Users\arnol\Downloads
; Name for the final installer file
OutputBaseFilename=setup
SetupIconFile=resource\icon.ico
; Optional: modern wizard style, if you like
WizardStyle=modern
DisableProgramGroupPage=yes

Uninstallable=yes
CreateUninstallRegKey=yes

; Change where the uninstaller goes:
UninstallFilesDir={commonappdata}\The Burning Of Isaac Reburn

; Basic settings
Compression=lzma2
SolidCompression=yes

[Files]
; 1) The main EXE
Source: "reburn.exe"; DestDir: "{app}"; Flags: ignoreversion

; 2) The entire lib folder
;    The Flags recursesubdirs and createallsubdirs copy entire folder structure
Source: "lib\*"; DestDir: "{app}\lib"; Flags: recursesubdirs createallsubdirs ignoreversion

; 3) The resource folder
Source: "resource\*"; DestDir: "{app}\resource"; Flags: recursesubdirs createallsubdirs ignoreversion

; 4) The Python file (only if you want to distribute it)
Source: "spriteSheetCoder.py"; DestDir: "{app}"

[Icons]
; Start Menu shortcut
Name: "{commonprograms}\The Burning of Isaac Reburn"; Filename: "{app}\reburn.exe"
; Optional Desktop shortcut
Name: "{commondesktop}\The Burning of Isaac Reburn"; Filename: "{app}\reburn.exe"; Tasks: desktopicon

[Tasks]
; Optional: user can choose whether to create a desktop icon
Name: "desktopicon"; Description: "Create a &desktop icon"; GroupDescription: "Additional icons:"