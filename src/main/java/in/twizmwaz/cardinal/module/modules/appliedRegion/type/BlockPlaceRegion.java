package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BlockPlaceRegion extends AppliedRegion {

    public BlockPlaceRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlockPlaced().getLocation().toVector())) && filter.evaluate(event.getPlayer(), event.getBlockPlaced(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
        if ((event.getBlock().getType().equals(Material.ACACIA_DOOR) || event.getBlock().getType().equals(Material.BIRCH_DOOR) || event.getBlock().getType().equals(Material.DARK_OAK_DOOR) || event.getBlock().getType().equals(Material.IRON_DOOR) || event.getBlock().getType().equals(Material.JUNGLE_DOOR) || event.getBlock().getType().equals(Material.SPRUCE_DOOR) || event.getBlock().getType().equals(Material.WOOD_DOOR) || event.getBlock().getType().equals(Material.WOODEN_DOOR) || event.getBlock().getType().equals(Material.LONG_GRASS)) && region.contains(new BlockRegion(null, event.getBlock().getLocation().add(0, 1, 0).toVector())) && !event.isCancelled() && filter.evaluate(event.getPlayer(), event.getBlockPlaced(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Material newMaterial = (event.getBucket().equals(Material.WATER_BUCKET) ? Material.WATER : (event.getBucket().equals(Material.LAVA_BUCKET) ? Material.LAVA : Material.AIR));
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector())) && filter.evaluate(event.getPlayer(), newMaterial, event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBLiquidFlow(BlockFromToEvent event) {
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getToBlock().getLocation().toVector())) && filter.evaluate(event.getToBlock(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (region.contains(block.getRelative(event.getDirection()).getLocation().toVector()) && filter.evaluate(block, event).equals(FilterState.DENY)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (region.contains(block.getRelative(event.getDirection()).getLocation().toVector()) && filter.evaluate(block, event).equals(FilterState.DENY)) {
                event.setCancelled(true);
            }
        }
    }
}
