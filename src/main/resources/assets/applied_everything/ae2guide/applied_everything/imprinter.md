---
navigation:
  parent: applied_everything/index.md
  title: Imprinter
  icon: imprinter
  position: 15
item_ids:
- imprinter
---

# Imprinter

<BlockImage id="imprinter" scale="6" />

The Imprinter turns a blank cell into an Infinity Cell. Each imprint takes **5 seconds** and **100,000 AE**, drawn from
the ME network it is connected to or from FE cables attached directly to it.

## Slots

* **Cell** (middle): a <ItemLink id="blank_infinity_cell" /> or a <ItemLink id="blank_infinity_fluid_cell" />.
* **Item** (top): the item to imprint into a blank Infinity Cell. One is used per imprint.
* **Tank** (bottom): 1,000 mB of the fluid to imprint into a blank Infinity Fluid Cell. Fill it with pipes, or by
  clicking the slot with a filled container.
* **Container** (bottom left): a filled fluid container, such as a water bucket, is poured into the tank. The empty
  container stays in this slot.
* **Output** (right): the imprinted cell.

Items and fluids that cannot be imprinted do not go into the item slot or the tank.

## Automation

* Blank cells go into the cell slot and other items into the item slot, from any side.
* Fluids can be piped into the tank from any side.
* Imprinted cells and emptied containers can be extracted from any side.

## Recipe

<RecipeFor id="imprinter" />
