package net.archers.block;

import net.archers.ArchersMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class ArcherWorkbenchBlock extends Block {
    public static Identifier ID = Identifier.of(ArchersMod.ID, "archers_workbench");
    public ArcherWorkbenchBlock(Settings settings) {
        super(settings);
    }

    // The block's `.hint` tooltip line is appended from `ArchersTooltip` (Block#appendTooltip was
    // removed in 1.21.11; item tooltips of block items go through the per-platform tooltip event).

    // MARK: Facing

    private static EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        FACING = Properties.HORIZONTAL_FACING;
        builder.add(FACING);
    }

    // MARK: Partial transparency

    @Override
    protected boolean isTransparent(BlockState state) {
        return true;
    }
}
