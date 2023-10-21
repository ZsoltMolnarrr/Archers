package net.archers.item.weapon;

import net.archers.config.RangedConfig;
import net.minecraft.item.BowItem;
import net.projectile_damage.api.IProjectileWeapon;

public interface CustomRangedWeaponProperties {
    int getCustomPullTime_RPGS();
    void setCustomPullTime_RPGS(int pullTime);
    float getCustomVelocity_RPGS();
    void setCustomVelocity_RPGS(float velocity);
    float getCustomPullProgress(int useTicks);

    default void configure(RangedConfig config) {
        var rangedWeapon = (CustomRangedWeaponProperties)this;
        rangedWeapon.setCustomPullTime_RPGS(config.pullTime());
        if (config.velocity() != null) {
            float velocity = config.velocity();
            rangedWeapon.setCustomVelocity_RPGS(velocity);
            ((IProjectileWeapon)this).setCustomLaunchVelocity((double) velocity);
        } else {
            rangedWeapon.setCustomVelocity_RPGS(0F);
            ((IProjectileWeapon)this).setCustomLaunchVelocity(null);
        }
        ((IProjectileWeapon)this).setProjectileDamage(config.damage());
    }
}
