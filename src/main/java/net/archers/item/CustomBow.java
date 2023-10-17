package net.archers.item;

import net.minecraft.item.BowItem;
import net.projectile_damage.api.IProjectileWeapon;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CustomBow extends BowItem {
    // Instances are kept a list of, so model predicates can be automatically registered
    public static ArrayList<CustomBow> instances = new ArrayList<>();
    public int pullTime = 20;
    public float customVelocity = 0F;

    public CustomBow(Settings settings) {
        super(settings);
        instances.add(this);
    }

    public float getCustomPullProgress(int useTicks) {
        float pullTime = this.pullTime > 0 ? this.pullTime : 20;
        float f = (float)useTicks / pullTime;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    // MARK: Configuration
    public record Config(int pullTime, int damage, @Nullable Float velocity) { }
    public void configure(Config config) {
        this.pullTime = config.pullTime;
        if (config.velocity != null) {
            var value = config.velocity.doubleValue();
            this.customVelocity = config.velocity;
            ((IProjectileWeapon)this).setCustomLaunchVelocity(value);
        } else {
            this.customVelocity = 0F;
        }
        ((IProjectileWeapon)this).setProjectileDamage(config.damage);
    }
}
