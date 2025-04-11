[Setup]
AppId=7O7S7NsvR0mp0QA1NpnL-g
AppName=The Burning Of Isaac: Reburn
AppVersion=0.9
DefaultDirName={pf}\The Burning of Isaac Reburn
OutputDir=C:\Users\arnol\Downloads
OutputBaseFilename=setup
SetupIconFile=resource\icon.ico
WizardStyle=modern
DisableProgramGroupPage=yes
Uninstallable=yes
CreateUninstallRegKey=yes
UninstallFilesDir={commonappdata}\The Burning Of Isaac Reburn
Compression=lzma2
SolidCompression=yes

[Files]
Source: "reburn.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "lib\*"; DestDir: "{app}\lib"; Flags: recursesubdirs createallsubdirs ignoreversion
Source: "resource\*"; DestDir: "{app}\resource"; Flags: recursesubdirs createallsubdirs ignoreversion
Source: "spriteSheetCoder.py"; DestDir: "{app}"

[Icons]
Name: "{commonprograms}\The Burning of Isaac Reburn"; Filename: "{app}\reburn.exe"
Name: "{commondesktop}\The Burning of Isaac Reburn"; Filename: "{app}\reburn.exe"; Tasks: desktopicon

[Tasks]
Name: "desktopicon"; Description: "Create a &desktop icon"; GroupDescription: "Additional icons:"