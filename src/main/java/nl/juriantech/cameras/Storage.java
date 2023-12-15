package nl.juriantech.cameras;

import dev.dejvokep.boostedyaml.YamlDocument;
import nl.juriantech.cameras.objects.Camera;
import nl.juriantech.cameras.objects.SimpleLocation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Storage {

    private final Main plugin;
    private final YamlDocument dataFile;
    private final ArrayList<Camera> cameraArrayList;

    public Storage(Main plugin) {
        this.plugin = plugin;
        try {
            this.dataFile = YamlDocument.create(new File(plugin.getDataFolder(), "data.yml"), plugin.getResource("data.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.cameraArrayList = new ArrayList<Camera>();
    }

    public void save() {
        for (Camera camera : cameraArrayList) {
            dataFile.set(camera.getId().toString() + ".name", camera.getName());
            dataFile.set(camera.getId().toString() + ".owner", camera.getOwner().toString());
            dataFile.set(camera.getId().toString() + ".loc", camera.getSimpleLocation().toString());
        }

        try {
            dataFile.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        for (String route : dataFile.getRoutesAsStrings(false)) {
            cameraArrayList.add(new Camera(UUID.fromString(route), dataFile.getString(route + ".name"), UUID.fromString(dataFile.getString(route + ".owner")), SimpleLocation.fromString(dataFile.getString(route + ".loc"))));
        }
    }
    public ArrayList<Camera> getCameras() {
        return cameraArrayList;
    }
}
