---
navigation:
  parent: applied_everything/index.md
  title: Singularity Compressor
  icon: singularity_compressor
  position: 10
item_ids:
- singularity_compressor
---

# Singularity Compressor

<BlockImage id="singularity_compressor" scale="6" />

The Singularity Compressor turns 9 <ItemLink id="ae2:singularity" /> into one
<ItemLink id="compressed_singularity" />. It is the only way to make compressed singularities.

## Power

* Each compression consumes **10,000,000 AE** (20,000,000 FE).
* Without cards the compressor draws **5,000 AE/t**, so one compression takes 100 seconds.
* It takes power from the ME network it is connected to, or from FE cables attached directly to it.
* It only stores enough power for two ticks, so it cannot be charged up in advance. If power runs short it simply works
  more slowly; progress is kept.
* It only draws power while it has at least 9 singularities and room in its output.
* Taking singularities out so that fewer than 9 remain resets the progress. The power spent so far is lost.

## Acceleration Cards

Up to 4 <ItemLink id="ae2:speed_card" /> can be installed. Each card adds 5,000 AE/t to the draw, so with 4 cards one
compression takes 20 seconds. **The total power per compression does not change**: cards only make it faster.

## Automation

* Singularities can be inserted from any side, for example with a hopper, an
  <ItemLink id="ae2:interface" /> or a <ItemLink id="ae2:pattern_provider" />.
* Compressed singularities can be extracted from any side. Singularities in the input cannot be pulled out.
* The compressor does not push its output out by itself. For autocrafting, put a processing pattern
  (9 singularities to 1 compressed singularity) in a pattern provider next to the compressor, and return the result
  with an <ItemLink id="ae2:import_bus" /> or an interface.

## Recipe

<RecipeFor id="singularity_compressor" />
