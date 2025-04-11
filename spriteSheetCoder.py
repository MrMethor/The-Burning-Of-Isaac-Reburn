from PIL import Image, PngImagePlugin

while True:
    typePath = input("Animation or spritesheet? ")
    text = input("Name of the png file (without .png) ")
    
    path = ""
    if "a" in typePath:
        path = "animationsheets/"
    else:
        path = "spritesheets/"
    size: str = ""
    img = Image.open(path + text + ".png")
    while size == "":
        size = input("Sprite size: ")
    metadata = PngImagePlugin.PngInfo()
    metadata.add_text("SpriteSize", size + "\0")
    img.save(path + text + ".png", pnginfo=metadata)

    input("Success")