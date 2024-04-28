# CloudFlare DDNS Program In Java

TLDR: This is a program that could change your record on cloudflare content with the external IP. Useful when you needs keep contact with your computer at home using web domain.

# You'll need:

- A windows/Linux/Macos with java runtime.
- Some java knowledge(or read below to get that)
- Internet
- Cloud Flare API key with DNS modify permission

# Usage
1. Download the jar or build from source yourself, and put it in a folder where there is no other  thing. Because it will produce logs and settings file.
2. Use  
```java -jar .\CloudflareDDNS.jar <yourKey>```
3. If this is the first time you launching it, it will ask you to provide the dns zone-id. Just type in and enter.
4. If this is the first time you launching it, two folder will appear, a log folder and a config folder. You need to edit the config file inside the config folder, to set the records' ``isControlling`` that you wanna control to true.
5. When the program runs again. It will get the external IP by access IP check server, and put it to the record content, if you marked this record.
6.  Set up a windows job or crontab or else(I use jenkins, weird huh?)

# Notice
**DO NOT** put this program behind a proxy, otherwise you will get your record point towards your proxy server.

# FAQ 

 - Q: Still don't know what is this for.
 - A: In my home I got a server setup with some game server running, and a IPv4 to let me access it. However, this ip may change with the time or afther the modem restarted. Then I might lost contact with my server. But if I got this program on server, it will automatically change the dns records on cloud flare when there is a ip change. Make sure the domain will always point at the server whether if the IP changed or not.

- Q: Where do I get my CF key
- A: https://dash.cloudflare.com/profile It's on the left. **Protect it because it's somekind of password to your CF account!!**
- Q: Zone-id?
- A: Click the domain on your dashboard, it's at the right-below the screen.
- Q: Great program, I'll go support you with money
- A: *This is not a answer but delusions.
