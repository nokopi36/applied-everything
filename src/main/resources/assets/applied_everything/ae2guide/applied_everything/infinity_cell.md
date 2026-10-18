---
navigation:
  parent: applied_everything/index.md
  title: Infinity Cell
  icon: infinity_cell
  position: 30
item_ids:
- blank_infinity_cell
- infinity_cell
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

Place a blank Infinity Cell and **one** other item anywhere in a crafting grid. The result is an Infinity Cell for that
item.

An imprint cannot be changed. The cell does not work in a <ItemLink id="ae2:cell_workbench" /> either.

These items cannot be imprinted:

* Items from Applied Everything itself.
* Items that differ from their default state: enchanted items and books, renamed items, damaged tools, filled shulker
  boxes, bundles or storage cells, dyed armor, and so on.

## In the Network

* Extracting the imprinted item always succeeds.
* Inserting the imprinted item voids it, like AE2's creative cell.
* Other items are not accepted.
* The cell cannot be placed inside other cells, such as portable cells.
