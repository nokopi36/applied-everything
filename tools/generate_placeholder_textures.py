"""Generates pixel-art textures for Applied Everything: singularity, core and the Singularity Compressor parts.

The cell textures are derived from AE2 instead: see derive_cell_textures.py.

Usage: python tools/generate_placeholder_textures.py src/main/resources/assets/applied_everything/textures <preview_png>
"""
import math
import struct
import sys
import zlib


def hexc(h):
    h = h.lstrip('#')
    return (int(h[0:2], 16), int(h[2:4], 16), int(h[4:6], 16), 255)


CLEAR = (0, 0, 0, 0)


def write_png(path, pixels):
    h = len(pixels)
    w = len(pixels[0])
    raw = b''.join(b'\x00' + b''.join(bytes(p) for p in row) for row in pixels)

    def chunk(t, d):
        return struct.pack('>I', len(d)) + t + d + struct.pack('>I', zlib.crc32(t + d) & 0xffffffff)

    data = (b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', w, h, 8, 6, 0, 0, 0))
            + chunk(b'IDAT', zlib.compress(raw, 9)) + chunk(b'IEND', b''))
    with open(path, 'wb') as f:
        f.write(data)


def from_grid(rows, palette):
    assert len(rows) == 16, len(rows)
    out = []
    for r in rows:
        assert len(r) == 16, (r, len(r))
        out.append([palette[c] if c != '.' else CLEAR for c in r])
    return out


def orb(ramp, outline, radius=6.6, highlight=None):
    """Shaded sphere lit from the top-left. ramp: dark -> light colors."""
    px = [[CLEAR] * 16 for _ in range(16)]
    cx = cy = 7.5
    for y in range(16):
        for x in range(16):
            dx, dy = x - cx, y - cy
            r = math.hypot(dx, dy)
            if r <= radius:
                # Lambert-ish shading towards the top-left plus a darker centre
                nz = math.sqrt(max(0.0, 1 - (r / radius) ** 2))
                light = (-dx * 0.55 - dy * 0.55) / radius + nz * 0.6
                t = max(0.0, min(0.999, 0.25 + light * 0.75))
                px[y][x] = ramp[int(t * len(ramp))]
            elif r <= radius + 0.9:
                px[y][x] = outline
    if highlight:
        for (x, y) in [(5, 4), (4, 5), (5, 5)]:
            px[y][x] = highlight
    return px


def overlay(px, rows, palette):
    for y, r in enumerate(rows):
        for x, c in enumerate(r):
            if c != '.':
                px[y][x] = palette[c]
    return px


def compressed_singularity():
    ramp = [hexc(c) for c in ('#120a1e', '#24123a', '#3d1f63', '#5a2f8f', '#7a46b8')]
    px = orb(ramp, hexc('#0b0612'), highlight=hexc('#9d74e0'))
    # Dark event horizon in the middle with a bright accretion ring
    ring = [
        '................',
        '................',
        '................',
        '................',
        '................',
        '................',
        '......aaaa......',
        '....aakkkkaa....',
        '....aakkkkaa....',
        '......aaaa......',
        '................',
        '................',
        '................',
        '................',
        '................',
        '................',
    ]
    return overlay(px, ring, {'a': hexc('#b98cff'), 'k': hexc('#050208')})


def infinity_core():
    ramp = [hexc(c) for c in ('#3a0a3c', '#5c1460', '#82208a', '#a933b3', '#cf55d6')]
    px = orb(ramp, hexc('#1f0521'), radius=6.6, highlight=hexc('#e7a0ee'))
    sign = [
        '................',
        '................',
        '................',
        '................',
        '................',
        '................',
        '....WW....WW....',
        '...W..W..W..W...',
        '...W...WW...W...',
        '...W..W..W..W...',
        '....WW....WW....',
        '................',
        '................',
        '................',
        '................',
        '................',
    ]
    return overlay(px, sign, {'W': hexc('#fff2ff'), 'g': hexc('#ff8cf0')})


def preview(path, images, scale=16, gap=8):
    bg = hexc('#c6c6c6')
    checker = hexc('#b0b0b0')
    w = len(images) * (16 * scale + gap) + gap
    h = 16 * scale + gap * 2
    canvas = [[bg] * w for _ in range(h)]
    for i, img in enumerate(images):
        ox = gap + i * (16 * scale + gap)
        for y in range(16 * scale):
            for x in range(16 * scale):
                p = img[y // scale][x // scale]
                if p[3] == 0:
                    p = checker if ((x // scale) + (y // scale)) % 2 else bg
                canvas[gap + y][ox + x] = p
    write_png(path, canvas)


def compressor_frame():
    """Silver casing for the compressor frame, lit from the top left like AE2's machines."""
    rows = ['W' * 15 + 'w'] + ['W' + 'L' * 14 + 'd' for _ in range(14)] + ['w' + 'd' * 14 + 'D']
    rows = [list(r) for r in rows]
    for y in range(3, 13):
        rows[y][2] = 'l'
        rows[y][13] = 'm'
    for x in range(2, 14):
        rows[2][x] = 'l'
        rows[13][x] = 'm'
    for (x, y) in ((3, 3), (12, 3), (3, 12), (12, 12)):
        rows[y][x] = 'O'
    palette = {'W': hexc('#f2f2f2'), 'w': hexc('#dedfe3'), 'L': hexc('#cbccd4'), 'l': hexc('#adb0c4'),
               'm': hexc('#9a9fb4'), 'd': hexc('#696d88'), 'D': hexc('#4d4d67'), 'O': hexc('#413f54')}
    return from_grid([''.join(r) for r in rows], palette)


def _pyramid(dark, mid, light):
    # Diagonal stripes give the steps a little texture
    px = [[CLEAR] * 16 for _ in range(16)]
    for y in range(16):
        for x in range(16):
            band = (x + y) % 6
            px[y][x] = light if band == 0 else (mid if band in (1, 5) else dark)
    return px


def compressor_pyramid():
    return _pyramid(hexc('#5f228c'), hexc('#7a32b0'), hexc('#9a4fd0'))


def compressor_pyramid_lit():
    return _pyramid(hexc('#a050e0'), hexc('#c47cf2'), hexc('#f0c8ff'))


def main():
    out_dir, preview_path = sys.argv[1], sys.argv[2]
    textures = {
        'item/compressed_singularity': compressed_singularity(),
        'item/infinity_core': infinity_core(),
        'block/singularity_compressor_frame': compressor_frame(),
        'block/singularity_compressor_pyramid': compressor_pyramid(),
        'block/singularity_compressor_pyramid_lit': compressor_pyramid_lit(),
    }
    for name, px in textures.items():
        write_png(f'{out_dir}/{name}.png', px)
    preview(preview_path, list(textures.values()))


if __name__ == '__main__':
    main()
