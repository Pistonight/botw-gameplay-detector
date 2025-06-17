# botw-gameplay-detector

**This project is currently unmaintained**

It has some glue driver code to be functional

## Input Video Requirements
- The game play must have hearts unobstructed
- It should start in the middle of a cutscene, not game play
- If non-game footage is included (like home screen), it might be inaccurate

## Usage
You need Python 3 and Java >= 8 installed

For Java on Windows, try [Microsoft Build of OpenJDK](https://learn.microsoft.com/en-us/java/openjdk/download)

First clone or download this repository
```
git clone https://github.com/Pistonight/botw-gameplay-detector
```

Install the python dependencies:
```
pip install opencv-python moviepy
```

Run the driver script
```
python driver.py MODE ARGS...
```

There are 2 modes: `process` and `splice`
- `process` processes a video file and outputs timestamps to splice. **The first frame of the video must be not gameplay**
- `splice` takes a video file and timestampts and outputs spliced video

To produce a video file with cutscenes removed, the whole process is:
```
python driver.py process video.mp4 -o timestamps.txt [ARGS...]
python driver.py splice video.mp4 timestamps.txt -o video2.mp4
```

Or, if you don't specify a mode, it will do `process` then `splice` for you automatically
```
python driver.py video.mp4 -o output.mp4 [ARGS...]
```


Args for processing:
- `--crop x,y,width,height`: Specify bounds to crop the input video to only contain the game footage.   Default is full video screen
- `--heart-thresholds pixel,count`: Thresholds for matching heart container.
  The first number is pixel similarity and second number is how many pixels need to match. For both numbers, lower means stricter match. Default is `50,130`
- `--skip-button-thresholds pixel,count`: Similar to above but for matching skip button at the end of shrine cut scene. Default is `75,120`
- `--min-interval frames`: minimum number of frames to be considered a cutscene. Default is 10
- `--streak frames`: minimum number of frames to hit before considering it is or is not
  a cutscene. Default is 15

You might need to tune the thresholds depending on the video quality.
It's recommended to splice a short segment of the video for testing first,
then process the whole video. If the output includes unwanted cutscenes, lower the number.
If the output removed gameplay (mistakens gameplay for cutscene), make the number higher