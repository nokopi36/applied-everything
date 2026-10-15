"""Derives the Infinity Cell textures from AE2's creative storage cell by recolouring the band.

The results are derivative works of AE2's assets and are licensed under CC BY-NC-SA 3.0
(see LICENSE-ASSETS). Requires Pillow.

Usage: python tools/derive_cell_textures.py <ae2_item_texture_dir> <output_dir>
  e.g. python tools/derive_cell_textures.py refs/ae2/src/main/resources/assets/ae2/textures/item \
           src/main/resources/assets/applied_everything/textures/item
"""
import sys

from PIL import Image

# The creative cell's casing is thicker than the regular cells, which sets the Infinity Cell apart
SOURCE = 'creative_storage_cell.png'

# Band colours of AE2's creative cell: dark, mid, light
AE2_BAND = [(143, 63, 118, 255), (193, 81, 137, 255), (230, 158, 191, 255)]

# Infinity Cell: violet band with gold highlights
INFINITY_BAND = [(112, 36, 160, 255), (176, 72, 224, 255), (255, 204, 77, 255)]

# Blank Infinity Cell: the same band, unlit
BLANK_BAND = [(52, 38, 72, 255), (72, 54, 98, 255), (92, 70, 124, 255)]


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
    src = f'{ae2_dir}/{SOURCE}'
    recolour(src, dict(zip(AE2_BAND, INFINITY_BAND))).save(f'{out_dir}/infinity_cell.png')
    recolour(src, dict(zip(AE2_BAND, BLANK_BAND))).save(f'{out_dir}/blank_infinity_cell.png')


if __name__ == '__main__':
    main()
