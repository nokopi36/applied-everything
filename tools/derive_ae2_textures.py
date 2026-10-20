"""Derives textures from AE2's assets by recolouring them.

- Infinity Cells: AE2's creative storage cell with the band recoloured
- Singularity Compressor GUI: AE2's inscriber GUI with the two press slots and their arrows removed,
  and the rest moved to the centre

The results are derivative works of AE2's assets and are licensed under CC BY-NC-SA 3.0
(see LICENSE-ASSETS). Requires Pillow.

Usage: python tools/derive_ae2_textures.py <ae2_textures_dir> <output_textures_dir>
  e.g. python tools/derive_ae2_textures.py refs/ae2/src/main/resources/assets/ae2/textures
           src/main/resources/assets/applied_everything/textures
"""
import sys

from PIL import Image, ImageDraw

# The creative cell's casing is thicker than the regular cells, which sets the Infinity Cell apart
CELL_SOURCE = 'item/creative_storage_cell.png'

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


# Areas of AE2's inscriber GUI covering the top/bottom press slots and the arrows joining them (x0, y0, x1, y1)
INSCRIBER_PRESS_AREAS = [(36, 20, 57, 41), (36, 52, 57, 73), (58, 20, 77, 35), (58, 58, 77, 73)]


# The remaining input slot, arrow, output slot and progress bar (x0, y0, x1, y1), and how far to move them
# left so they sit in the middle of the 176 px wide panel
INSCRIBER_MACHINE_AREA = (60, 35, 140, 60)
CENTERING_SHIFT = -12


def compressor_gui(src):
    im = Image.open(src).convert('RGBA')
    background = im.getpixel((20, 30))
    draw = ImageDraw.Draw(im)
    for area in INSCRIBER_PRESS_AREAS:
        draw.rectangle(area, fill=background)
    x0, y0, x1, y1 = INSCRIBER_MACHINE_AREA
    machine = im.crop((x0, y0, x1 + 1, y1 + 1))
    draw.rectangle(INSCRIBER_MACHINE_AREA, fill=background)
    im.paste(machine, (x0 + CENTERING_SHIFT, y0))
    return im


def main():
    ae2_dir, out_dir = sys.argv[1], sys.argv[2]
    cell = f'{ae2_dir}/{CELL_SOURCE}'
    recolour(cell, dict(zip(AE2_BAND, INFINITY_BAND))).save(f'{out_dir}/item/infinity_cell.png')
    recolour(cell, dict(zip(AE2_BAND, BLANK_BAND))).save(f'{out_dir}/item/blank_infinity_cell.png')

    compressor_gui(f'{ae2_dir}/guis/inscriber.png').save(f'{out_dir}/gui/singularity_compressor.png')


if __name__ == '__main__':
    main()
