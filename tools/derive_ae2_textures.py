"""Derives textures from AE2's assets by recolouring them.

- Infinity Cells: AE2's creative storage cell with the band recoloured
- Singularity Compressor: AE2's condenser (sides) and IO port top (top/bottom) with blue recoloured

The results are derivative works of AE2's assets and are licensed under CC BY-NC-SA 3.0
(see LICENSE-ASSETS). Requires Pillow.

Usage: python tools/derive_ae2_textures.py <ae2_textures_dir> <output_textures_dir>
  e.g. python tools/derive_ae2_textures.py refs/ae2/src/main/resources/assets/ae2/textures
           src/main/resources/assets/applied_everything/textures
"""
import sys

from PIL import Image

# The creative cell's casing is thicker than the regular cells, which sets the Infinity Cell apart
CELL_SOURCE = 'item/creative_storage_cell.png'

# Band colours of AE2's creative cell: dark, mid, light
AE2_BAND = [(143, 63, 118, 255), (193, 81, 137, 255), (230, 158, 191, 255)]

# Infinity Cell: violet band with gold highlights
INFINITY_BAND = [(112, 36, 160, 255), (176, 72, 224, 255), (255, 204, 77, 255)]

# Blank Infinity Cell: the same band, unlit
BLANK_BAND = [(52, 38, 72, 255), (72, 54, 98, 255), (92, 70, 124, 255)]

# AE2's blue machine accents -> violet, and the condenser's purple core -> gold
MACHINE_COLOURS = {
    (112, 140, 186, 255): (120, 72, 170, 255),
    (125, 169, 210, 255): (160, 90, 215, 255),
    (156, 211, 255, 255): (205, 130, 250, 255),
    (218, 255, 255, 255): (255, 225, 140, 255),
    (145, 93, 205, 255): (255, 190, 60, 255),
    (226, 163, 227, 255): (255, 240, 170, 255),
    (90, 71, 158, 255): (190, 120, 30, 255),
}


def recolour(src, mapping):
    im = Image.open(src).convert('RGBA')
    px = im.load()
    for y in range(im.height):
        for x in range(im.width):
            if px[x, y] in mapping:
                px[x, y] = mapping[px[x, y]]
    return im


def main():
    ae2_dir, out_dir = sys.argv[1], sys.argv[2]
    cell = f'{ae2_dir}/{CELL_SOURCE}'
    recolour(cell, dict(zip(AE2_BAND, INFINITY_BAND))).save(f'{out_dir}/item/infinity_cell.png')
    recolour(cell, dict(zip(AE2_BAND, BLANK_BAND))).save(f'{out_dir}/item/blank_infinity_cell.png')

    side = recolour(f'{ae2_dir}/block/condenser.png', MACHINE_COLOURS)
    top = recolour(f'{ae2_dir}/block/io_port_top.png', MACHINE_COLOURS)
    side.save(f'{out_dir}/block/singularity_compressor_side.png')
    top.save(f'{out_dir}/block/singularity_compressor_top.png')
    top.save(f'{out_dir}/block/singularity_compressor_bottom.png')


if __name__ == '__main__':
    main()
