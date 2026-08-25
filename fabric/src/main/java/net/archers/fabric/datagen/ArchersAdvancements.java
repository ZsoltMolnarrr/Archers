package net.archers.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.VillagerTradeCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.misc.criteria.SpellCastCriteria;
import net.spell_engine.spellbinding.SpellBindingCriteria;
import net.spell_engine.spellbinding.SpellBookCreationCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Generates the Archer advancement tree using the standard {@link Advancement.Builder} API.
 * <p>
 * The advancements live in the shared {@code rpg_series} namespace (they are part of the RPG Series
 * progression UI) but their content is provided by the Archers mod. {@link #entries()} is the single
 * source of truth: this provider exports each built {@link AdvancementEntry}, and
 * {@code ArchersDataGenerator.LangGen} reads the same list for the
 * {@code advancements.rpg_series.<path>.title/description} translation keys, which are derived from the
 * advancement id (see {@link #translationKey}). Every advancement id therefore matches its translation
 * suffix — e.g. {@code trade_with_archery_artisan} rather than a separate file name and key.
 */
public class ArchersAdvancements extends FabricAdvancementProvider {
    public static final String NAMESPACE = "rpg_series";

    public ArchersAdvancements(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        for (var entry : entries()) {
            consumer.accept(entry.entry());
        }
    }

    /** A generated advancement paired with the plain-text strings behind its (id-derived) translation keys. */
    public record Entry(AdvancementEntry entry, String title, String description) {
        public String titleKey() { return translationKey(entry.id(), "title"); }
        public String descriptionKey() { return translationKey(entry.id(), "description"); }
    }

    static String translationKey(Identifier id, String suffix) {
        return "advancements." + id.getNamespace() + "." + id.getPath() + "." + suffix;
    }

    // MARK: Definitions

    private static List<Entry> entries;

    /** Lazily builds the advancement tree on first use (during data generation, when registries are ready). */
    public static List<Entry> entries() {
        if (entries != null) {
            return entries;
        }
        var list = new ArrayList<Entry>();

        // Class choice — create the Archery Manual
        list.add(task("path_choose_archer", "rpg_series:classes", spellBookIcon("archer"),
                "book", spellBookCreation("archers:spell_book/archer"),
                "Path of Archery", "Create as Archery Manual"));

        // Obtain the first archery skill — bind a single spell
        list.add(task("spell_novice_archer", "rpg_series:path_choose_archer", item("archers:composite_longbow"),
                "bind", spellBinding("archers:spell_book/archer", false),
                "Lethal Shots", "Obtain your first Archery skill"));

        // Complete the manual — finish binding
        list.add(challenge("spell_master_archer", "rpg_series:spell_novice_archer", item("archers:royal_longbow"),
                "cast", spellBinding("archers:spell_book/archer", true),
                "Ballistics Expert", "Complete the full Archery Manual"));

        // Cast a skill from the manual (silent — no toast or chat announcement)
        list.add(silent("spell_cast_archer_book", "rpg_series:spell_novice_archer", spellBookIcon("archer"),
                "cast", spellCast("#archers:spell_book/archer"),
                "Archery Training", "Use a skill from the Archery Manual"));

        // Trade with the Archery Artisan villager
        list.add(secret("trade_with_archery_artisan", "rpg_series:misc_items", item("minecraft:arrow"),
                "trade_with_archery_artisan", villagerTrade("archers:archery_artisan"),
                "Archery Sale", "Purchase an item from an Archery Artisan Villager"));

        entries = list;
        return entries;
    }

    // MARK: Builder shorthands

    /** Visible task: toast + chat announcement on. */
    private static Entry task(String idPath, String parent, ItemStack icon,
                              String criterionName, AdvancementCriterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementFrame.TASK, true, true, false, criterionName, criterion, title, description);
    }

    /** Challenge-framed task. */
    private static Entry challenge(String idPath, String parent, ItemStack icon,
                                   String criterionName, AdvancementCriterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementFrame.CHALLENGE, true, true, false, criterionName, criterion, title, description);
    }

    /** Task without toast or chat announcement. */
    private static Entry silent(String idPath, String parent, ItemStack icon,
                                String criterionName, AdvancementCriterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementFrame.TASK, false, false, false, criterionName, criterion, title, description);
    }

    /** Hidden task: not shown in the tree until earned. */
    private static Entry secret(String idPath, String parent, ItemStack icon,
                                String criterionName, AdvancementCriterion<?> criterion, String title, String description) {
        return advancement(idPath, parent, icon, AdvancementFrame.TASK, true, true, true, criterionName, criterion, title, description);
    }

    @SuppressWarnings("deprecation") // Advancement.Builder.parent(Identifier) is the only way to reference parents built outside this provider.
    private static Entry advancement(String idPath, String parent, ItemStack icon, AdvancementFrame frame,
                                     boolean showToast, boolean announceToChat, boolean hidden,
                                     String criterionName, AdvancementCriterion<?> criterion, String title, String description) {
        var id = Identifier.of(NAMESPACE, idPath);
        // The original data-pack advancements did not send telemetry events (vanilla default is off),
        // so keep them untelemetered rather than using the telemetered Advancement.Builder.create().
        var entry = Advancement.Builder.createUntelemetered()
                .parent(Identifier.of(parent))
                .display(
                        icon,
                        Text.translatable(translationKey(id, "title")),
                        Text.translatable(translationKey(id, "description")),
                        null,
                        frame,
                        showToast,
                        announceToChat,
                        hidden)
                .criterion(criterionName, criterion)
                .build(id);
        return new Entry(entry, title, description);
    }

    // MARK: Icon helpers

    private static ItemStack item(String itemId) {
        return new ItemStack(Registries.ITEM.get(Identifier.of(itemId)));
    }

    private static ItemStack spellBookIcon(String book) {
        var stack = new ItemStack(Registries.ITEM.get(Identifier.of("spell_engine", "spell_book")));
        stack.set(net.minecraft.component.DataComponentTypes.ITEM_MODEL, Identifier.of("archers", "spell_book/" + book)); // vanilla item-model definition = pool id (assets/archers/items/spell_book/<pool>.json)
        return stack;
    }

    // MARK: Criterion helpers

    private static AdvancementCriterion<?> spellBookCreation(String spellPool) {
        return SpellBookCreationCriteria.INSTANCE.create(
                new SpellBookCreationCriteria.Condition(Optional.empty(), Optional.of(spellPool)));
    }

    private static AdvancementCriterion<?> spellBinding(String spellPool, boolean complete) {
        return SpellBindingCriteria.INSTANCE.create(
                new SpellBindingCriteria.Condition(Optional.empty(), Optional.of(spellPool), Optional.of(complete)));
    }

    private static AdvancementCriterion<?> spellCast(String spell) {
        return SpellCastCriteria.INSTANCE.create(
                new SpellCastCriteria.Condition(Optional.empty(), Optional.of(spell), Optional.empty()));
    }

    private static AdvancementCriterion<?> villagerTrade(String profession) {
        var villagerData = new NbtCompound();
        villagerData.putString("profession", profession);
        var nbt = new NbtCompound();
        nbt.put("VillagerData", villagerData);
        var villager = EntityPredicate.contextPredicateFromEntityPredicate(
                EntityPredicate.Builder.create().nbt(new NbtPredicate(nbt)));
        return Criteria.VILLAGER_TRADE.create(
                new VillagerTradeCriterion.Conditions(Optional.empty(), Optional.of(villager), Optional.empty()));
    }
}
