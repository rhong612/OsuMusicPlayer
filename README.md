# OsuMusicPlayer
A music player that extracts all songs from your osu! library. Simply select the Songs folder in your osu! directory and the application will handle the rest!

![Alt text](/screenshots/appScreenshot.png?raw=true "Screenshot of application")

# How does it work?
There are 2 ways to add songs to the music player.

### [Recommended]
If you select "Open Folder" and select the Songs folder in your osu! directory, the application will automatically obtain all the information it needs from your beatmap folders.

### [Not as ideal...but it still works]
If you select "Add File," you can select individual .mp3 files to add the player. When doing so, the application will search the directory containing the .mp3 file for a .osu file. If one is found (all osu! beatmaps have a .osu file so this should not be a problem), it will extract the song metadata and populate the list with that data.

### What if I want to add non-osu mp3 files?
The application will still add the .mp3 file even if a .osu file is not found, but there will be no image associated with the song and it will be missing information such as the artist's name. Keep in mind that this application is developed with osu! beatmaps in mind.

# Why should I care?
Obviously, you could just grab all the mp3 files yourself. However, this application saves you all the work. In addition, osu beatmaps tend to have lots of auxiliary sound effects. All of these sound files will be ignored - leaving the background songs from your favorite beatmaps in a neat sortable list. 

# How do I build it?
No one has time for that, so you can download it here:
https://www.dropbox.com/s/ol2chntsy4hzkyo/OsuMusicPlayer.jar?dl=0
