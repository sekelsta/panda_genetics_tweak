# Panda Genetics Tweak
A Minecraft mod changing panda genetics so that brown is not a personality. Now you can breed playful brown pandas, lazy brown pandas, worried brown pandas, and so on. For more info see: https://www.curseforge.com/minecraft/mc-mods/panda-genetics-tweak

This mod does not add any new entities, but simply modifies Minecraft's pandas and anything else subclassing PandaEntity. It can be installed on existing worlds with no visible changes to the pandas. If uninstalled, pandas that existed before it was added and pandas found in the wild will stay the same as before, but pandas bred while it was installed may change color.

Server-side changes: Pandas store their brown gene in Forge's persistent data, separately from their personality gene. The server also sends this info to the client. When pandas are bred, the baby inherits its brown gene from its parents separetely from its personality gene.

Client-side changes: Pandas use a custom renderer and have more available textures. The client also receives the packets sent by the server so that the client can know whether the pandas are brown.
