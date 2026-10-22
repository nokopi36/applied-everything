# Applied Everything

[日本語](README.ja.md)

An addon for [Applied Energistics 2](https://github.com/AppliedEnergistics/Applied-Energistics-2) that adds the
**Infinity Cell**: an ME storage cell that supplies one item of your choice without limit. Any item from any mod can be
imprinted, as long as you can pay for the cell.

There are no modes, no limits to configure and no way to change an imprint later. The cost is the cell itself, and it is
the same for every item.

- Download: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/applied-everything)
- Minecraft 26.1.2 / NeoForge
- Requires Applied Energistics 2 and GuideME
- Optional: [Applied Flux](https://modrinth.com/mod/appflux) adds the Infinity Energy Cell, which supplies unlimited FE
- Jade, WTHIT and The One Probe show the progress of the machines (through AE2's built-in support; no extra dependency)
- In-game documentation: open the AE2 guide (hold **G** over an item) and look for "Applied Everything"

## For Modpack Makers

### Imprint Blacklist

Items in the item tag `applied_everything:imprint_blacklist` cannot be imprinted into an Infinity Cell.

Default contents:

| Item | Why |
|---|---|
| All items from Applied Everything | Imprinting the materials would skip the cost of further cells |
| `ae2:singularity` | An endless supply of singularities would make every further cell cost nothing but power |

Change it with a datapack at `data/applied_everything/tags/item/imprint_blacklist.json`. Use `values` to add entries
and NeoForge's `remove` to take default entries out:

```json
{
  "values": [
    "minecraft:nether_star"
  ],
  "remove": [
    "ae2:singularity"
  ]
}
```

Prefer `remove` over `"replace": true`: items added to the default list in later versions stay blacklisted.

To see the list currently in effect, including your changes:

```
/neoforge tags minecraft:item get applied_everything:imprint_blacklist
```

Fluids in the fluid tag `applied_everything:imprint_blacklist` cannot be imprinted into an Infinity Fluid Cell. It
is empty by default; change it the same way at `data/applied_everything/tags/fluid/imprint_blacklist.json`, and check
it with `/neoforge tags minecraft:fluid get applied_everything:imprint_blacklist`.

Items that differ from their default state (enchanted, renamed, damaged, filled containers and so on) can never be
imprinted. This depends on the item's state rather than its type, so it is not part of the tag and cannot be turned off.
It prevents duplicating the contents of containers.

### Recipes

All crafting recipes are regular datapack recipes in `data/applied_everything/recipe/`:
`blank_infinity_cell`, `blank_infinity_fluid_cell`, `infinity_core`, `singularity_compressor`, `imprinter` and
`infinity_energy_cell`. Override one by placing a file with the same name in your datapack, or remove it with:

```json
{
  "neoforge:conditions": [{ "type": "neoforge:never" }]
}
```

KubeJS and CraftTweaker work as with any other crafting recipe. The Singularity Compressor's process is not a
recipe; change its cost in the server config below.

### Server Config

`config/applied_everything-server.toml` (or `serverconfig/applied_everything-server.toml` in a world folder, for that
world only). On a server, the values are sent to the clients.

| Key | Default | Meaning |
|---|---|---|
| `singularity_compressor.singularities_per_compression` | 9 | Singularities consumed by one compression (1-64) |
| `singularity_compressor.energy_per_compression` | 10,000,000 | AE consumed by one compression |
| `singularity_compressor.base_draw_per_tick` | 5,000 | AE per tick without Acceleration Cards |
| `singularity_compressor.draw_per_card` | 5,000 | Additional AE per tick for each Acceleration Card (the total per compression stays the same) |
| `infinity_imprinter.energy_per_imprint` | 100,000 | AE consumed by one imprint |
| `infinity_imprinter.ticks_per_imprint` | 100 | Ticks one imprint takes |
| `infinity_imprinter.energy_cell_charge_rate` | 50,000 | AE per tick pushed into an Infinity Energy Cell |
| `infinity_energy_cell.capacity_fe` | 1,000,000,000 | FE needed before an Infinity Energy Cell becomes infinite |

- The in-game guide always shows the default values.
- Raising `capacity_fe` in an existing world makes full Infinity Energy Cells partly charged again: they stop supplying
  FE until they are charged up to the new capacity. Lowering it is safe.
- The number of Acceleration Cards (4) cannot be changed.

## License

- Code: [MIT](LICENSE)
- Assets (textures, models, language files, guide pages, screen layouts): [CC BY-NC-SA 3.0](LICENSE-ASSETS). Some
  textures are derived from Applied Energistics 2's assets, which use the same license.
