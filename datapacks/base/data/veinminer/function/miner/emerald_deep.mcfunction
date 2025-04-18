execute if score hunger settings matches 1.. run effect give @s minecraft:hunger 2 120 true

# Apply fortune
execute if block ~ ~ ~ deepslate_emerald_ore unless score @s veinminer_const matches 0 run function veinminer:miner/check_fortune {block:"deepslate_emerald_ore",type:"deepslate"}

# Destroy current block
execute if block ~ ~ ~ deepslate_emerald_ore run setblock ~ ~ ~ air destroy

# Recursion to affect whole vein
execute positioned ~ ~ ~1 if block ~ ~ ~ deepslate_emerald_ore run function veinminer:miner/emerald_deep
execute positioned ~ ~ ~-1 if block ~ ~ ~ deepslate_emerald_ore run function veinminer:miner/emerald_deep
execute positioned ~1 ~ ~ if block ~ ~ ~ deepslate_emerald_ore run function veinminer:miner/emerald_deep
execute positioned ~-1 ~ ~ if block ~ ~ ~ deepslate_emerald_ore run function veinminer:miner/emerald_deep
execute positioned ~ ~1 ~ if block ~ ~ ~ deepslate_emerald_ore run function veinminer:miner/emerald_deep
execute positioned ~ ~-1 ~ if block ~ ~ ~ deepslate_emerald_ore run function veinminer:miner/emerald_deep
