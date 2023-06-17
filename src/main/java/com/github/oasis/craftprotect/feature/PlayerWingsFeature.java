package com.github.oasis.craftprotect.feature;

import com.github.oasis.craftprotect.CraftProtectPlugin;
import com.github.oasis.craftprotect.api.Feature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class PlayerWingsFeature implements Feature {

    @Inject
    private CraftProtectPlugin plugin;
    private List<Vector> vectors = new ArrayList<>();

    public PlayerWingsFeature() throws IOException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/hat.obj");
        if (resourceAsStream != null) {
            this.vectors = loadModel(resourceAsStream, 1f);
//                    0.05f);
        }
    }


    public static List<Vector> loadModel(InputStream file, float scale) throws IOException {

        try (InputStream inputStream = file) {
            Obj obj = ObjReader.read(inputStream);
            Obj normalizedObj = ObjUtils.convertToRenderable(obj);
            List<Vector> vertices = new ArrayList<>();

            // Create a Set to store unique indices of surface vertices
            Set<Integer> surfaceVertexIndices = new HashSet<>();

            // Iterate over all faces and add vertex indices to the Set
            for (int i = 0; i < normalizedObj.getNumFaces(); i++) {
                for (int j = 0; j < normalizedObj.getFace(i).getNumVertices(); j++) {
                    surfaceVertexIndices.add(normalizedObj.getFace(i).getVertexIndex(j));
                }
            }

            // Iterate over the unique surface vertex indices and add the corresponding vertices to the List
            for (int index : surfaceVertexIndices) {
                float x = normalizedObj.getVertex(index).getX() * scale;
                float y = normalizedObj.getVertex(index).getY() * scale;
                float z = normalizedObj.getVertex(index).getZ() * scale;
                vertices.add(new Vector(x, y, z));
            }

            return vertices;
        }
    }

/*    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        plugin.attachAsyncRepeaterTask(event.getPlayer(), "wings", () -> {
            float rotation = event.getPlayer().getLocation().getYaw();
            try {
                Object handle = event.getPlayer().getClass().getMethod("getHandle").invoke(event.getPlayer());
                rotation = (float) handle.getClass().getField("aV").get(handle);
            } catch (Exception ignored) {
                System.out.println("Not found");
            }

            double radians = Math.toRadians(rotation);

            double cos = Math.cos(radians);
            double sin = Math.sin(radians);

            Vector directionVector = new Vector();
            //directionVector.setX(-Math.sin(Math.toRadians(rotation)));
            //directionVector.setZ(Math.cos(Math.toRadians(rotation)));
            //directionVector = directionVector.multiply(0.2F);
            directionVector.setY(-0.8);

            Location clone = event.getPlayer().getEyeLocation().clone().subtract(directionVector);

            Particle.DustOptions options = new Particle.DustOptions(Color.PURPLE, 0.5f);

            for (Vector vector : vectors) {
                Vector rotatedVector = new Vector(cos * vector.getX() - sin * vector.getZ(), vector.getY(), sin * vector.getX() + cos * vector.getZ());
                Location add = clone.clone().add(rotatedVector);
                add.getWorld().spawnParticle(Particle.REDSTONE, add, 1, 0, 0, 0, 0, options);
            }


        }, 5, 5);

    }*/

    @Override
    public void close() throws IOException {

    }
}
