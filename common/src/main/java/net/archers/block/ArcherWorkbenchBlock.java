package net.archers.block;

import net.archers.ArchersMod;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class ArcherWorkbenchBlock extends Block {
    public static Identifier ID = Identifier.fromNamespaceAndPath(ArchersMod.ID, "archers_workbench");
    public ArcherWorkbenchBlock(Properties settings) {
        super(settings);
    }

    // The block's `.hint` tooltip line is appended from `ArchersTooltip` (Block#appendTooltip was
    // removed in 1.21.11; item tooltips of block items go through the per-platform tooltip event).

    // MARK: Facing

    private static EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        builder.add(FACING);
    }

    // MARK: Partial transparency

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }
}
