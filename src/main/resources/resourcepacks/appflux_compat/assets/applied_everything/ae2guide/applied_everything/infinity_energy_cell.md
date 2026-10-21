---
navigation:
  parent: applied_everything/index.md
  title: Infinity Energy Cell
  icon: infinity_energy_cell
  position: 40
item_ids:
- infinity_energy_cell
---

# Infinity Energy Cell

<ItemImage id="infinity_energy_cell" scale="4" />

An Infinity Cell for energy, available because the Applied Flux addon is installed.

<RecipeFor id="infinity_energy_cell" />

A new cell has to be charged with **1,000,000,000 FE** (500,000,000 AE) before it works. Charge it in the
[Infinity Imprinter](imprinter.md) (up to 50,000 AE/t), in AE2's <ItemLink id="ae2:charger" /> (up to 10,000 AE/t), or in
any other mod's FE charger. Its name shows how far it has been charged.

Once full, put it into an <ItemLink id="ae2:drive" />: it supplies unlimited FE to the network through Applied Flux.
Applied Flux's <ItemLink id="appflux:flux_accessor" /> sends it on to machines from other mods. It also powers the ME
network itself if Applied Flux's `flux_accessor.enable` setting is turned on (it is off by default).
