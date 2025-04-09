package tboir.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourceManager {

    private final HashMap<String, SpriteSheet> spriteSheets;
    private final HashMap<String, Texture> textures;
    private final HashMap<String, AnimationSheet> animationSheets;

    public ResourceManager() {
        this.spriteSheets = new HashMap<>();
        this.textures = new HashMap<>();
        this.animationSheets = new HashMap<>();
        this.loadResources();
    }

    private void loadResources() {
        this.loadSpriteSheets();
        this.loadTextures();
        this.loadAnimationSheets();
    }

    private void loadSpriteSheets() {
        File folder = new File("resource/spritesheets");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                int size = this.extractSpriteSize(file);
                if (size > 0) {
                    String sheetName = file.getName().replaceAll(".png", "");
                    this.spriteSheets.put(sheetName, new SpriteSheet(this.loadImage("resource/spritesheets/" + sheetName + ".png"), this.extractSpriteSize(file)));
                } else {
                    System.out.println("Couldn't find sprite size in file: " + file.getName());
                }
            }
        }
    }

    private void loadTextures() {
        File folder = new File("resource/textures");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String textureName = file.getName().replaceAll(".png", "");
                this.textures.put(textureName, new Texture(this.loadImage("resource/textures/" + textureName + ".png")));
            }
        }
    }

    private void loadAnimationSheets() {
        File folder = new File("resource/animationsheets");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                int size = this.extractSpriteSize(file);
                if (size > 0) {
                    String sheetName = file.getName().replaceAll(".png", "");
                    this.animationSheets.put(sheetName, new AnimationSheet(this.loadImage("resource/animationsheets/" + sheetName + ".png"), this.extractSpriteSize(file)));
                } else {
                    System.out.println("Couldn't find animation size in file: " + file.getName());
                }
            }
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + path, e);
        }
    }

    private int extractSpriteSize(File spriteSheet) {
        try {
            int size = 0;
            byte[] fileContent = Files.readAllBytes(spriteSheet.toPath());
            int maxDigits = 4;
            int firstIndex = 0x34;
            ArrayList<Character> numList = new ArrayList<>();
            while (fileContent[firstIndex] != '\0' && firstIndex < firstIndex + maxDigits) {
                if ((char)fileContent[firstIndex] > 47 && (char)fileContent[firstIndex] < 58 ) {
                    numList.addFirst((char)fileContent[firstIndex]);
                    firstIndex++;
                } else {
                    return 0;
                }
            }
            for (int i = 0; i < numList.size(); i++) {
                size += Integer.parseInt(numList.get(i).toString()) * (int)Math.pow(10, i);
            }
            return size;
        } catch (IOException e) {
            System.out.println("Error reading sprite sheet: " + spriteSheet.getName());
        }
        return 0;
    }

    public BufferedImage getSprite(String name, int column, int row) {
        if (this.spriteSheets.containsKey(name)) {
            return this.spriteSheets.get(name).getImage(column, row);
        }
        System.out.println("Couldn't find sprite: " + name);
        return null;
    }

    public BufferedImage getTexture(String name) {
        if (this.textures.containsKey(name)) {
            return this.textures.get(name).getImage();
        }
        return null;
    }

    public AnimationSheet getAnimation(String name) {
        if (this.animationSheets.containsKey(name)) {
            return this.animationSheets.get(name);
        }
        return null;
    }
}
