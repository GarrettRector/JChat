# JChat
A simple free messaging client
Built on the Java language, it is easy to run and bypasses proxy's and web filters. It was mainly created as a communication protocol for me and friends at school where many platforms are blocked, but can be used by anyone

# Points of consideration:
1. All files for the application are small, everything if not most of the handling is done by the server. More customization is avaliable with the server version
2. If you want to download and use the program, DO NOT USE SERVER FILES. These are for if you want to run the application on a server, and then connect to it from the client version
3. All files are also server based. Client side files will be stored as a .exe, but are not avaliable right now.
4. Look to releases for official JAR files with required libraries. The source is stored for editing for mainly friends who are also working on the project with me

## Required Libraries:
  1. [Apache Commons CSV Parsing](https://commons.apache.org/proper/commons-csv/download_csv.cgi)
  2. [Apache Commons String Parsing](https://commons.apache.org/proper/commons-lang/download_lang.cgi)

# Commands
The valid commands are right now:

`login, Username, Password`

`msg, User Message, Content`

`join, Groupchat name`

`leave, Groupchat name`

`quit, logoff or logout`

Do note that the commas are not part of the commands, they just signify the seperation between terms.
