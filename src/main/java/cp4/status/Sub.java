package cp4.status;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.message.title.HTitle;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static cp4.status.listeners.prefixPlugin;


public class Sub implements CommandExecutor {

    private String subtitle = "TEST";

    public Sub(Main main) {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        Location location = player.getLocation();

        if (player.hasPermission("cp4.sub")) {
            player.sendMessage("Du leuchtest jetzt");
            player.setGlowing(true);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 0.5f);
            player.sendMessage(prefixPlugin + "§3Du leuchtest jetzt");

            Firework fireWork = location.getWorld().spawn(location, Firework.class);
            FireworkMeta fireworkMeta = fireWork.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).withTrail().withColor(Color.AQUA).build());
            fireworkMeta.setPower(1);
            fireWork.setFireworkMeta(fireworkMeta);

            //HTitle myTitle = new HTitle("Title", "Subtitle", 5, 3, 2); // created a title with title "Title" and subtitle "Subtitle" which appears in 3 ticks, disappears in 2 ticks so totally exists for 5 ticks
            //HCore.sendTitle(player, myTitle); // sent title to player


                double space = 0.2;
                double defX = location.getX() - (space * shape[0].length / 2) + space;
                double x = defX;
                double y = location.clone().getY() + 2;
                double angle = -((location.getYaw() + 180) / 60);
                angle += (location.getYaw() < -180 ? 3.25 : 2.985);

                for (boolean[] aShape : shape) {
                    for (boolean anAShape : aShape) {
                        if (anAShape) {

                            Location target = location.clone();
                            target.setX(x);
                            target.setY(y);

                            Vector v = target.toVector().subtract(location.toVector());
                            Vector v2 = getBackVector(location);
                            v = rotateAroundAxisY(v, angle);
                            v2.setY(0).multiply(-0.2);

                            location.add(v);
                            location.add(v2);
                            for (int k = 0; k < getModifiedAmount(3); k++) {
                                Particles.REDSTONE.display(255, 255, 255, location);
                            }
                            location.subtract(v2);
                            location.subtract(v);
                        }
                        x += space;
                    }
                    y -= space;
                    x = defX;
                }
            }

            public static Vector rotateAroundAxisY(Vector v, double angle) {
                double x, z, cos, sin;
                cos = Math.cos(angle);
                sin = Math.sin(angle);
                x = v.getX() * cos + v.getZ() * sin;
                z = v.getX() * -sin + v.getZ() * cos;
                return v.setX(x).setZ(z);
            }

            public static Vector getBackVector(Location loc) {
                final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90))));
                final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90))));
                return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
            }
        }




             //   }
           // }
        }
        else player.sendMessage(prefixPlugin + "§3Du musst Sub bei Oasis4_0 oder Oreocast sein");


            return true;


        }

    }
