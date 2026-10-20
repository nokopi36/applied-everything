# Applied Everything

[日本語](README.ja.md)

An addon for [Applied Energistics 2](https://github.com/AppliedEnergistics/Applied-Energistics-2) that adds the
**Infinity Cell**: an ME storage cell that supplies one item of your choice without limit. Any item from any mod can be
imprinted, as long as you can pay for the cell.

There are no modes, no limits to configure and no way to change an imprint later. The cost is the cell itself, and it is
the same for every item.

- Minecraft 26.1.2 / NeoForge
- Requires Applied Energistics 2 and GuideME
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

Items that differ from their default state (enchanted, renamed, damaged, filled containers and so on) can never be
imprinted. This depends on the item's state rather than its type, so it is not part of the tag and cannot be turned off.
It prevents duplicating the contents of containers.

## License

- Code: [MIT](LICENSE)
- Assets (textures, models, language files, guide pages, screen layouts): [CC BY-NC-SA 3.0](LICENSE-ASSETS). Some
  textures are derived from Applied Energistics 2's assets, which use the same license.
