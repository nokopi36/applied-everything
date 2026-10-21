"""Derives textures from AE2's assets by recolouring them.

- Infinity Cells, Infinity Fluid Cells and the Infinity Energy Cell: AE2's creative storage cell with the band recoloured
- Singularity Compressor GUI: AE2's inscriber GUI with the two press slots and their arrows removed,
  and the rest moved to the centre
- Icon of the bundled Applied Flux integration pack: the Infinity Energy Cell, enlarged
- Mod logo (mod list and CurseForge project icon): the Infinity Cell, enlarged on a dark square
- Imprinter GUI: AE2's inscriber GUI with one more slot (the fluid container) copied to the left of the bottom slot

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

# Infinity Fluid Cell: aqua band with the same gold highlights, and its unlit blank
FLUID_BAND = [(22, 104, 158, 255), (46, 170, 222, 255), (255, 204, 77, 255)]
BLANK_FLUID_BAND = [(30, 48, 68, 255), (42, 68, 92, 255), (56, 88, 118, 255)]

# Infinity Energy Cell: amber band with white-hot highlights
ENERGY_BAND = [(170, 92, 14, 255), (236, 150, 30, 255), (255, 240, 170, 255)]

# Where the bundled Applied Flux integration pack lives, relative to the project root
PACK_ICON_DIR = 'src/main/resources/resourcepacks/appflux_compat'

# Mod logo, relative to the project root. Referenced from neoforge.mods.toml
LOGO_PATH = 'src/main/resources/logo.png'
LOGO_SIZE = 400
LOGO_SCALE = 20
LOGO_BACKGROUND = (27, 21, 48, 255)


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


# The inscriber's bottom press slot frame (x0, y0, x1, y1), copied this far left for the container slot
INSCRIBER_BOTTOM_SLOT = (36, 52, 57, 73)
CONTAINER_SLOT_SHIFT = -24


def imprinter_gui(src):
    im = Image.open(src).convert('RGBA')
    x0, y0, x1, y1 = INSCRIBER_BOTTOM_SLOT
    frame = im.crop((x0, y0, x1 + 1, y1 + 1))
    im.paste(frame, (x0 + CONTAINER_SLOT_SHIFT, y0))
    return im


def logo(cell):
    im = Image.new('RGBA', (LOGO_SIZE, LOGO_SIZE), LOGO_BACKGROUND)
    big = cell.resize((cell.width * LOGO_SCALE, cell.height * LOGO_SCALE), Image.NEAREST)
    offset = ((LOGO_SIZE - big.width) // 2, (LOGO_SIZE - big.height) // 2)
    im.alpha_composite(big, offset)
    return im


def main():
    ae2_dir, out_dir = sys.argv[1], sys.argv[2]
    cell = f'{ae2_dir}/{CELL_SOURCE}'
    infinity_cell = recolour(cell, dict(zip(AE2_BAND, INFINITY_BAND)))
    infinity_cell.save(f'{out_dir}/item/infinity_cell.png')
    recolour(cell, dict(zip(AE2_BAND, BLANK_BAND))).save(f'{out_dir}/item/blank_infinity_cell.png')
    recolour(cell, dict(zip(AE2_BAND, FLUID_BAND))).save(f'{out_dir}/item/infinity_fluid_cell.png')
    recolour(cell, dict(zip(AE2_BAND, BLANK_FLUID_BAND))).save(f'{out_dir}/item/blank_infinity_fluid_cell.png')
    energy_cell = recolour(cell, dict(zip(AE2_BAND, ENERGY_BAND)))
    energy_cell.save(f'{out_dir}/item/infinity_energy_cell.png')
    # Pack icons are shown at 32 px or more: enlarge without smoothing to keep the pixels sharp
    energy_cell.resize((128, 128), Image.NEAREST).save(f'{PACK_ICON_DIR}/pack.png')
    logo(infinity_cell).save(LOGO_PATH)

    compressor_gui(f'{ae2_dir}/guis/inscriber.png').save(f'{out_dir}/gui/singularity_compressor.png')
    imprinter_gui(f'{ae2_dir}/guis/inscriber.png').save(f'{out_dir}/gui/imprinter.png')


if __name__ == '__main__':
    main()
