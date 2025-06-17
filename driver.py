
import sys
import os
import shutil
import subprocess

def main():
    if len(sys.argv) < 2:
        print("missing mode: `process` or `splice`")
        exit(1)
    mode = sys.argv[1].lower()

    if mode == "process":
        main_process(sys.argv[2:])
    elif mode == "splice":
        main_splice(sys.argv[2:])
    else:
        output_file = main_process(sys.argv[1:])
        if not output_file.endswith(".mp4"):
            actual_output_file = output_file + ".mp4"
        else:
            actual_output_file = output_file
        main_splice([sys.argv[1], output_file, "-o", actual_output_file])

def main_process(args: list[str]) -> str:
    if len(args) < 1:
        print("missing input file name")
        exit(1)
    input_file = args[0]
    output_file = "timestamps.txt"
    crop_x = 0
    crop_y = 0
    crop_w = 0
    crop_h = 0
    heart_thres = 130
    heart_pixel_thres = 50
    skip_btn_thres = 120
    skip_btn_pixel_thres = 75
    min_interval = 10
    streak = 15
    flag = ""
    for arg in args[1:]:
        if flag == "--crop":
            try:
                parts = [int(x.strip()) for x in arg.split(",") ]
                if len(parts) != 4:
                    raise ValueError("invalid format for --crop")
            except:
                print("invalid format for --crop, should be `x,y,width,height`")
                exit(1)
            crop_x = parts[0]
            crop_y = parts[1]
            crop_w = parts[2]
            crop_h = parts[3]
        elif flag == "--heart-thresholds":
            try:
                parts = [int(x.strip()) for x in arg.split(",") ]
                if len(parts) != 2:
                    raise ValueError("invalid format for --heart-thresholds")
            except:
                print("invalid format for --heart-thresholds, should be `pixel,count`")
                exit(1)
            heart_pixel_thres = parts[0]
            heart_thres = parts[1]
        elif flag == "--skip-button-threshold":
            try:
                parts = [int(x.strip()) for x in arg.split(",") ]
                if len(parts) != 2:
                    raise ValueError("invalid format for --skip-button-threshold")
            except:
                print("invalid format for --skip-button-threshold, should be `pixel,count`")
                exit(1)
            skip_btn_pixel_thres = parts[0]
            skip_btn_thres = parts[1]
        elif flag == "-o":
            output_file = arg
        elif flag == "--min-interval":
            try:
                min_interval = int(arg)
            except:
                print("invalid format for --min-interval. should be number of frames")
                exit(1)
        elif flag == "--streak":
            try:
                streak = int(arg)
            except:
                print("invalid format for --streak. should be number of frames")
                exit(1)
        
        if flag:
            flag = ""
        else:
            flag = arg
        
    if crop_w == 0 or crop_h == 0:
        if crop_w != 0 or crop_h != 0 or crop_x != 0 or crop_y != 0:
            print("invalid crop bounds. both width and height should be non-zero, or omit --crop to use the full screen")
            exit(1)

    input_file = os.path.abspath(input_file)
    if not os.path.exists(input_file):
        print(f"input video file doesn't exist: {input_file}")
        exit(1)
    print(f"input: {input_file}")

    if crop_w == 0 or crop_h == 0:
        crop_w, crop_h = find_video_wh(input_file)
    if not crop_w or not crop_h:
        print(f"failed to determine video width or height")
        exit(1)
    print(f"crop area: {crop_x=} {crop_y=} {crop_w=} {crop_h=}")

    output_file = os.path.abspath(output_file)
    print(f"output: {output_file}")

    print(f"{min_interval=} {streak=}")

    java = shutil.which("java")
    if not java:
        print("`java` not found, please make sure you have java installed")
        exit(1)

    run_jar = os.path.join(os.path.dirname(__file__), "botw-gameplay-classifier", "run.jar")
    lib_path = os.path.join(os.path.dirname(__file__), "botw-gameplay-classifier", "lib", "opencv", "java", "x64")
    try:
        os.makedirs(os.path.dirname(output_file), exist_ok=True)
        subprocess.check_call([java, f"-Djava.library.path={lib_path}", "-jar", run_jar, 
                            input_file, str(crop_x), str(crop_y), str(crop_w), str(crop_h),
                            str(heart_thres), str(skip_btn_thres),
                            str(heart_pixel_thres), str(skip_btn_pixel_thres),
                            str(min_interval), str(streak),
                            output_file])
        print("done")
    except:
        print("command failed")
        exit(1)

    return output_file

def find_video_wh(video_file):
    import cv2
    cap = cv2.VideoCapture(video_file)

    if not cap.isOpened():
        print(f"error: failed to open video file: {video_file}")
        return None, None

    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))

    cap.release()
    return width, height

def main_splice(args: list[str]):
    if len(args) < 1:
        print("missing input video file name")
        exit(1)
    input_video_file = args[0]
    if len(args) < 2:
        print("missing input timestamp file name")
        exit(1)
    input_timestamp_file = args[1]
    output_file = ""

    flag = ""
    for arg in args[2:]:
        if flag == "-o":
            output_file = arg

        if flag:
            flag = ""
        else:
            flag = arg

    if not output_file:
        print("please specify a output file with -o OUTPUT_FILE")
        exit(1)
    
    input_video_file = os.path.abspath(input_video_file)
    print(f"input video: {input_video_file}")
    input_timestamp_file = os.path.abspath(input_timestamp_file)
    print(f"input timestamps: {input_timestamp_file}")
    output_file = os.path.abspath(output_file)
    print(f"output: {output_file}")

    with open(input_timestamp_file, "r", encoding="utf-8") as f:
        clip_ends = f.read().strip()
        if not clip_ends.startswith('[') or not clip_ends.endswith(']'):
            print("invalid timestamp file format")
            exit(1)
        clip_ends = [float(x.strip()) for x in clip_ends[1:-1].split(',')]
        if len(clip_ends) % 2 != 0:
            print("invalid timestamp file, it should have an even number of timestamps")
            exit(1)
    print("splicing the video...")
    splice_video(input_video_file, output_file, clip_ends)
    print("done")

def splice_video(input_video, output_video, clip_ends: list):
    from moviepy import VideoFileClip, concatenate_videoclips

    clips = []
    orig_vid = VideoFileClip(input_video)
    print()
    for i in range(0, len(clip_ends), 2):
        start = clip_ends[i]
        end = clip_ends[i+1]
        print(f'\ropening clip {i//2 + 1} of {len(clip_ends)//2}: {start} --> {end}')
        clip = orig_vid.subclipped(start, end)
        clips.append(clip)
    print()

    out_vid = concatenate_videoclips(clips)
    os.makedirs(os.path.dirname(output_video), exist_ok=True)
    out_vid.write_videofile(output_video, fps=30, threads=8, preset="veryfast")

    # cleanup
    for clip in clips:
        clip.close()
    out_vid.close()
    
if __name__ == "__main__":
    main()