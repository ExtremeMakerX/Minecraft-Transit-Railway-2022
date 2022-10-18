package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockLiftPanelEven1 extends BlockLiftPanel1 {

	public BlockLiftPanelEven1(boolean isFlat) {
		super(false, isFlat);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LEFT, SIDE, TEMP);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLiftPanelEven1(pos, state);
	}

	public static class TileEntityLiftPanelEven1 extends TileEntityLiftPanel1Base {

		public TileEntityLiftPanelEven1(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_EVEN_1_TILE_ENTITY.get(), pos, state, true);
		}

		@Override
		protected void convert() {
			if (!converted && level != null) {
				final BlockState state = level.getBlockState(getBlockPos());
				if (IBlock.getStatePropertySafe(state, TEMP)) {
					final Direction newFacing = IBlock.getStatePropertySafe(state, FACING).getOpposite();
					final IBlock.EnumSide newSide = IBlock.getStatePropertySafe(state, LEFT) ? EnumSide.LEFT : EnumSide.RIGHT;
					level.setBlockAndUpdate(getBlockPos(), state.setValue(FACING, newFacing).setValue(SIDE, newSide));
				}
				converted = true;
				setChanged();
				syncData();
			}
		}
	}
}
