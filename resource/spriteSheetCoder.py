from PIL import Image, PngImagePlugin

while True:
    text = input("Put the spritesheet into the spritesheet folder and write it's name (without .png)\n")
    img = Image.open("spritesheets/" + text + ".png")
    size: str = ""
    while size == "":
        size = input("Sprite size: ")
    metadata = PngImagePlugin.PngInfo()
    metadata.add_text("SpriteSize", size + "\0")
    img.save("spritesheets/" + text + ".png", pnginfo=metadata)

    input("Success")