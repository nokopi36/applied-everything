---
navigation:
  parent: applied_everything/index.md
  title: Infinity Cell
  icon: infinity_cell
  position: 30
item_ids:
- blank_infinity_cell
- infinity_cell
- blank_infinity_fluid_cell
- infinity_fluid_cell
---

# Infinity Cell

<Row>
  <ItemImage id="blank_infinity_cell" scale="4" />
  <ItemImage id="infinity_cell" scale="4" />
</Row>

An Infinity Cell supplies one item without limit. Put it in an <ItemLink id="ae2:drive" /> or an
<ItemLink id="ae2:me_chest" /> and the network never runs out of that item, no matter how many you take out.

## Blank Infinity Cell

<RecipeFor id="blank_infinity_cell" />

## Imprinting

Put a blank Infinity Cell and the item into the [Imprinter](imprinter.md). The result is an Infinity Cell for that
item.

An imprint cannot be changed. The cell does not work in a <ItemLink id="ae2:cell_workbench" /> either.

These items cannot be imprinted:

* Items from Applied Everything itself, and AE2's <ItemLink id="ae2:singularity" /> (an endless supply of singularities
  would make every further cell cost nothing but power).
* Items that differ from their default state: enchanted items and books, renamed items, damaged tools, filled shulker
  boxes, bundles or storage cells, dyed armor, and so on.

## In the Network

* Extracting the imprinted item always succeeds.
* Inserting the imprinted item voids it, like AE2's creative cell.
* Other items are not accepted.
* The cell cannot be placed inside other cells, such as portable cells.

## Infinity Fluid Cell

<Row>
  <ItemImage id="blank_infinity_fluid_cell" scale="4" />
  <ItemImage id="infinity_fluid_cell" scale="4" />
</Row>

The same, for a fluid. The blank cell is made like the blank Infinity Cell, with an
<ItemLink id="ae2:fluid_cell_housing" /> instead of the item cell housing.

<RecipeFor id="blank_infinity_fluid_cell" />

To imprint it, put it into the [Imprinter](imprinter.md) with 1,000 mB of the fluid in the tank. Fluids carrying
extra data cannot be imprinted.

## Infinity Energy Cell

Only with the Applied Flux addon installed. It is made like the blank Infinity Cell, with a fully charged
<ItemLink id="ae2:dense_energy_cell" /> instead of the item cell housing.

A new cell has to be charged with **1,000,000,000 FE** (500,000,000 AE) before it works. Charge it in the
[Imprinter](imprinter.md) (up to 50,000 AE/t), in AE2's <ItemLink id="ae2:charger" /> (up to 10,000 AE/t), or in any
other mod's FE charger. Once full, it supplies unlimited FE to the network through Applied Flux. Applied Flux's Flux
Accessor sends it on to machines from other mods. It also powers the ME network itself if Applied Flux's
`flux_accessor.enable` setting is turned on (it is off by default).
